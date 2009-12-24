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
import org.gwt.speedchart.client.MutableDataset;
import org.gwt.speedchart.client.data.Mutation.AppendMutation;
import org.gwt.speedchart.client.data.tuple.Tuple2D;
import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Dataset that permits certain types of mutations (e.g. appending new
 * data points, modifying Y-value of existing data points).
 * <p>
 * 
 * @see Mutation
 * 
 * @author Chad Takahashi
 */
public class MutableDataset2D extends ArrayDataset2D implements MutableDataset<Tuple2D> {
  private MipMapStrategy mipMapStrategy;
  private List<DatasetListener<Tuple2D>> listeners = new ArrayList<DatasetListener<Tuple2D>>();

  public MutableDataset2D(double[] domain, double[] range, MipMapStrategy mms) {
    super(domain, range, mms);
    mipMapStrategy = mms;
  }

  public MutableDataset2D(double[] domain, double[] range) {
    this(domain, range, BinaryMipMapStrategy.MEAN);
  }

  public void addListener(DatasetListener<Tuple2D> listener) {
    ArgChecker.isNotNull(listener, "listener");
    this.listeners.add(listener);
  }
  
  public void removeListener(DatasetListener<Tuple2D> listener) {
    listeners.remove(listener);
  }

  public void mutate(Mutation mutation) {
    ArgChecker.isNotNull(mutation, "mutation");

    double newX, newY;

    if (mutation instanceof Mutation.AppendMutation) {
      AppendMutation m = (Mutation.AppendMutation) mutation;
      newY = m.getY();
      newX = m.getX();
      double newInterval = newX - rawData.getDomain().getLast();
      appendXY(newX, newY);
    } 
    else if (mutation instanceof Mutation.RangeMutation) {
      Mutation.RangeMutation m = (Mutation.RangeMutation) mutation;
      newY = m.getY();
      mipMapStrategy.setRangeValue(m.getPointIndex(), newY,
          mipMapChain.getMipMappedRangeTuples()[0]);
      newX = rawData.getDomain().get(m.getPointIndex());
    } 
    else {
      // TODO: Can add more mutation handlers later
      throw new UnsupportedOperationException("mutation of type "
          + mutation.getClass().getName() + " currently not supported");
    }
    
    notifyListeners(this, newX, newX);
  }

  private void appendXY(double x, double y) {
    if (x <= getDomainExtrema().getEnd()) {
      throw new IllegalArgumentException(
          "Insertions not allowed; x was <= domainEnd: " + x + ":"
              + getDomainExtrema().getEnd());
    }
    
    mipMapStrategy.appendXY(x, y, mipMapChain);
  }

  private void notifyListeners(Dataset<Tuple2D> ds, double domainStart, double domainEnd) {
    for (DatasetListener<Tuple2D> l : this.listeners) {
      l.onDatasetChanged(ds, domainStart, domainEnd);
    }
  }
}
