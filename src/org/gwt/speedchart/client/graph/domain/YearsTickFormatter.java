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

public class YearsTickFormatter extends DateTickFormatter {

  public YearsTickFormatter() {
    super("XXXX");
    this.superFormatter = null;
    this.subFormatter = new MonthsTickFormatter(this);
    // TODO: Really need to define a millennium formatter, and so on... 
    this.possibleTickSteps = 
      new int[] {1, 5, 10, 20, 25, 50, 100, 250, 500, 1000, 
                 2500, 5000, 10000, 25000, 50000, 100000, 500000,
                 1000000, 5000000, 10000000};
    this.timeUnitTickInterval = TimeUnit.YEAR;
  }

  public YearsTickFormatter(MilleniumTickFormatter milleniumTickFormatter) {
    this();
    this.superFormatter = milleniumTickFormatter;
  }

  public String formatTick() {
    return String.valueOf(currTick.getYear());
  }

  public int getSubTickStep(int primaryTickStep) {
    int x = primaryTickStep;
    if (x == 1) {
      return 4;
    }
    else if (x < 10) {
      return x;
    }
    else if (x == 10) {
      return 2;
    }
    else if (x == 20) {
      return 8;
    }
    else if (x == 25) {
      return 1;
    }
    else if (x == 50) {
      return 5;
    }
    else if (x == 100) {
      return 4;
    }
    else {
      // Catch-all: remaining tick steps will be (they better be) multiples of 2. 
      return 2;
    }
  }
  
}
