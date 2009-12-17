package org.gwt.speedchart.client.data;

import org.gwt.speedchart.client.util.Array2D;

import java.util.List;

/**
 * Strategy for converting an ordered set of data points into multiple coarser
 * resolutions (i.e. "mip-mapping") for the primary purpose of speeding up
 * visual rendering.
 * 
 * @author Chad Takahashi
 */
public interface MipMapStrategy {

  /**
   * Calculates the specified domain and range at decreasing levels of
   * resolution.
   * 
   * @param domain - the domain to be mipmapped
   * @param range - the range to be mipmapped
   */
  MipMapChain mipmap(double[] domain, double[] range);

  /**
   * Calculates the specified domain and n-tuple range at decreasing levels of
   * resolution.
   * 
   * @param domain - the domain to be mipmapped.
   * @param range - the n-tuple range to be mipmapped; range.get(i) returns an
   *    array representing the i-th dimension of the range tuple values.
   */
  MipMapChain mipmap(double[] domain, List<double[]> range);

  void appendXY(double x, double y, MipMapChain mipMapChain);
  
  /**
   * Updates the Y-value of an existing datapoint within a dataset (optional
   * operation).
   * 
   * @param pointIndex - the 0-based index of the datapoint.
   * @param y - the range value to be updated.
   */
  void setRangeValue(int pointIndex, double y, Array2D mipmappedRange);

}
