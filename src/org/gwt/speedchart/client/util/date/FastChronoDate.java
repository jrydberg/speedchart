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

import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.TimeUnit;

/**
 * Date class that implements its own date and time calculations, and therefore 
 * does not rely on the Java (in hosted mode) or Javascript (in browser mode)
 * Date class.
 *  
 * @author chad takahashi
 */
public class FastChronoDate extends ChronoDate {
  
  static final DayOfWeek[] DAYS_OF_WEEK = DayOfWeek.values();

  static final double MS_IN_SECOND = 1000;
  static final double MS_IN_MINUTE = MS_IN_SECOND * 60;
  static final double MS_IN_HOUR = MS_IN_MINUTE * 60;
  static final double MS_IN_DAY = MS_IN_HOUR * 24;
  static final double MS_IN_YEAR = MS_IN_DAY * 365;
  static final double MS_IN_LEAPYEAR = MS_IN_DAY * 366;
  
  private static final double[] DAY_OFFSETS_IN_MS = calcOffsetArrayInMs(31, MS_IN_DAY);
  private static final double[] HOUR_OFFSETS_IN_MS = calcOffsetArrayInMs(60, MS_IN_HOUR);
  private static final double[] MINUTE_OFFSETS_IN_MS = calcOffsetArrayInMs(60, MS_IN_MINUTE);
  private static final double[] SECOND_OFFSETS_IN_MS = calcOffsetArrayInMs(60, MS_IN_SECOND);
  
  // Stores the constituent time unit values.
  private DateFields dateFields = new DateFields();
  
  // Provides "method chaining style" setting of constituent date components
  private DateFieldSetter dateFieldSetter = new DateFieldSetter(this);
  
  // Requests for era-specific calculations are delegated to this object.
  private EraCalc eraCalc;
  
  // 'java.util.Calendar.timeInMillis()' representation of this date.
  private double timeInMs;
  
  // True only if this.timeInMs is not sync'd with externally-assigned this.dateFields.
  private boolean isTimestampDirty = true;
  
  /**
   * Constructs a new date object initialized to the specified timestamp.
   */
  public FastChronoDate(double timestamp) {
    this.setTime(timestamp);
  }
  
  /**
   * Constructs a new date object initialized to 0 hours, 0 minutes, 0 seconds,
   * and 0 ms for the specified year, month and day.
   */
  public FastChronoDate(int year, int month, int day) {
    this.dateFields.clear().setYear(year).setMonth(month).setDay(day);
    this.eraCalc = EraCalc.getByYear(year);
    checkDateFields(this.dateFields);
    this.isTimestampDirty = true;
  }
  
  @Override
  public void add(TimeUnit timeUnit, int numUnits) {
    ArgChecker.isNonNegative(numUnits, "numUnits");
    if (numUnits == 0) {
      return;
    }
    
    addRecursive(timeUnit, numUnits);
    this.isTimestampDirty = true;
  }
  
  /**
   * Recursive routine called by the public {@link #add(TimeUnit, int)} method.
   */
  private void addRecursive(TimeUnit timeUnit, int numUnits) {
    switch (timeUnit) {
      case MILLENIUM:
        addYears(numUnits* 1000);
        break;
      case CENTURY:
        addYears(numUnits* 100);
        break;
      case DECADE:
        addYears(numUnits* 10);
        break;
      case YEAR:
        addYears(numUnits);
        break;
      case MONTH:
        dateFields.month += numUnits;
        if (dateFields.month > 11) {
          int numYears = dateFields.month / 12;
          dateFields.month = dateFields.month % 12;
          addRecursive(TimeUnit.YEAR, numYears);
        }
        break;
      case WEEK:
        // Just multiply by 7 and let fall through to the DAY case
        numUnits *= 7;
      case DAY:
        boolean isLeapYear = this.eraCalc.isLeapYear(dateFields.year);
        int daysInMonth = this.eraCalc.getDaysInMonth(dateFields.month, isLeapYear);
        int newDay = dateFields.day + numUnits;
        if (newDay <= daysInMonth) {
          dateFields.day = newDay;
        }
        else {
          int numDaysToNextMonth = daysInMonth - dateFields.day + 1;
          dateFields.day = 1;
          addRecursive(TimeUnit.MONTH, 1);
          addRecursive(TimeUnit.DAY, (numUnits - numDaysToNextMonth));
        }        
        break;
      case HOUR:
        dateFields.hour += numUnits;
        if (dateFields.hour > 23) {
          int numDays = dateFields.hour / 24;
          dateFields.hour = dateFields.hour % 24;
          addRecursive(TimeUnit.DAY, numDays);
        }
        break;
      case MIN:
        dateFields.minute += numUnits;
        if (dateFields.minute > 59) {
          int numHours = dateFields.minute / 60;
          dateFields.minute = dateFields.minute % 60;
          addRecursive(TimeUnit.HOUR, numHours);
        }
        break;
      case SEC:
        dateFields.second += numUnits;
        if (dateFields.second > 59) {
          int numMinutes = dateFields.second / 60;
          dateFields.second = dateFields.second % 60;
          addRecursive(TimeUnit.MIN, numMinutes);
        }
        break;
      case TENTH_SEC:
        dateFields.ms += (numUnits * 100);
        if (dateFields.ms > 999) {
          int numSeconds = dateFields.ms / 1000;
          dateFields.ms = dateFields.ms % 1000;
          addRecursive(TimeUnit.SEC, numSeconds);
        }
        break;
      case MS:
        dateFields.ms += numUnits;
        if (dateFields.ms > 999) {
          int numSeconds = dateFields.ms / 1000;
          dateFields.ms = dateFields.ms % 1000;
          addRecursive(TimeUnit.SEC, numSeconds);
        }
        break;
      default:
        throw new UnsupportedOperationException("TimeUnit " + timeUnit + " not supported");
    }
  }
  
