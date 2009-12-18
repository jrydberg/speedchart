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

public class TenthsOfSecondTickFormatter extends DateTickFormatter {

  public TenthsOfSecondTickFormatter(DateTickFormatter superFormatter) {
    super("XX:XX:XX");
    this.superFormatter = superFormatter;
    this.subFormatter = null;
    this.possibleTickSteps = new int[] {1, 2, 5, 10};
    this.timeUnitTickInterval = TimeUnit.TENTH_SEC;
  }

  public String formatTick() {
    return dateFormat.tenthOfSecond(currTick);
  }

  @Override
  public void resetToQuantizedTick(double dO, int idealTickStep) {
    currTick.setTime(dO);
    currTick.truncate(TimeUnit.SEC);
  }

}
