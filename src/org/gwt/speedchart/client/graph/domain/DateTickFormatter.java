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

package org.gwt.speedchart.client.graph.domain;

import org.gwt.speedchart.client.util.MathUtil;
import org.gwt.speedchart.client.util.TimeUnit;
import org.gwt.speedchart.client.util.date.DateFormatHelper;

import java.util.Date;

/**
 * Provides functionality for rendering the date/time ticks in a context-sensitive
 * way depending on the current domain interval.
 * 
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 * @author Chad Takahashi &lt;chad@timepedia.org&gt;
 */
public abstract class DateTickFormatter extends TickFormatter {
  
  protected DateFormatHelper dateFormat = new DateFormatHelper();
  
  protected Date currTick = new Date();
  
  /**
   * Subclasses assign this field upon construction.
   * 
   * @see #getTickInterval()
   */
  protected TimeUnit timeUnitTickInterval;
    
  /**
   * Constructs a new formatter.
   *  
   * @param longestPossibleLabel Represents the the longest possible label that
   * could occur, given the set of all labels for this formatter.  For example,
   * if this formatter formatted days of the week, then "Saturday" should be used,
   * since it is the longest name of the 7 days.
   */
  public DateTickFormatter(String longestPossibleLabel) {
    super(longestPossibleLabel);
  }
  
  @Override
  public double getTickDomainValue() {
    return currTick.getTime();
  }

  /**
   * Returns a positive value corresponding to a single tick for this formatter.
   * For example, if this is a day-of-month formatter, then this method would
   * return {@link TimeUnit#DAY#ms()}.
   */
  public final double getTickInterval() {
    return timeUnitTickInterval.ms();
  }

  /**
   * Increments <tt>date</tt> by the specified number of time units (where a 
   * time unit can be an hour, day, second, etc.).  Subclasses may sometimes 
   * need to override this method to modify the actual number of time units in
   * order to ensure that the associated tick labels are stable when scrolling.
   * 
   * @return the number of time units that were *actually* incremented; typically,
   * this value will be the same as the <tt>numTimeUnits</tt> input parameter, but
   * in the aforementioned subclass override case, a different value could 
   * get returned (the typical case for this is a date near the end of a month).
   */
  public int incrementTick(int numTimeUnits) {
    currTick.setTime(currTick.getTime()
        + (long) (timeUnitTickInterval.ms() * numTimeUnits));
    return numTimeUnits;
  }

  protected int getTimeUnit(Date d, TimeUnit timeUnit) {
    switch (timeUnit) {
    case CENTURY:
      return d.getYear() / 100;
    case DECADE:
      return d.getYear() / 10;
    case YEAR:
      return d.getYear();
    case MONTH:
      return d.getMonth();
    case DAY:
      return d.getDay();
    case HOUR:
      return d.getHours();
    case MIN:
      return d.getMinutes();
    case SEC:
      return d.getSeconds();
    case MILLENIUM: // define this near/at the bottom, as it's used less frequently
      return (d.getYear() / 1000);
    default:
      throw new UnsupportedOperationException(
	"TimeUnit " + timeUnit + " not supported at this time");
    }
  }

  protected void setTimeUnit(Date d, TimeUnit timeUnit, int value) {
    switch (timeUnit) {
    case MONTH:
      d.setMonth(value);
      break;
    case HOUR:
      d.setHours(value);
      break;
    case DAY:
      //d.setDay(value);
      break;
    case MIN:
      d.setMinutes(value);
      break;
    case SEC:
      d.setSeconds(value);
      break;
    }

  }

  /**
   * Quantizes the specified timeStamp down to the specified tickStep.  For example, 
   * suppose this is a MonthTickFormatter, 
   * <tt>timeStamp = JUN-19-1985:22hrs:36min...</tt>, and 
   * <tt>tickStep = 3</tt> (in this context, '3' refers to 3 months).  This method
   * will return <tt>APR-1-1985:0hrs:0min, ...</tt>.
   * 
   * @param timeStamp -The point in time, specified in milliseconds, to be quantized
   * @param tickStep - The tick step to which the timeStamp will be quantized
   * @return the quantized date
   */
  public void resetToQuantizedTick(double timeStamp, int tickStep) {
    currTick.setTime((long) (timeStamp - (timeStamp % this.timeUnitTickInterval.ms())));
    int normalizedValue =
      MathUtil.quantize(getTimeUnit(currTick, this.timeUnitTickInterval), tickStep);
    setTimeUnit(currTick, timeUnitTickInterval, normalizedValue);
  }
  
  public void setTick(double timestamp) {
    currTick.setTime((long) timestamp);
  }
}
