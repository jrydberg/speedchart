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

import org.gwt.speedchart.client.util.TimeUnit;

/**
 * @author chad takahashi
 */
public class HoursTickFormatter extends DateTickFormatter {

  public HoursTickFormatter(DateTickFormatter superFormatter) {
    super("00xx"); // e.g. "12pm"
    this.superFormatter = superFormatter;
    this.subFormatter = new MinutesTickFormatter(this);
    this.possibleTickSteps = new int[] {1, 3, 6, 12};
    this.timeUnitTickInterval = TimeUnit.HOUR;
  }

  public String formatTick() {
    int hourOfDay = currTick.getHours();
    switch (hourOfDay) {
      case 0:
        return dateFormat.dayAndMonth(currTick);
      default:
        return dateFormat.hour(hourOfDay);
    }
  }

  public int getSubTickStep(int primaryTickStep) {
    switch (primaryTickStep) {
      case 12:
      case 1:
        return 4;
      case 6:
        return 2;
      default:
        return super.getSubTickStep(primaryTickStep);
    }
  }

}
