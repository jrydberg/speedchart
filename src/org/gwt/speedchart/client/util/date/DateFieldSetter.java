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

/**
 * Facilitates setting of date fields via method chaining.
 * 
 * @see {@link ChronoDate#set()}.
 * 
 * @author chad takahashi
 */
public class DateFieldSetter {
  DateFields dateFields = new DateFields();
  private FastChronoDate parent;
  
  DateFieldSetter(FastChronoDate parent) {
    this.parent = parent;
  }
  
  public DateFieldSetter year(int year) {
    dateFields.year = year;
    return this;
  }
  
  public DateFieldSetter month(int month) {
    dateFields.month = month;
    return this;
  }
  
  public DateFieldSetter day(int day) {
    dateFields.day = day;
    return this;
  }
  
  public DateFieldSetter hour(int hour) {
    dateFields.hour = hour;
    return this;
  }
  
  public DateFieldSetter min(int minute) {
    dateFields.minute = minute;
    return this;
  }
  public DateFieldSetter sec(int second) {
    dateFields.second = second;
    return this;
  }
  public DateFieldSetter ms(int ms) {
    dateFields.ms = ms;
    return this;
  }
  
  public DateFieldSetter timeUnit(TimeUnit timeUnit, int value) {
    switch (timeUnit) {
      case YEAR:
        year(value); break;
      case MONTH:
        month(value); break;
      case DAY: 
        day(value); break;
      case HOUR:
        hour(value); break;
      case MIN:
        min(value); break;
      case SEC:
        sec(value); break;
      case MS:
        ms(value); break;
      case MILLENIUM:
        if (value >= 0) {
          year((value * 1000) + Math.abs(dateFields.year % 1000));
        }
        else {
          year((value * 1000) + -Math.abs(dateFields.year % 1000));
        }
        break;
      default:
        throw new UnsupportedOperationException("TimeUnit '" + timeUnit + " not supported");
    }
    return this;
  }

  public ChronoDate done() {
    parent.commitDateFieldChanges();
    return parent;
  }
  
}
