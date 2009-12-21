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

package org.gwt.speedchart.client.graph.axis;

import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.util.DOMUtil;
import org.gwt.speedchart.client.util.MathUtil;


public class DefaultRangeTickFormatter implements RangeTickFormatter {

  private int maxLabelHeight = -1;

  private String longestPossibleLabel = "XXX XXX";

  private double[] unitValues = new double[] {1, 1000.0, 10000000.0,
      1000000000.0, 1000000000000.0, 1000000000000000.0 };
  private String[] unitNames = new String[]{"", "K", "M",  "G", "T", "P" };

  public int getMaxTickLabelHeight(String tickLabelClassName) {
    if (maxLabelHeight == -1) {
      maxLabelHeight = DOMUtil.get().stringHeight(longestPossibleLabel, 
          tickLabelClassName);
    }
    return maxLabelHeight;
  }

  public double[] calcTickPositions(Interval visRange, 
       int maxTicksForScreen) {

    final double range = visRange.length();
    final double roughInterval = range / maxTicksForScreen;

    final int logRange = ((int) Math.floor(Math.log10(roughInterval))) - 1;
    final double exponent = Math.pow(10, logRange);
    int smoothSigDigits = (int) (roughInterval / exponent);
    smoothSigDigits = smoothSigDigits + 5;
    smoothSigDigits = smoothSigDigits 
        - (int) MathUtil.mod(smoothSigDigits, 5.0);

    final double smoothInterval = smoothSigDigits * exponent;
    final double axisStart = visRange.getStart() 
        - MathUtil.mod(visRange.getStart(), smoothInterval);
    int numTicks = (int) (Math.ceil((visRange.getEnd() 
	- axisStart) / smoothInterval));

    if (axisStart + (smoothInterval * (numTicks - 1)) < visRange.getEnd()) {
      numTicks++;
    }

    double tickPositions[] = new double[numTicks];
    double tickValue = axisStart;
    for (int i = 0; i < tickPositions.length; i++) {
      tickPositions[i] = tickValue;
      tickValue += smoothInterval;
    }

    return tickPositions;
  }

  public String format(double dataY) {
    boolean isPositive = dataY >= 0.0;
    if (!isPositive) {
      dataY = -dataY;
    }

    int selectedUnit = 0;
    for (int i = 0; i < unitValues.length; i++) {
      if (dataY > unitValues[i]) {
	selectedUnit = i;
      }
    }

    dataY = dataY / unitValues[selectedUnit];
    final double normalized = round(dataY, 2);
    return (isPositive ? "" : "-") + Double.toString(normalized) 
        + unitNames[selectedUnit];
  }

  // positive value only.
  public static double round(double value, int decimalPlace) {
    double power_of_ten = 1;
    // floating point arithmetic can be very tricky.
    // that's why I introduce a "fudge factor"
    double fudge_factor = 0.05;
    while (decimalPlace-- > 0) {
      power_of_ten *= 10.0d;
      fudge_factor /= 10.0d;
    }
    return Math.round((value + fudge_factor) * power_of_ten) / power_of_ten;
  }

}
