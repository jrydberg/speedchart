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
import org.gwt.speedchart.client.util.Array1D;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.util.Util;

/**
 * Provides skeletal implementation of an {@link Dataset} to simplify
 * implementation of a concrete class.
 *
 * @author Chad Takahashi
 */
public abstract class AbstractDataset<T extends Tuple2D> implements Dataset<T> {

  protected double minDomainInterval;

  protected Interval preferredRangeAxisInterval;

  private Interval domainExtrema = new Interval(0.0, 0.0);

  protected AbstractDataset(Interval preferredRangeAxisInterval) {
    this.preferredRangeAxisInterval = preferredRangeAxisInterval;
  }

  public final Interval getDomainExtrema() {
    this.domainExtrema.setEndpoints(getX(0), getX(getNumSamples() - 1));
    return this.domainExtrema;
  }

  public final double getMinDomainInterval() {
    return minDomainInterval;
  }

  public MipMapRegion getBestMipMapForInterval(Interval region, int maxSamples) {
    int domainStartIdx = 0;
    int domainEndIdx = 0;
    MipMapChain mipMapChain = getMipMapChain();
    MipMap bestMipMap = mipMapChain.getMipMap(0);
    while (true) {
      Array1D domain = bestMipMap.getDomain();
      domainStartIdx = Util.binarySearch(domain, region.getStart());
      domainEndIdx = Util.binarySearch(domain, region.getEnd());
      if ((domainEndIdx - domainStartIdx) <= maxSamples) {
        break;
      }
      bestMipMap = bestMipMap.next();
    }
    return new MipMapRegion(bestMipMap, domainStartIdx, domainEndIdx);
  }

  public final Interval getPreferredRangeAxisInterval() {
    return preferredRangeAxisInterval;
  }

}
