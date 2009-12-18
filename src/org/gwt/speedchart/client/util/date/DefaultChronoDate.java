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

import org.gwt.speedchart.client.util.TimeUnit;

import java.util.Date;

/**
 * {@link ChronoDate} backed by a <tt>java.util.Date</tt> object.
 * 
 * @author chad takahashi
 */
public final class DefaultChronoDate extends ChronoDate {
  private Date d;
  
  public DefaultChronoDate(double timeStamp) {
    this.d = new Date((long)timeStamp);
  }

  @Override
  public void add(TimeUnit timeUnit, int numUnits) {
    switch (timeUnit) {
      case YEAR:
        d.setYear(d.getYear() + numUnits);
        break;
      case MONTH:
        d.setMonth(d.getMonth() + numUnits);
        break;
      case WEEK:
        d.setDate(d.getDate() + numUnits * 7);
        break;
      case DAY:
        d.setDate(d.getDate() + numUnits);
        break;
      case HOUR:
        d.setHours(d.getHours() + numUnits);
        break;
      case MIN:
        d.setMinutes(d.getMinutes() + numUnits);
        break;
      case SEC:
        d.setSeconds(d.getSeconds() + numUnits);
        break;
      case TENTH_SEC:
        d.setTime(d.getTime() + (long)(numUnits * 100));
        break;
      default:
        throw new UnsupportedOperationException("TimeUnit " + timeUnit + " not supported at this time");
    }
  }

  @Override
  public int getDaysInMonth() {
    int year = d.getYear() + 1900;
    int month = d.getMonth();
    
    // Special case: Oct 1582 only has 21 days (Oct 5th - 14th were omitted due to 
    // transition from Julian to Gregorian calendar).
    if (year == 1582 && month == 9) {
      return 21;
    }
    
    Date tmp = (Date)d.clone();
    tmp.setMonth(month + 1);
    tmp.setDate(1);
    tmp.setDate(0);
    return tmp.getDate();
  }
  
  @Override
  public DayOfWeek getDayOfWeek() {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public int getDay() {
    return d.getDate();
  }
  
  @Override
  public int getHour() {
    return d.getHours();
  }
  
  @Override
  public int getMinute() {
    return d.getMinutes();
  }
  
  @Override
  public int getMonth() {
    return d.getMonth();
  }

  @Override
  public int getSecond() {
    return d.getSeconds();
  }

  @Override
  public double getTime() {
    return (double)d.getTime();
  }

  @Override
  public int getYear() {
    return d.getYear() + 1900;
  }
  
  public boolean isFirstOfMonth() {
    return d.getDate() == 1;
  }

  @Override
  public ChronoDate truncate(TimeUnit timeUnit) {
    Date maskedDate;
    switch (timeUnit) {
      case YEAR:
        maskedDate = new Date(d.getYear(), 0, 1);
        break;
      case MONTH:
        maskedDate = new Date(d.getYear(), d.getMonth(), 1);
        break;
      case WEEK:
        FastChronoDate fd=new FastChronoDate(d.getTime());
        fd.truncate(TimeUnit.WEEK);
        maskedDate = new Date((long)fd.getTime());
        break;
      case DAY:
        maskedDate = new Date(d.getYear(), d.getMonth(), d.getDate());
        break;
      case HOUR:
        maskedDate = new Date(d.getYear(), d.getMonth(), d.getDate(), d.getHours(), 0, 0);
        break;
      case MIN:
        maskedDate = new Date(d.getYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes(), 0);
        break;
      case SEC:
        maskedDate = new Date(d.getYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds());
        break;
      default:
        throw new IllegalArgumentException("Unsupported time unit: " + timeUnit);
    }
    
    this.d = maskedDate;
    return this;
  }

  @Override
  public DateFieldSetter set() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void set(TimeUnit timeUnit, int value) {
    switch (timeUnit) {
      case YEAR:
        d.setYear(value - 1900); 
        break;
      case MONTH:
        d.setMonth(value); 
        break;
      case WEEK:
        FastChronoDate fd=new FastChronoDate(d.getTime());
        fd.set(timeUnit, value);
        d.setTime((long) fd.getTime());
        break;
      case DAY:
        d.setDate(value);
        break;
      case HOUR:
        d.setHours(value);
        break;
      case MIN:
        d.setMinutes(value);
        break;
      case SEC:
        d.setSeconds(value);
        break;
      default:
        throw new UnsupportedOperationException("Unsupported time unit: " + timeUnit);
    }
  }

  @Override
  public void setTime(double ms) {
    d.setTime((long)ms);
  }
  
  @Override
  public String toString() {
    return this.d.toString();
  }

}
