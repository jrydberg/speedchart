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

package org.gwt.speedchart.client.graph;

import org.gwt.speedchart.client.Dataset;
import org.gwt.speedchart.client.GraphUiProps;
import org.gwt.speedchart.client.data.MipMap;
import org.gwt.speedchart.client.data.tuple.Tuple2D;

import com.google.gwt.graphics.client.CanvasGradient;

/**
 * Represents a dataset along with all the associated information needed to 
 * render it.
 * 
 * @author chad takahashi
 */
public class DrawableDataset<T extends Tuple2D> {
  
  /**
   * Sets all associated objects to null and all primitive number vales to -1.
   */
  public void invalidate() {
    this.dataset = null;
    this.currMipLevel = -1;
    this.maxDrawablePoints = -1;
    this.visDomainEndIndex = -1;
    this.visDomainStartIndex = -1;
  }
  
  /**
   * Sets the most recent mip level used by {@link DefaultXYPlot} to draw
   * this dataset.
   */
  public void setCurrMipLevel(int mipLevel) {
    if (mipLevel != this.currMipLevel) {
      this.currMipLevel = mipLevel;
      this.currMipMap = this.dataset.getMipMapChain().getMipMap(mipLevel);
    }
  }
  
  public GraphUiProps graphUiProps;
  public CanvasGradient fillGradient;

  /**
   * The dataset model to be rendered
   */
  public Dataset<T> dataset;
  
  /**
   * The maximum number of domain points that this dataset's associated renderer
   * is capable of handling.
   */
  public int maxDrawablePoints;
  
  /**
   * Stores the start and end data point indices that are currently visible in
   * the plot.
   */
  public int visDomainStartIndex, visDomainEndIndex;
  
  /**
   * Returns the {@link MipMap} that's currently being used by the active
   * {@link #renderer}. 
   */
  public MipMap currMipMap;
  
  /**
   * The most recent mip level used to render this dataset.
   */
  private int currMipLevel = -1;


}
