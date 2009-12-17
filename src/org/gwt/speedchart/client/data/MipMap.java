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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a version of a {@link Dataset} at a decreased level
 * of resolution.
 * 
 * @see {@link MipMapChain}
 * @see {@link Dataset#getMipMapChain()}
 * 
 * @author chad takahashi
 */
public class MipMap {
  private Array1D domain;
  private FlyweightTuple flyweightTuple;
  private int mipLevel;
  private Array1D[] rangeTuples;
  
  MipMap nextMipMap;
  
  public MipMap(Array1D domain, Array1D[] rangeTuples) {
    ArgChecker.isNotNull(domain, "domain");
    ArgChecker.isNotNull(rangeTuples, "rangeTuples");
    
    this.mipLevel = 0;
    this.domain = domain;
    this.rangeTuples = rangeTuples;
    this.flyweightTuple = new FlyweightTuple(this.domain, this.rangeTuples);
  }
  
  MipMap(Array2D multiResDomain, Array2D[] multiResRangeTuple, int mipLevel) {
    ArgChecker.isNotNull(multiResDomain, "multiResDomain");
    ArgChecker.isNotNull(multiResRangeTuple, "multiResRangeTuple");
    ArgChecker.isNonNegative(mipLevel, "mipLevel");
    
    this.mipLevel = mipLevel;
    this.domain = multiResDomain.getRow(mipLevel);
    
    this.rangeTuples = new Array1D[multiResRangeTuple.length];
    for (int i = 0; i < this.rangeTuples.length; i++) {
      this.rangeTuples[i] = multiResRangeTuple[i].getRow(mipLevel);
    }
    this.flyweightTuple = new FlyweightTuple(this.domain, this.rangeTuples);
  }
  
  /**
   * Returns the array of domain values within this {@link MipMap}.
   */
  public Array1D getDomain() {
    return this.domain;
  }
  
  /**
   * Returns the ordinal mip level of this mipmap object.  Level 0
   * represents the "raw" dataset data.
   */
  public int getLevel() {
    return this.mipLevel;
  }
  
  /**
   * Returns the datapoint tuple at the specified index within this {@link MipMap}.
   */
  public Tuple2D getTuple(int dataPointIndex) {
    ArgChecker.isLTE(dataPointIndex, this.size() - 1, "dataPointIndex");
    this.flyweightTuple.setDomainAndRange(domain, rangeTuples);
    this.flyweightTuple.setDataPointIndex(dataPointIndex);
    return this.flyweightTuple;
  }
  
  /**
   * Returns the array of range values for a specific tuple element
   *  within this {@link MipMap}.
   */
  public Array1D getRange(int tupleIndex) {
    return this.rangeTuples[tupleIndex];
  }
  
  /**
   * Returns the number of elements in each range tuple within this mipmap. 
   */
  public int getRangeTupleSize() {
    return this.rangeTuples.length;
  }
  
  public boolean isEmpty() {
    return this.domain.isEmpty();
  }
  
  /**
   * Returns the next {@link MipMap} in this chain.
   */
  public MipMap next() {
    return this.nextMipMap;
  }
  
  /**
   * The number of data points in this mipmap.
   */
  public int size() {
    return this.domain.size();
  }
  
  /**
   * Returns an iterator over tuple data points at the resolution
   * of this mipmap.
   */
  public Iterator<Tuple2D> getTupleIterator(final int startIndex) {
    final Array1D domain = this.domain;
    final Array1D[] rangeTuples = this.rangeTuples;
    
    return new Iterator<Tuple2D>() {
      final FlyweightTuple tuple = new FlyweightTuple(domain, rangeTuples);
      final int endIndex = domain.size() - 1;
      int ptr = startIndex;
      
      public boolean hasNext() {
        return ptr <= endIndex;
      }

      public Tuple2D next() {
        if (!hasNext()) {
          throw new NoSuchElementException("ptr=" + ptr + ", endIndex=" + endIndex);
        }
        tuple.setDataPointIndex(ptr++);
        return tuple;
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
  
}
