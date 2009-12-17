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

package org.gwt.speedchart.client.data;

import org.gwt.speedchart.client.data.tuple.Tuple5D;
import org.gwt.speedchart.client.util.Array1D;

/**
 * @author chad takahashi
 */
public final class FlyweightTuple implements Tuple5D {
  private int dataPointIndex = 0;
  private double[] domainData;
  private double[][] rangeTupleData;
  private int tupleLength;
  
  public FlyweightTuple(Array1D domain, Array1D[] rangeTuples) {
    this.tupleLength = 1 + rangeTuples.length;
    rangeTupleData = new double[rangeTuples.length][];
    setDomainAndRange(domain, rangeTuples);
  }
  
  public void setDomainAndRange(Array1D domain, Array1D[] rangeTuples) {
    domainData = domain.backingArray();
    for (int i = 0; i < this.rangeTupleData.length; i++) {
      rangeTupleData[i] = rangeTuples[i].backingArray();
    }
  }
  
  public double getRange(int rangeTupleIndex) {
    return rangeTupleData[rangeTupleIndex][this.dataPointIndex];
  }

  public int size() {
    return tupleLength;
  }

  public double getDomain() {
    return domainData[this.dataPointIndex];
  }

  public double getRange0() {
    return rangeTupleData[0][this.dataPointIndex];
  }

  public double getRange1() {
    return rangeTupleData[1][this.dataPointIndex];
  }

  public double getRange2() {
    return rangeTupleData[2][this.dataPointIndex];
  }

  public double getRange3() {
    return rangeTupleData[3][this.dataPointIndex];
  }
  
  public void setDataPointIndex(int dataPointIndex) {
    this.dataPointIndex = dataPointIndex;
  }
}
