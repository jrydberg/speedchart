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
import org.gwt.speedchart.client.util.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request to construct an instance of {@link Dataset} using
 * {@link DatasetFactory}.
 * 
 * @see DatasetFactory
 * 
 * @author Chad Takahashi
 */
public abstract class DatasetRequest {
 
  /**
   * Request in which client provides their own n-tuple data, and the
   * mipmapped data is then computed by the provided
   * {@link #setDefaultMipMapStrategy(MipMapStrategy) MipMapStrategy} object.
   */
  public static class Basic extends DatasetRequest {
    private double[] domainData;
    
    // tupleData[n] represents the Nth dimension value of each range tuple
    private List<double[]> rangeTupleData = new ArrayList<double[]>();
    
    public double[] getDomain() {
      if (domainData == null) {
        throw new IllegalStateException("domain not set");
      }
      return domainData;
    }
    
    public int getTupleLength() {
      return 1 + rangeTupleData.size();
    }
    
    /**
     * Returns an array containing the Nth element of every tuple in this request,
     * where N is the specified index. 
     */
    public double[] getRangeTupleSlice(int rangeTupleIndex) {
      return rangeTupleData.get(rangeTupleIndex);
    }
    
    /**
     * The first tuple slice (dimension) added is assumed to be the domain,
     * whose values must be in sorted ascending order.  Subsequent slices are
     * assumed to be components of an n-tuple range.
     *  
     * See {@link #getRangeTupleSlice(int)}.
     */
    public void addRangeTupleSlice(double[] slice) {
      ArgChecker.isNotNull(slice, "slice");
      rangeTupleData.add(slice);
    }
    
    /**
     * Sets the domain values for this request.
     */
    public void setDomain(double[] domain) {
      ArgChecker.isNotNull(domain, "domain");
      this.domainData = domain;
    }
    
    /**
     * Replaces the range tuple slice at the specified tuple coordinate 
     * with the new slice.
     * 
     * @return the range tuple slice previously at the specified coordinate.
     */
    public double[] setRangeTupleSlice(int rangeTupleCoordinate, double[] slice) {
      return rangeTupleData.set(rangeTupleCoordinate, slice);
    }
    
    public void validate() {
      super.validate();
      
      // Make sure everything's non-null
      ArgChecker.isNotNull(domainData, "domainData");
      for (int i = 0; i < rangeTupleData.size(); i++) {
        ArgChecker.isNotNull(rangeTupleData.get(i), "tupleData[" + i + "]");
      }
      
      // Make sure all double[] elements are the same length
      final int domainLength = domainData.length;
      for (int i = 0; i < rangeTupleData.size(); i++) {
        double[] tupleSlice = rangeTupleData.get(i);
        int currLength = tupleSlice.length;
        if (currLength != domainLength) {
          throw new IllegalArgumentException("tupleData[" + i + "].length=" + 
              currLength + " but domainData.length=" + domainLength);
        }
      }
    }
  }
  
  /**
   * Request in which the n-tuple values at each mipmap level must be 
   * explicitly assigned.
   */
  public static class MultiRes extends DatasetRequest {
    private Array2D mipmappedDomainData;
    private List<Array2D> mipmappedRangeTupleData = new ArrayList<Array2D>();
    
    public List<Array2D> getMultiResRangeTuples() {
      return mipmappedRangeTupleData;
    }
    
    public Array2D getMultiresDomain() {
      return mipmappedDomainData;
    }
    
    public void addMultiresRangeTupleSlice(Array2D slice) {
      ArgChecker.isNotNull(slice, "slice");
      mipmappedRangeTupleData.add(slice);
    }

    public int getTupleLength() {
      return 1 + mipmappedRangeTupleData.size();
    }
    
    public void setMultiresDomain(Array2D domain) {
      ArgChecker.isNotNull(domain, "domain");
      mipmappedDomainData = domain;
    }
    
    public void validate() {
      ArgChecker.isNotNull(mipmappedDomainData, "mipmappedDomainData");
      for (int i = 0; i < mipmappedRangeTupleData.size(); i++) {
        ArgChecker.isNotNull(mipmappedRangeTupleData.get(i), "mipmappedTupleData[" + i + "]");
      }

      // Verify that multiDomain and multiRange have same number
      // of elements at each level
      for (int i = 1; i < mipmappedRangeTupleData.size(); i++) {
        Array2D mipmappedTupleSlice = mipmappedRangeTupleData.get(i);
        if (!mipmappedTupleSlice.isSameSize(mipmappedDomainData)) {
          throw new IllegalArgumentException(
              "i=" + i + ": domain and range mipmaps differ in size");
        }
      }
    }
  }

  private String axisId, identifier, rangeLabel;
  private MipMapStrategy defaultMipMapStrategy = BinaryMipMapStrategy.MEAN;
  private double rangeBottom = Double.NaN, rangeTop = Double.NaN;
  private String preferredRenderer;
  
  public String getAxisId() {
    return axisId;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getRangeLabel() {
    return rangeLabel;
  }

  public MipMapStrategy getDefaultMipMapStrategy() {
    return defaultMipMapStrategy;
  }
  
  /**
   * See {@link Dataset#getPreferredRenderer()}.
   */
  @Deprecated
  public String getPreferredRenderer() {
    return this.preferredRenderer;
  }
  
  /**
   * The client can optionally specify its own range bottom and top, which will 
   * be used by the {@link RangeAxis} to override the actual min/max range values 
   * contained in the {@link Dataset}.  Should be set in conjunction with
   * {@link #getRangeTop()}.
   */
  public double getRangeBottom() {
    return rangeBottom;
  }

  /**
   * The client can optionally specify its own range bottom and top, which will 
   * be used by the {@link RangeAxis} to override the actual min/max range values 
   * contained in the {@link Dataset}.  Should be set in conjunction with
   * {@link #getRangeBo(ttom)}.
   */
  public double getRangeTop() {
    return rangeTop;
  }
  
  /**
   * Returns the number of elements that each tuple is capable of storing.
   */
  public abstract int getTupleLength();
  
  /**
   * Returns the preferred range axis interval, whose start value is 
   * {@link #getRangeBottom()} and whose end value is {@link #getRangeTop()}.
   * 
   * @see {@link Dataset#getPreferredRangeAxisInterval()}
   */
  public Interval getPreferredRangeAxisInterval() {
    if (!(Double.isNaN(rangeBottom) || Double.isNaN(rangeTop))) {
      return new Interval(rangeBottom, rangeTop);
    }
    
    return null;
  }
  
  public void setAxisId(String axisId) {
    this.axisId = axisId;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setRangeLabel(String label) {
    this.rangeLabel = label;
  }

  public void setDefaultMipMapStrategy(MipMapStrategy mipMapStrategy) {
    this.defaultMipMapStrategy = mipMapStrategy;
  }
  
  @Deprecated
  public void setPreferredRenderer(String preferredRenderer) {
    this.preferredRenderer = preferredRenderer;
  }
  
  public void setRangeBottom(double rangeBottom) {
    this.rangeBottom = rangeBottom;
  }

  public void setRangeTop(double rangeTop) {
    this.rangeTop = rangeTop;
  }

  /**
   * Validates the state of this request object.
   */
  public void validate() {
    ArgChecker.isNotNull(defaultMipMapStrategy, "defaultMipMapStrategy");
    //TODO: add checker that can compare two args and report an error in the
    //relationship between them
  }

}
