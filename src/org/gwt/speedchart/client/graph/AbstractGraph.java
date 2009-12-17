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

import com.google.gwt.graphics.client.Canvas;
import com.google.gwt.graphics.client.Color;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import org.gwt.speedchart.client.GraphUiProps;
import org.gwt.speedchart.client.Dataset;
import org.gwt.speedchart.client.fx.AnimationListener;
//import org.gwt.speedchart.client.Datasets;
//import org.gwt.speedchart.client.Focus;
//import org.gwt.speedchart.client.XYPlot;
//import org.gwt.speedchart.client.axis.RangeAxis;
//import org.gwt.speedchart.client.canvas.Layer;
//import org.gwt.speedchart.client.canvas.View;
import org.gwt.speedchart.client.data.MipMap;
import org.gwt.speedchart.client.data.MipMapRegion;
import org.gwt.speedchart.client.data.tuple.Tuple2D;
//import org.gwt.speedchart.client.gss.GssElement;
//import org.gwt.speedchart.client.gss.GssProperties;
//import org.gwt.speedchart.client.plot.DefaultXYPlot;
import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.util.Array1D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;

/**
 *
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 * @author Johan Rydberg &lt;johan.rydberg@gmail.com&gt;
 */
public abstract class AbstractGraph<T extends Tuple2D> extends FocusPanel
    implements AnimationListener {

  private final int COORD_X_WIDTH;
  private final int COORD_Y_HEIGHT;

  private boolean isAnimating = false;

  private final List<DrawableDataset<T>> drawableDatasets =
      new ArrayList<DrawableDataset<T>>();
  
  protected final Canvas canvas;

  protected AbstractGraph(int width, int height) {
    COORD_X_WIDTH = width;
    COORD_Y_HEIGHT = height;
    canvas = new Canvas(COORD_X_WIDTH, COORD_Y_HEIGHT);
    setElement(canvas.getElement());
  }

  protected AbstractGraph() {
    this(1000, 400);
  }

  protected Interval visRange;
  protected Interval visDomain;

  public void animationStart() {
    isAnimating = true;
  }

  public void animationStop() {
    isAnimating = false;
  }

  private void calcVisibleDomainAndRange(List<DrawableDataset<T>> dds,
      Interval plotDomain) {

    final int numDatasets = dds.size();
    
//     for (int i = 0; i < plot.getRangeAxisCount(); i++) {
//       plot.getRangeAxis(i).resetVisibleRange();
//     }
    
    visRange = null;

    for (int datasetIdx = 0; datasetIdx < numDatasets; datasetIdx++) {
      DrawableDataset<T> drawableDataset = dds.get(datasetIdx);
      Dataset<T> dataSet = drawableDataset.dataset;


      if (!plotDomain.intersects(dataSet.getDomainExtrema())) {
        continue;
      }

      // Find the highest-resolution mipmap whose number of data points
      // that lie within the plot domain is <= maxDataPoints.
      final int maxDrawableDataPoints = getMaxDrawableDataPoints(
          drawableDataset);
      MipMapRegion bestMipMapRegion = dataSet
          .getBestMipMapForInterval(plotDomain, maxDrawableDataPoints);

      MipMap bestMipMap = bestMipMapRegion.getMipMap();
      if (drawableDataset.currMipMap.getLevel() != bestMipMap.getLevel()) {
        drawableDataset.currMipMap = bestMipMap;
        //plot.getHoverPoints()[datasetIdx] = DefaultXYPlot.NO_SELECTION;
      }

      int domainStartIdx = bestMipMapRegion.getStartIndex();
      int domainEndIdx = bestMipMapRegion.getEndIndex();
      domainStartIdx = Math.max(0, domainStartIdx - 1);
      domainEndIdx = Math.min(domainEndIdx, dataSet.getNumSamples() - 1);

      drawableDataset.visDomainStartIndex = domainStartIdx;
      drawableDataset.visDomainEndIndex = domainEndIdx;
      //Log.info("draw domain start: " + domainStartIdx);
      //Log.info("draw end start: " + domainEndIdx);


      //RangeAxis rangeAxis = drawableDataset.graphUiProps.getRangeAxis(datasetIdx);
      Interval visRange = dataSet.getPreferredRangeAxisInterval();
      if (visRange == null) {
	visRange = calcVisibleRange(bestMipMap, domainStartIdx, domainEndIdx);
      }
      if (this.visRange == null) {
	this.visRange = visRange;
      } else {
	this.visRange.expand(visRange);
      }
 
//       if (rangeAxis.isCalcRangeAsPercent()) {
//         final double refY = calcReferenceY(rangeAxis, drawableDataset);
//         double maxY = visRange.getEnd();
//         double minY = visRange.getStart();
//         visRange.setEndpoints(
//             RangeAxis.calcPrctDiff(refY, minY), 
//             RangeAxis.calcPrctDiff(refY, maxY));
//       }
      
//       rangeAxis.adjustVisibleRange(visRange);
    }

    Log.info("visible range is from " + visRange.getStart() + " to " + visRange.getEnd());
  }

  private double lx, ly, fx;

  protected double rangeToScreenY(double dataY) {
    //Log.info("rangeToScreenY: " + dataY);
    double c = (dataY - visRange.getStart()) / (
      visRange.getEnd() - visRange.getStart());
    return COORD_Y_HEIGHT - c * COORD_Y_HEIGHT;
  }

  protected double domainToScreenX(double dataX) {
    double c = visDomain.getRatioFromPoint(dataX);
    //Log.info("domainToScreenX: " + dataX + "; c=" + c);
    return COORD_X_WIDTH * c;
  }

  protected void beginDrawing() {
    canvas.clear();
  }

  protected void beginCurve(DrawableDataset<T> dds) {
    canvas.beginPath();
    lx = ly = fx = -1;
  }

  protected void drawCurvePart(DrawableDataset<T> dds, T point, int methodCallCount) {
    double ux = domainToScreenX(point.getDomain());
    double uy = rangeToScreenY(point.getRange0());

    if (methodCallCount == 0) {
      fx = lx = ux;
      ly = uy;
    } else {
      if (methodCallCount == 1) {
	//Log.info("move to: " + lx + "," + ly);
	canvas.moveTo(lx, ly);
      }

      //Log.info("line to: " + ux + "," + uy);
      canvas.lineTo(ux, uy);

      lx = ux;
      ly = uy;
    }
  }

  protected void endCurve(DrawableDataset<T> dds) {
    canvas.setGlobalAlpha(1.0);
    canvas.setStrokeStyle(dds.graphUiProps.getStrokeColor());
    canvas.stroke();

    if (!isAnimating) {
      canvas.setGlobalAlpha(0.5);
      canvas.setFillStyle(dds.fillGradient);
      canvas.lineTo(lx, COORD_Y_HEIGHT);
      canvas.lineTo(fx, COORD_Y_HEIGHT);
      canvas.fill();
    }
  }

  /**
   * Calculates the range-Y extrema values of the specified {@link MipMap}.
   */
  private Interval calcVisibleRange(MipMap mipMap, int domainStartIdx, int domainEndIdx) {
    double rangeMin = Double.POSITIVE_INFINITY;
    double rangeMax = Double.NEGATIVE_INFINITY;
    Iterator<Tuple2D> tupleItr = mipMap.getTupleIterator(domainStartIdx);
    for (int i = domainStartIdx; i <= domainEndIdx; i++) {
      double y = tupleItr.next().getRange0();
      rangeMin = Math.min(rangeMin, y);
      rangeMax = Math.max(rangeMax, y);
    }

    return new Interval(rangeMin, rangeMax);
  }

  private void drawDataset(int datasetIndex) {
    DrawableDataset dds = drawableDatasets.get(datasetIndex);

    Dataset<T> dataSet = dds.dataset;
    //DatasetRenderer<T> renderer = dds.getRenderer();

    if (dataSet.getNumSamples() < 2) {
      return;
    }

    beginCurve(dds);

    MipMap currMipMap = dds.currMipMap;
    final int domainStartIdx = dds.visDomainStartIndex;
    final int domainEndIdx = dds.visDomainEndIndex;
    //RangeAxis rangeAxis = dds.graphUiProps.getRangeAxis(datasetIndex);
    //final boolean calcRangeAsPercent = rangeAxis.isCalcRangeAsPercent();
    
    // Render the curve
    // final double refY = calcReferenceY(dds.graphUiProps, dds);
    
    int methodCallCount = 0;
    Iterator<Tuple2D> tupleItr = currMipMap.getTupleIterator(domainStartIdx);
    for (int i = domainStartIdx; i <= domainEndIdx; i++) {
      Tuple2D dataPt = tupleItr.next();

//       if (calcRangeAsPercent) {
//         LocalTuple tmpTuple = new LocalTuple();
//         tmpTuple.setXY(dataPt.getDomain(), 
// 		       RangeAxis.calcPrctDiff(refY, dataPt.getRange0()));
//         dataPt = tmpTuple;
//       }
      // FIXME: refactor to remove cast
      drawCurvePart(dds, (T) dataPt, methodCallCount++);
    }
    endCurve(dds);
  }

  public Interval calcWidestDomain() {
    if (drawableDatasets.isEmpty()) {
      return null;
    }

    Interval widestPlotDomain = null;
    for (DrawableDataset<T> dds : drawableDatasets) {
      Dataset<T> ds = dds.dataset;
      final int maxDrawableDataPoints = getMaxDrawableDataPoints(dds);
      MipMap mm = ds.getMipMapChain()
          .findHighestResolution(maxDrawableDataPoints);

      Interval drawableDomain = getDrawableDomain(mm.getDomain());
      if (widestPlotDomain == null) {
        widestPlotDomain = drawableDomain;
      } else {
        widestPlotDomain.expand(drawableDomain);
      }
    }

    return widestPlotDomain;
  }

  /**
   * Subclasses can override this method to return a domain span that's larger
   * than the maximum according to current mipmapped domain associated with the
   * {@link DrawableDataset}.  This is sometimes necessary depending on how each
   * datapoint is rendered (e.g. barchart requires domain padding to avoid
   * cropping of the end point bars).
   */
  protected Interval getDrawableDomain(Array1D mipmappedDomain) {
    return new Interval(mipmappedDomain.get(0), mipmappedDomain.getLast());
  }

  public void draw() {
    if (visDomain == null) {
      visDomain = calcWidestDomain();
    }
    drawDatasets();
  }

  public void draw(Interval domain) {
    visDomain = domain.copy();
    drawDatasets();
  }
  
  private void drawDatasets() {
    calcVisibleDomainAndRange(drawableDatasets, visDomain);

    //Log.info("visible domain is: " + visDomain.getStart() 
    //	     + ", " + visDomain.getEnd());

    beginDrawing();

    final int numDatasets = drawableDatasets.size();
    for (int i = 0; i < numDatasets; i++) {
      drawDataset(i);
    }
  }

  /**
   * Adds a new dataset to the list of datasets that this renderer is
   * responsible for.
   */
  public void addDataset(Dataset<T> dataset, GraphUiProps graphUiProps) {
    ArgChecker.isNotNull(dataset, "dataset");

    DrawableDataset<T> drawableDataset = new DrawableDataset<T>();
    drawableDataset.dataset = dataset;
    drawableDataset.graphUiProps = graphUiProps;
    drawableDataset.currMipMap = drawableDataset.dataset.getMipMapChain()
        .getMipMap(0);
    drawableDataset.maxDrawablePoints = getCurrentMaxDrawableDatapoints();
    drawableDataset.fillGradient = canvas.createLinearGradient(0, 0, 
        0, COORD_Y_HEIGHT);
    drawableDataset.fillGradient.addColorStop(0.8, Color.WHITE);
    drawableDataset.fillGradient.addColorStop(0, graphUiProps.getGraphColor());
    //drawableDataset.fillGradient.addColorStop(0, graphUiProps.getGraphColor());

    drawableDatasets.add(drawableDataset);
  }

  public int getCurrentMaxDrawableDatapoints() {
    return (isAnimating ? 100 : 800);
  }

  /**
   * Returns the lesser of what the {@link XYPlot} and the {@link
   * DatasetRenderer} report as the maximum number of datapoints that they can
   * handle.
   */
  private int getMaxDrawableDataPoints(DrawableDataset dds) {
    int maxDrawablePoints = getCurrentMaxDrawableDatapoints();
    return Math.min(dds.maxDrawablePoints, maxDrawablePoints);
  }

  /**
   * Removes the specified dataset from the list of datasets that this renderer
   * is responsible for.
   */
  public void removeDataset(Dataset<T> dataset) {
    ArgChecker.isNotNull(dataset, "dataset");

    boolean wasDatasetFound = false;
    for (int i = 0; i < drawableDatasets.size(); i++) {
      DrawableDataset<T> drawableDataset = this.drawableDatasets.get(i);
      if (dataset == drawableDataset.dataset) {
        drawableDatasets.remove(i);
        drawableDataset.invalidate();
        wasDatasetFound = true;
        break;
      }
    }

    // throw a fit if we can't find the dataset-to-be-removed
    if (!wasDatasetFound) {
      throw new RuntimeException(
          "dataset did not exist in drawableDatasets list");
    }
  }

  public void resetMipMapLevels() {
    for (DrawableDataset<T> dds : this.drawableDatasets) {
      dds.currMipMap = dds.dataset.getMipMapChain().getMipMap(0);
    }
  }

  public double calcReferenceY(GraphUiProps guip, DrawableDataset dds) {
    final int refYIndex = guip.isAutoZoomVisibleRange() ? dds.visDomainStartIndex : 0;
    return dds.currMipMap.getTuple(refYIndex).getRange0();
  }
  
  private static final class LocalTuple implements Tuple2D {
    private double x, y;
    
    public void setXY(double x, double y) {
      this.x = x;
      this.y = y;
    }
    
    public double getDomain() {
      return x;
    }

    public double getRange(int index) {
      if (index == 0) {
        return y;
      }
      throw new UnsupportedOperationException("unsupported tuple index: " + index);
    }

    public double getRange0() {
      return y;
    }

    public int size() {
      return 2;
    }
    
    public String toString() {
      return "x=" + (long)x + "; y=" + y;
    }
  }
}
