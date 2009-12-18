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

import org.gwt.speedchart.client.util.DateFormatter;

/**
*/
public abstract class DateFormatterFactory {

  static DateFormatterFactory dateFormatterFactory = 
      new DateFormatterFactory() {
	public DateFormatter getDateFormatter(String format) {
	  return new GWTDateFormatter(format);
	}
      };

  public abstract DateFormatter getDateFormatter(String format);

  public static void setDateFormatterFactory(
      DateFormatterFactory dff) {
    dateFormatterFactory = dff;
  }

  public static DateFormatterFactory getInstance() {
    return dateFormatterFactory;
  }
}
