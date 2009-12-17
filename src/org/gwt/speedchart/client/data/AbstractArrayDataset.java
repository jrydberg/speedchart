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

import org.gwt.speedchart.client.Dataset;
import org.gwt.speedchart.client.data.tuple.Tuple2D;
import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.Array1D;
import org.gwt.speedchart.client.util.Array2D;
import org.gwt.speedchart.client.util.ExtremaArrayFunction;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.util.MinIntervalArrayFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides most of the implementation necessary for an N-tuple dataset backed
 * by {@link Array2D} objects.
 *
 * @author Chad Takahashi
 */
public abstract class AbstractArrayDataset<T extends Tuple2D>
    extends AbstractDataset<T> {

  protected MipMapChain mipMapChain;

  /**
   * Stores the min/max range values for each tuple coordinate in {@link
   * #mmRangeTuple}.
   */
  protected Interval[] rangeIntervals;

  /**
   * Mip level 0 of the {@link #mipMapChain}.
   */
  private MipMap rawData;

  public AbstractArrayDataset(double[] domain, double[] range, MipMapStrategy mms, 
			      Interval preferredRangeAxisInterval) {
    super(preferredRangeAxisInterval);

    List<double[]> rangeTuples = new ArrayList<double[]>();
    rangeTuples.add(range);
    mipMapChain = mms.mipmap(domain, rangeTuples);
    rawData = mipMapChain.getMipMap(0);

    MinIntervalArrayFunction minIntervalFn = new MinIntervalArrayFunction();
    rawData.getDomain().execFunction(minIntervalFn);
    minDomainInterval = minIntervalFn.getMinInterval();

    rangeIntervals = new Interval[mipMapChain.getRangeTupleSize()];
    ExtremaArrayFunction extremaFn = new ExtremaArrayFunction();
    for (int i = 0; i < rangeIntervals.length; i++) {
      Array1D rangeVals = rawData.getRange(i);
      rangeVals.execFunction(extremaFn);
      rangeIntervals[i] = extremaFn.getExtrema();
    }
  }

  public final T getFlyweightTuple(int index) {
    return (T) rawData.getTuple(index);
  }

  public Interval getRangeExtrema(int tupleCoordinate) {
    return rangeIntervals[tupleCoordinate].copy();
  }

  public MipMapChain getMipMapChain() {
    return this.mipMapChain;
  }

  public int getNumSamples() {
    return this.rawData.size();
  }

  public int getTupleLength() {
    return 1 + this.mipMapChain.getRangeTupleSize();
  }

  public double getX(int index) {
    return this.rawData.getDomain().get(index);
  }

  protected MipMapChain createMipMapChain(Array2D mipMappedDomain,
      List<Array2D> mipMappedRangeTuples) {
    return new MipMapChain(mipMappedDomain, mipMappedRangeTuples);
  }
}