  private void addYears(int numYears) {
    dateFields.year += numYears;
    EraCalc newEraCalc = EraCalc.getByYear(dateFields.year);
    if (newEraCalc != this.eraCalc) {
      //log("TESTING: switching to new EraCalc " + newEraCalc);
      this.eraCalc = newEraCalc;
    }
  }
  
  @Override
  public int getDaysInMonth() {
    boolean isLeapYear = eraCalc.isLeapYear(dateFields.year);
    return this.eraCalc.getDaysInMonth(dateFields.month, isLeapYear);
  }
  
  @Override
  public DayOfWeek getDayOfWeek() {
    return this.eraCalc.calcDayOfWeek(dateFields.year, dateFields.month, dateFields.day);
  }
  
  @Override
  public int getDay() {
    return dateFields.day;
  }

  @Override
  public int getHour() {
    return dateFields.hour;
  }

  @Override
  public int getMinute() {
    return dateFields.minute;
  }

  @Override
  public int getMonth() {
    return dateFields.month;
  }

  @Override
  public int getSecond() {
    return dateFields.second;
  }

  @Override
  public double getTime() {
    if (this.isTimestampDirty) {
      updateTimeStamp();
    }
    return this.timeInMs;
  }

  @Override
  public int getYear() {
    return dateFields.year;
  }

  @Override
  public DateFieldSetter set() {
    dateFields.copyTo(dateFieldSetter.dateFields);
    return this.dateFieldSetter;
  }
  
  @Override
  public void set(TimeUnit timeUnit, int value) {
    set().timeUnit(timeUnit, value).done();
  }
  
  /**
   * To be called exclusively by {@link DateFieldSetter#done()}.
   */
  void commitDateFieldChanges() {
    // Make sure we're using the proper EraCalc for the given
    // (possibly modified) year.
    this.eraCalc = EraCalc.getByYear(this.dateFields.year);
    
    // Verify that a valid date is implied by the modified temporary date fields 
    checkDateFields(dateFieldSetter.dateFields);
    
    // No exception thrown?  Ok, it's a good date, so transfer the temporary
    // fields back to this.dateFields.
    dateFieldSetter.dateFields.copyTo(this.dateFields);
    
    this.isTimestampDirty = true;
  }
  
  /**
   * Calculates and assigns the month, day, hour, minute, second, ms fields based 
   * on the input parameters.
   * 
   * @param yearOffset - The number of milliseconds from Jan-01 00:00:00.0
   * @param isLeapYear - true only if this is a leap year
   * @param dateFields - the object whose fields will be assigned by this method
   */
  private final void setDateFieldsStartingFromMonth(double yearOffset, 
      boolean isLeapYear, DateFields dateFields) {
    
    // Calculate month
    int month = -1;
    double monthOffset = 0;
    double[] monthOffsets = this.eraCalc.getMonthOffsetsInMs(isLeapYear);
    for (int i = 0; i < monthOffsets.length - 1; i++) {
      if (yearOffset < monthOffsets[i + 1]) {
        monthOffset = yearOffset - monthOffsets[i];
        month = i;
        break;
      }
    }

    // Calculate day
    final int zeroBasedDay = (int)(monthOffset / MS_IN_DAY);
    final double dayOffset = monthOffset - DAY_OFFSETS_IN_MS[zeroBasedDay];
    
    // Calculate hour
    final int hour = (int)(dayOffset / MS_IN_HOUR);
    final double hourOffset = dayOffset - HOUR_OFFSETS_IN_MS[hour];
    
    // Calculate minute
    final int minute = (int)(hourOffset / MS_IN_MINUTE);
    final double minuteOffset = hourOffset - MINUTE_OFFSETS_IN_MS[minute];

    // Calculate second
    final int second = (int)(minuteOffset / MS_IN_SECOND);
    final double secondOffset = minuteOffset - SECOND_OFFSETS_IN_MS[second];

    dateFields.month = month;
    dateFields.day = zeroBasedDay + 1;
    dateFields.hour = hour;
    dateFields.minute = minute;
    dateFields.second = second;
    dateFields.ms = (int)secondOffset;
  }
  
