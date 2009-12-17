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

package org.gwt.speedchart.client.util;

/**
 * Calculates the minimum and maximum values across all elements
 * in an {@link Array1D} object.
 * 
 * @author chad takahashi
 */
public class ExtremaArrayFunction implements ArrayFunction {
  private Interval extrema;
  
  public void exec(double[] data, int arrayLength) {
    if (arrayLength == 0) {
      extrema = null;
    }
    else {
      double min = Double.POSITIVE_INFINITY;
      double max = Double.NEGATIVE_INFINITY;
      
      for (int i = 0; i < arrayLength; i++) {
        double value = data[i];
        min = Math.min(min, value);
        max = Math.max(max, value);
      }
      
      extrema = new Interval(min, max);
    }
  }
  
  public Interval getExtrema() {
    return this.extrema;
  }
}
