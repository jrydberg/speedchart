/*
 * This file was original part of Timepedia Chronoscope.
 *
 * Copyright (C) Timepedia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package org.gwt.speedchart.client.util.date;

/**
 * @author chad takahashi
 */
public class GregorianConstants {
  
  /**
   * Array of pre-computed leap year indicator flags from year offsets [0..399].
   */
  public final boolean[] leapYearFlags;
  
  public final double msInLeapYear;
  
  public final double msInYear;
  
  public final double msInLeapCentury; 
  
  public final double msInNonLeapCentury; 
  
  public final int[] yearOffsetsInDays;
      
  public final double msIn4centuryPeriod;

  public final double[] yearOffsetsInMs;

  public GregorianConstants() {
    leapYearFlags = calcLeapYearFlags();
    msInLeapYear = FastChronoDate.MS_IN_LEAPYEAR;
    msInYear = FastChronoDate.MS_IN_YEAR;
    msInLeapCentury = (msInLeapYear + (msInYear * 3)) * 25; 
    msInNonLeapCentury = 
      (msInYear * 4) + (msInLeapYear + (msInYear * 3)) * 24; 
    yearOffsetsInDays = calcYearOffsetsInDays(leapYearFlags);
    msIn4centuryPeriod = 
      (msInLeapCentury + (msInNonLeapCentury * 3));
    yearOffsetsInMs = calcYearOffsetsInMillis(leapYearFlags, msInLeapYear, msInYear);
  }
  
  /**
   * Calculates an array containing 4 centuries of leap year flags.
   */
  private static boolean[] calcLeapYearFlags() {
    boolean[] leapYearFlags = new boolean[400];
    for (int y = 0; y < leapYearFlags.length; y++) {
      leapYearFlags[y] = (y == 0) || (y % 4 == 0 && y % 100 != 0);
    }
    return leapYearFlags;
  }

  /**
   * Returns an array containing 4 centuries of year offsets in days.
   * Element [0] = 0, meaning that 0 days have passed since the first day of the 4 century
   * period (obviously); element [1] = the number of days that have passed in 1 year
   * from the 4 century period, and so on.
   * <p>
   * A period of 4 centuries was chosen because that's the smallest interval that repeats
   * indefinitely according to the Gregorian calendar.
   */
  private static int[] calcYearOffsetsInDays(boolean[] leapYearFlags) {
    int[] offsets = new int[400];
    offsets[0] = 0;
    
    int counter = 0;
    int i = 1;
    while (i < offsets.length) {
      boolean isLeapYear = leapYearFlags[counter];
      offsets[i] = offsets[i - 1] + (isLeapYear ? 366 : 365);
      ++counter;
      ++i;
    }
    
    return offsets;
  }

  /**
   * Returns an array containing 4 centuries of year offset timestamps in milliseconds.
   * Element [0] = 0, representing Jan-01 at midnight for the first year; 
   * element [1] = the timestamp for Jan-02 at midnight, and so on.
   * <p>
   * A period of 4 centuries was chosen because that's the smallest interval that repeats
   * indefinitely according to the Gregorian calendar.
   */
  private static double[] calcYearOffsetsInMillis(boolean[] leapYearFlags,
      double msInLeapYear, double msInYear) {
    double[] offsets = new double[400];
    offsets[0] = 0;
    
    int counter = 0;
    int i = 1;
    while (i < offsets.length) {
      boolean isLeapYear = leapYearFlags[counter];
      offsets[i] = offsets[i - 1] + (isLeapYear ? msInLeapYear : msInYear);
      ++counter;
      ++i;
    }
    
    return offsets;
  }
  
}
