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

import java.util.Date;

/**
 * A specialized date class for use with the Chronoscope framework.
 *
 * @author chad takahashi
 */
public abstract class ChronoDate {

  /**
   * Factory method that creates a new date object for the specified timeStamp.
   */
  public static final ChronoDate get(double timeStamp) {
//    return new DefaultChronoDate(timeStamp);
    return new FastChronoDate(timeStamp);
  }

  public static final ChronoDate get(int year, int month, int day) {
    return new FastChronoDate(year, month, day);
  }

  /**
   * Returns a date representing the current time/date.
   */
  public static final ChronoDate getSystemDate() {
    return get(new Date().getTime());
  }

  /**
   * Adds the specified number of time units to this date. Implementations of
   * this method are expected to handle "rollover" scenarios, where adding
   * <tt>numUnits</tt> to this date would cause the next largest time unit to
   * increment.
   */
  public abstract void add(TimeUnit timeUnit, int numUnits);

  /**
   * Returns a copy of this date.
   */
  public ChronoDate copy() {
    return ChronoDate.get(this.getTime());
  }

  /**
   * Copies the state of this date over to the target date.
   */
  public void copyTo(ChronoDate target) {
    ArgChecker.isNotNull(target, "target");
    // TODO: Subclasses can potentially speed up this calc if we check if 
    // 'target' is also an instance of this class and, if so, manually transfer 
    // over the fields.
    target.setTime(this.getTime());
  }

  /**
   * Returns the year, month, day, etc. portion of this date object.
   *
   * @param timeUnit - The portion of this date whose value is to be returned
   */
  public final int get(TimeUnit timeUnit) {
    switch (timeUnit) {
      case CENTURY:
        return getYear() / 100;
      case DECADE:
        return getYear() / 10;
      case YEAR:
        return getYear();
      case MONTH:
        return getMonth();
      case DAY:
        return getDay();
      case HOUR:
        return getHour();
      case MIN:
        return getMinute();
      case SEC:
        return getSecond();
      case MILLENIUM: // define this near/at the bottom, as it's used less frequently
        return (getYear() / 1000);
      default:
        throw new UnsupportedOperationException(
            "TimeUnit " + timeUnit + " not supported at this time");
    }
  }

  /**
   * Returns the number of days in this date's month.
   */
  public abstract int getDaysInMonth();

  /**
   * Returns the day of the week that this date falls on.
   */
  public abstract DayOfWeek getDayOfWeek();

  public abstract int getDay();

  public abstract int getHour();

  public abstract int getMinute();

  public abstract int getMonth();

  public abstract int getSecond();

  /**
   * Analagous to <tt>java.util.Date.getTime()</tt>.
   */
  public abstract double getTime();

  public abstract int getYear();

  /**
   * Sets the constituent components of this date via method chaining. For
   * example, to set the date to 'January 19th, 1956':
   * <blockquote><pre>
   * myDate.set().year(1956).month(0).day(19).done();
   * </pre></blockquote>
   *
   * Don't forget to call {@link DateFieldSetter#done()}! Otherwise the date
   * modifications will not take effect.
   */
  public abstract DateFieldSetter set();

  /**
   * Sets the specified date field to the specified value. <p> NOTE: If you need
   * to set more than 1 date field, use {@link #set()} instead.  This method
   * defers date field validation until the {@link DateFieldSetter#done()}
   * method is called.
   */
  public abstract void set(TimeUnit dateField, int value);

  /**
   * Analagous to <tt>java.util.Date.setTime()</tt>.
   */
  public abstract void setTime(double ms);

  /**
   * Truncates this date up to the specified time unit. For example, if
   * <tt>myDate = '1987-Mar-12 14:30:59'</tt>, then <tt>truncate(myDate,
   * TimeUnit.MONTH)</tt> will truncate this date up to the month resulting in
   * the date <tt>'1987-Mar-01 00:00:00'</tt>.
   *
   * @throws UnsupportedOperationException if the specified timeUnit is not
   *                                       supported by a particular subclass.
   */
  public abstract ChronoDate truncate(TimeUnit timeUnit);
}
