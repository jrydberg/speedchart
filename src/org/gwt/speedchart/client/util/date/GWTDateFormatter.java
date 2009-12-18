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

import com.google.gwt.i18n.client.DateTimeFormat;

import org.gwt.speedchart.client.util.DateFormatter;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: ray Date: Oct 24, 2008 Time: 12:41:00 AM To
* change this template use File | Settings | File Templates.
*/
public class GWTDateFormatter implements DateFormatter {

  private DateTimeFormat fmt;

  public GWTDateFormatter(String format) {
    fmt = DateTimeFormat.getFormat(format);
  }

  public String format(double timestamp) {
    return fmt.format(new Date((long) timestamp));
  }

  public double parse(String date) {
    return fmt.parse(date).getTime();
  }
}
