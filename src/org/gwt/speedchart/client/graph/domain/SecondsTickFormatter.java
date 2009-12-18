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
import org.gwt.speedchart.client.util.date.ChronoDate;

public class SecondsTickFormatter extends DateTickFormatter {

  public SecondsTickFormatter(DateTickFormatter superFormatter) {
    super("XX:XX:XX");
    this.superFormatter = superFormatter;
    this.subFormatter = new TenthsOfSecondTickFormatter(this);
    this.possibleTickSteps = new int[] {1, 5, 15, 30, 60};
    this.timeUnitTickInterval = TimeUnit.SEC;
  }

  public String formatTick() {
    ChronoDate d = currTick;
    if (d.getHour() == 0 && d.getMinute() == 0 && d.getSecond() == 0) {
      return dateFormat.dayAndMonth(d);
    }
    else {
      return dateFormat.hourMinuteSecond(d);
    }
  }

  public int getSubTickStep(int primaryTickStep) {
    switch (primaryTickStep) {
      case 60:
        return 4;
      case 30:
        return 2;
      case 15:
        return 3;
      case 1:
        return 10;
      default:
        return super.getSubTickStep(primaryTickStep);
    }
  }

}
