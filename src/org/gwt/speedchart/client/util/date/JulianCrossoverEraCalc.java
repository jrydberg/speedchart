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
 * Only handles year 1582, which is the year in which the Julian calendar was replaced
 * by the Gregorian calendar.  This calendar has a 355-day year (October 5th through
 * October 14th were removed).
 * 
 * @author chad takahashi
 */
class JulianCrossoverEraCalc extends EraCalc {
  
  private static final DayOfWeek[] DAYS_OF_WEEK = DayOfWeek.values();
  
  // Oct. is special month having only 21 days (the 5th through 14th are omitted)
  private static final int[] DAYS_IN_MONTH_1582 = 
      {31, 28, 31, 30, 31, 30, 31, 31, 30, 21 /* Oct. is missing 10 days */, 30, 31};

  private static final int[] MONTH_OFFSETS_IN_DAYS = 
      EraCalc.calcMonthOffsetsInDays(DAYS_IN_MONTH_1582);

  private static final double TS_1582_JAN_01 = EraCalc.getTimestampForYear(1582);
  
  private static final double TS_1583_JAN_01 = getTimestampForYear(1583);

  public JulianCrossoverEraCalc() {
    this.monthOffsetsInMs = calcMonthOffsetsInMs(DAYS_IN_MONTH_1582);
    this.monthOffsetsInMsLeapYear = calcMonthOffsetsInMs(DAYS_IN_MONTH_1582);
    
    this.monthOffsetsInDays = calcMonthOffsetsInDays(DAYS_IN_MONTH_1582);
    this.monthOffsetsInDaysLeapYear = calcMonthOffsetsInDays(DAYS_IN_MONTH_1582);
  }
  
  @Override
  public double calcYearField(double timeInMs, DateFields dateFields) {
    checkTimestampNotLessThan(timeInMs, TS_1582_JAN_01);
    checkTimestampLessThan(timeInMs, TS_1583_JAN_01);
    
    dateFields.year = 1582;
    return timeInMs - TS_1582_JAN_01;
  }

  @Override
  public DayOfWeek calcDayOfWeek(int year, int month, int day) {
    if (year != 1582) {
      throw new IllegalArgumentException("This EraCalc only deals with the year 1582: " + year);
    }
    
    // 1582-Jan-01 was a Monday:
    final int yearOffsetInDays = MONTH_OFFSETS_IN_DAYS[month] + day - 1;
    return DAYS_OF_WEEK[(yearOffsetInDays + 1) % 7];
  }

  @Override
  public double calcYearTimestamp(int year) {
    return TS_1582_JAN_01;
  }
  
  @Override
  public int getDaysInMonth(int month, boolean isLeapYear) {
    return DAYS_IN_MONTH_1582[month];
  }

  @Override
  public int getMaxYear() {
    return 1582;
  }

  @Override
  public boolean isLeapYear(int year) {
    return false;
  }
  
  @Override
  public double[] getMonthOffsetsInMs(boolean isLeapYear) {
    if (this.monthOffsetsInMs == null) {
      this.monthOffsetsInMs = calcMonthOffsetsInMs(DAYS_IN_MONTH_1582);
    }
    return this.monthOffsetsInMs;
  }

}
