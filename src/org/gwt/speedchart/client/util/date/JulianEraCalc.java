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

import org.gwt.speedchart.client.util.MathUtil;

/**
 * Handles date calculations up to but not including 1582-JAN-01 00h:00m:00s.0ms.
 * 
 * @author chad takahashi
 */
public class JulianEraCalc extends EraCalc {

  // The latest leap year that this date object can represent (1582 marks the end of the 
  // Julian era).
  private static final int MAX_LEAP_YEAR = 1580;
  
  // millisecond timestamp corresponding to Jan-01 00:00:00.0 of maxLeapYear
  private static final double MAX_LEAP_YEAR_TIMESTAMP = getTimestampForYear(MAX_LEAP_YEAR);
  
  // The latest year supported by this EraCalc object
  private static final int MAX_YEAR = 1581;
  
  private static final double MS_IN_LEAPYEAR = FastChronoDate.MS_IN_LEAPYEAR;
  
  private static final double MS_IN_YEAR = FastChronoDate.MS_IN_YEAR;
  
  private static final double MS_IN_4YR_PERIOD = MS_IN_LEAPYEAR + (MS_IN_YEAR * 3);
  
  private static final int DAYS_IN_4YR_PERIOD = 366 + 365 + 365 + 365;
  
  // The largest timestamp allowed, plus 1 ms.  This value must correspond to
  // the timestamp that represents Jan-01-yyyy 00:00:00.0, 
  // where 'yyyy' = getMaxYear() + 1. For example, if maxYear=1581, then maxTimeStamp
  // should be set to the date 1582-Jan-01 00:00:00.0.  This means that the
  // largest valid date would be '1581-Dec-31 23:59:59.999'.
  private static final double TS_1582_JAN_01 = getTimestampForYear(1582);
    
  // [0] = # of days from beginning of 4-year period to Jan-01 of year 0
  // [1] = # of days from beginning of 4-year period to Jan-01 of year 1
  // [2] = # of days from beginning of 4-year period to Jan-01 of year 2
  // [3] = # of days from beginning of 4-year period to Jan-01 of year 3
  private static final int[] YEAR_OFFSETS_IN_DAYS = 
      {0, 366, (366 + 365), (366 + 365 + 365), (366 + 365 +365 + 365)};

  private static final double[] YEAR_OFFSETS_IN_MS = new double[] {
    0,
    MS_IN_LEAPYEAR, 
    MS_IN_LEAPYEAR + MS_IN_YEAR, 
    MS_IN_LEAPYEAR + MS_IN_YEAR + MS_IN_YEAR, 
    MS_IN_LEAPYEAR + MS_IN_YEAR + MS_IN_YEAR + MS_IN_YEAR, 
    };

  @Override
  public double calcYearField(double timeInMs, DateFields dateFields) {
    checkTimestampLessThan(timeInMs, TS_1582_JAN_01);
    
    // Calculate the starting point of the 4-year period that contains 'timeInMs'
    final double normalizedMs = timeInMs - MAX_LEAP_YEAR_TIMESTAMP;
    final int fourYearIndex = (int)Math.floor(normalizedMs / MS_IN_4YR_PERIOD);
    final int yearStart = MAX_LEAP_YEAR + (fourYearIndex * 4);
    final double fourYearPeriodStart = 
        MAX_LEAP_YEAR_TIMESTAMP + (fourYearIndex * MS_IN_4YR_PERIOD);
    
    // Calculate year
    final double fourYearPeriodOffset = timeInMs - fourYearPeriodStart;
    for (int i = 0; i < YEAR_OFFSETS_IN_MS.length - 1; i++) {
      if (fourYearPeriodOffset < YEAR_OFFSETS_IN_MS[i + 1]) {
        dateFields.year = yearStart + i;
        return fourYearPeriodOffset - YEAR_OFFSETS_IN_MS[i];
      }
    }
    
    throw new RuntimeException("Unable to determine year from timestamp");
  }

  public DayOfWeek calcDayOfWeek(int year, int month, int day) {
    // Calculate the number of days that have passed since Jan-01 of the 4-year period
    // in which the date implied by (year, month, day) is a member.
    int yearOffsetIndex = MathUtil.mod(year, 4);
    int numDaysFromPeriodStart = YEAR_OFFSETS_IN_DAYS[yearOffsetIndex];
    numDaysFromPeriodStart += this.getMonthOffsetsInDays(isLeapYear(year))[month];
    numDaysFromPeriodStart += (day - 1);

    // E.g. if MAX_LEAP_YEAR=1580, then: 
    //    year=[1580,1581] -> 0, year=[1576-1579] -> 1, year=[1572-1575] -> 2, ... 
    int fourYearIdx = -(int)(Math.floor((year - MAX_LEAP_YEAR) / 4.0));
    
    /*
    System.out.println("TESTING: y=" + year + 
        "; fourYearIndex=" + fourYearIdx + 
        "; yearOffsetIndex=" + yearOffsetIndex + 
        "; daysFromPeriodStart=" + daysFromPeriodStart
        );
    */
    
    // This formula assumes that:
    //    1) 1580-JAN-01 is a Friday (5 == Friday).
    //    2) Starting from 1580, The day-of-week of JAN-01 of the previous 4-year period
    //       is always 2 days more than the current 4-year period.
    //       E.g. 1580-JAN-01 is a Friday, 1576-JAN-01 is Sunday, 1572-JAN-01 is Tuesday.
    int d = (2 * fourYearIdx) + 5;
    return FastChronoDate.DAYS_OF_WEEK[MathUtil.mod(numDaysFromPeriodStart + d, 7)];
  }
  
  @Override
  public double calcYearTimestamp(int year) {
    if (year > MAX_YEAR) {
      throw new IllegalArgumentException("Year too big for JulianEraCalc: " + year);
    }
    
    final int yearDiff = MAX_LEAP_YEAR - year;
    double ts = MAX_LEAP_YEAR_TIMESTAMP;
    ts -= (Math.ceil((double)yearDiff / 4.0) * MS_IN_4YR_PERIOD);

    final int yearOffsetIndex = MathUtil.mod(year, 4);
    ts += YEAR_OFFSETS_IN_MS[yearOffsetIndex];

    return ts;
  }
  
  @Override
  public int getMaxYear() {
    return MAX_YEAR;
  }
  
  @Override
  public boolean isLeapYear(int year) {
    return (year & 0x3) == 0; // same as (year % 4 == 0)
  }

}