  @Override
  public void setTime(double timeInMs) {
    this.eraCalc = EraCalc.getByTimestamp(timeInMs);
    
    double yearOffset = this.eraCalc.calcYearField(timeInMs, this.dateFields);
    this.timeInMs = timeInMs;

    boolean isLeapYear = this.eraCalc.isLeapYear(this.dateFields.year);
    setDateFieldsStartingFromMonth(yearOffset, isLeapYear, this.dateFields);
    this.isTimestampDirty = false;
  }
  
  @Override
  public ChronoDate truncate(TimeUnit truncatePoint) {
    if (truncatePoint == TimeUnit.WEEK) {
      // WEEK truncation requires special handling...
      final DayOfWeek firstDayOfWeek = DAYS_OF_WEEK[0];
      final DayOfWeek dow = getDayOfWeek();
      if (dow != firstDayOfWeek) {
        int targetDay = this.dateFields.day - dow.ordinal();
        //System.out.println("TESTING: dow=" + dow + "; dow.ordinal=" + dow.ordinal() + "; targetDay=" + targetDay);
        if (targetDay < 1) {
          decrementMonth(dateFields);
          int daysInMonth =
            this.eraCalc.getDaysInMonth(dateFields.month, eraCalc.isLeapYear(dateFields.year));
          targetDay = daysInMonth + targetDay;
          //System.out.println("TESTING: daysInMonth=" + daysInMonth + "; new targetDay=" + targetDay);
        }
        dateFields.day = targetDay;
      }
      dateFields.clearStartingAfter(TimeUnit.DAY);
    }
    else {
      dateFields.clearStartingAfter(truncatePoint);
    }
    
    isTimestampDirty = true;
    return this;
  }

  public String toString() {
    return getDayOfWeek() + " " + dateFields.toString();
  }
  
  private void checkDateFields(DateFields dateFields) {
    // FIXME:
    //checkTimeUnitRange(TimeUnit.YEAR, this.year, this.minYear, this.maxYear);
    
    checkTimeUnitRange(TimeUnit.MONTH, dateFields.month, 0, 11);

    final int numDaysInMonth = 
        eraCalc.getDaysInMonth(dateFields.month, eraCalc.isLeapYear(dateFields.year));
    
    checkTimeUnitRange(TimeUnit.DAY, dateFields.day, 1, numDaysInMonth);
    checkTimeUnitRange(TimeUnit.HOUR, dateFields.hour, 0, 23);
    checkTimeUnitRange(TimeUnit.MIN, dateFields.minute, 0, 59);
    checkTimeUnitRange(TimeUnit.SEC, dateFields.second, 0, 59);
    checkTimeUnitRange(TimeUnit.MS, dateFields.ms, 0, 999);
  }
  
  private void checkTimeUnitRange(TimeUnit timeUnit, int value, int min, int max) {
    if (value < min && value != Integer.MIN_VALUE) {
      String msg = timeUnit + "=" + value + ", but minimum allowable value is " + min 
        + " (state = '" + this + "')";
      throw new IllegalArgumentException(msg);
    }
    else if (value > max && value != Integer.MAX_VALUE) {
      String msg = timeUnit + "=" + value + ", but maximum allowable value is " + max;
    throw new IllegalArgumentException(msg);
    }
  }

  /**
   * Updates the {@link #timeInMs} field based on the current state of {@link #dateFields}.
   */
  private void updateTimeStamp() {
    boolean isLeapYear = this.eraCalc.isLeapYear(this.dateFields.year);
    double[] monthOffsets = this.eraCalc.getMonthOffsetsInMs(isLeapYear);
    double ts = this.eraCalc.calcYearTimestamp(this.dateFields.year);

    ts += monthOffsets[dateFields.month];
    ts += DAY_OFFSETS_IN_MS[dateFields.day - 1]; // days are 1-based
    ts += HOUR_OFFSETS_IN_MS[dateFields.hour];
    ts += MINUTE_OFFSETS_IN_MS[dateFields.minute];
    ts += SECOND_OFFSETS_IN_MS[dateFields.second];
    ts += dateFields.ms;

    this.isTimestampDirty = false;
    this.timeInMs = ts;
  }

  /**
   * Creates an array of timestamp offset intervals for a given time unit. For
   * example, if numOffsets=4 and offsetInterval=10, then the resulting array
   * would be [0.0, 10.0, 20.0, 30.0].
   */
  private static double[] calcOffsetArrayInMs(int numOffsets, double offsetInterval) {
    double[] offsets = new double[numOffsets];
    offsets[0] = 0.0;
    for (int i = 0; i < offsets.length - 1; i++) {
      offsets[i + 1] = offsets[i] + offsetInterval;
    }
    return offsets;
  }
 
  private static void decrementMonth(DateFields df) {
    --df.month;
    if (df.month < 0) {
      df.month = 11;
      --df.year;
    }
  }
  
  private static final void log(Object msg) {
    System.out.println("FastChronoDate> " + msg);
  }
}
