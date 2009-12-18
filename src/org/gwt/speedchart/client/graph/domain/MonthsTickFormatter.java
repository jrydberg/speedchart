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

public class MonthsTickFormatter extends DateTickFormatter {

  public MonthsTickFormatter(DateTickFormatter superTickFormatter) {
    super("XXX'XX");
    this.superFormatter = superTickFormatter;
    this.subFormatter = new DaysTickFormatter(this);
    this.possibleTickSteps = new int[] {1, 3, 6};
    this.timeUnitTickInterval = TimeUnit.MONTH;
  }

  public String formatTick() {
    return dateFormat.monthAndYear(currTick);
  }

  public int getSubTickStep(int primaryTickStep) {
    if (primaryTickStep == 1) {
      // Place a subtick between each month
      return 2;
    } else {
      // otherwise, place a subtick at 1 month intervals between
      // the labeled ticks.
      return primaryTickStep;
    }
  }

}
