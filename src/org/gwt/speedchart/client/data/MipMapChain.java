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
import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.Array2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An ordered set of {@link MipMap} objects, where each MipMap represents
 * a compressed version of the raw {@link Dataset}.
 *   
 * @author chad takahashi
 */
public class MipMapChain {
  private Map<String,MipMap> name2mipmap;
  private List<MipMap> mipMaps;
  private final int rangeTupleSize;
  
  private Array2D mipMappedDomain;
  private Array2D[] mipMappedRangeTuples;

  public MipMapChain(Array2D mipMappedDomain, List<Array2D> mipMappedRangeTuples) {
    this(mipMappedDomain, mipMappedRangeTuples, null);
  }

  public MipMapChain(Array2D mipMappedDomain, List<Array2D> mipMappedRangeTuples,
        List<String> mipLevelNames) {
    
    validate(mipMappedDomain, mipMappedRangeTuples);
    this.mipMappedDomain = mipMappedDomain;
    this.mipMappedRangeTuples = mipMappedRangeTuples.toArray(new Array2D[0]);
    this.rangeTupleSize = mipMappedRangeTuples.size();
    
    final int numMipLevels = mipMappedDomain.numRows();
    
    this.mipMaps = new ArrayList<MipMap>();
    for (int i = 0; i < numMipLevels; i++) {
      addMipLevel();
    }
    
    if (mipLevelNames != null) {
      if (mipLevelNames.size() != numMipLevels) {
        throw new IllegalArgumentException("mipLevelNames.size() != numMipLevels");
      }
      this.name2mipmap = new HashMap<String,MipMap>();
      for (int i = 0; i < numMipLevels; i++) {
        String mipLevelName = mipLevelNames.get(i);
        if (mipLevelName != null) {
          this.name2mipmap.put(mipLevelName, this.mipMaps.get(i));
        }
      }
    }
  }
  
  void addMipLevel() {
    final int currHighestMipLevel = mipMaps.size() - 1;
    MipMap nextMipMap = 
      new MipMap(mipMappedDomain, mipMappedRangeTuples, currHighestMipLevel + 1);
    if (!this.mipMaps.isEmpty()) {
      MipMap currMipMap = this.mipMaps.get(currHighestMipLevel);
      currMipMap.nextMipMap = nextMipMap;
    }
    this.mipMaps.add(nextMipMap);
  }
  
  /**
   * Returns the "densest" (having the most data points) MipMap in this chain whose
   * number of datapoints is not greater than <tt>maxDataPoints</tt>.
   */
  public MipMap findHighestResolution(int maxDataPoints) {
    MipMap mipMap = getMipMap(0);
    while (true) {
      int numPoints = mipMap.size();
      if (numPoints <= maxDataPoints) {
        return mipMap;
      }
      mipMap = mipMap.next();
    }
  }
  
  /**
   * Returns the index-th mapmap in this chain, where index 0 represents
   * the raw data.
   */
  public MipMap getMipMap(int index) {
    return mipMaps.get(index);
  }
  
  /**
   * Returns the mipmap bound to the specified name.
   */
  public MipMap getMipMap(String name) {
    ArgChecker.isNotNull(name, "name");
    if (name2mipmap != null) {
      return name2mipmap.get(name);
    }
    else {
      throw new UnsupportedOperationException("named MipMaps not supported for this object");
    }
  }
  
  /**
   * Returns the number of elements in each range tuple.
   */
  public int getRangeTupleSize() {
    return this.rangeTupleSize;
  }
  
  /**
   * Returns the number of {@MipMap} objects in this chain.
   */
  public int size() {
    return mipMaps.size();
  }
  
  /**
   * Returns the {@link Array2D} object that backs the domain values
   * in this object.  This method is intended for use with JUnit tests.
   */
  Array2D getMipMappedDomain() {
    return this.mipMappedDomain;
  }
  
  /**
   * Returns the list of {@link Array2D} objects that back the range
   * tuple values in this object.  This method is intended for use with 
   * JUnit tests.
   */
  Array2D[] getMipMappedRangeTuples() {
    return this.mipMappedRangeTuples;
  }  
  
  private void validate(Array2D mipMappedDomain, List<Array2D> mipMappedRangeTuple) {
    ArgChecker.isNotNull(mipMappedDomain, "mipMappedDomain");
    ArgChecker.isNotNull(mipMappedRangeTuple, "mipMappedRangeTuple");
    
    if (mipMappedRangeTuple.isEmpty()) {
      throw new IllegalArgumentException("mipMappedRangeTuple list was empty");
    }
    
    for (int i = 0; i < mipMappedRangeTuple.size(); i++) {
      Array2D mipMappedRange = mipMappedRangeTuple.get(i);
      if (!mipMappedDomain.isSameSize(mipMappedRange)) {
        throw new IllegalArgumentException("mipMappedDoamin and " +
            "mipMappedRange(" + i + ") are difference sizes");
      }
    }
  }
}
