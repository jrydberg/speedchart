package org.gwt.speedchart.client.graph;

import com.google.gwt.graphics.client.Canvas;
import com.google.gwt.graphics.client.Color;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.RequiresResize;

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
import org.gwt.speedchart.client.util.Array2D;
import org.gwt.speedchart.client.util.MutableArray1D;
import org.gwt.speedchart.client.util.JavaArray1D;
import org.gwt.speedchart.client.util.JavaArray2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Element;


public class AreaGraph<T extends Tuple2D> 
    extends AbstractGraph<T> {

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

  /**
   * Information that is held for a {@link Dataset} while the drawing
   * is planned.
   */
  private static final class PlannedDataset {
    
    public Tuple2D next;

    public Tuple2D last;

    public double trend;

    public Iterator<Tuple2D> iterator;

    public double getRange() {
      return last.getRange0();
    }

    public double rangeForDomain(double domain) {
      if (domain == getDomain()) 
	return getRange();
      else
	return trend * (domain - last.getDomain());
    }

    public double getDomain() {
      return next.getDomain();
    }

    public void calcLineTrend() {
      if (iterator.hasNext()) {
	last = next;
	next = iterator.next();
	
	trend = (next.getRange0() - last.getRange0())
	  / (next.getDomain() - last.getDomain());
      } else {
	next = null;
      }
    }

    public boolean hasSamples() {
      return next != null;
    }

  }

  private static final class Planning {
    
    public MutableArray1D domainArray = new JavaArray1D();

    public Array2D rangeArray = new JavaArray2D();
  }

  public AreaGraph() {
    super(1000, 500);
  }
  
  private Planning planDraw(List<DrawableDataset<T>> dds,
      Interval plotDomain) {
    final Planning planning = new Planning();

    final int numDatasets = dds.size();
    PlannedDataset[] plannedDatasets = new PlannedDataset[numDatasets];

    for (int datasetIdx = 0; datasetIdx < numDatasets; datasetIdx++) {
      DrawableDataset<T> drawableDataset = dds.get(datasetIdx);
      Dataset<T> dataSet = drawableDataset.dataset;

      if (!plotDomain.intersects(dataSet.getDomainExtrema())) {
        continue;
      }

      final int maxDrawableDataPoints = getMaxDrawableDataPoints(
	drawableDataset);
      MipMapRegion bestMipMapRegion = dataSet
	.getBestMipMapForInterval(plotDomain, maxDrawableDataPoints);
      MipMap bestMipMap = bestMipMapRegion.getMipMap();

      PlannedDataset plannedDataset = new PlannedDataset();
      Iterator<Tuple2D> iterator = bestMipMap.getTupleIterator(
          bestMipMapRegion.getStartIndex());
      plannedDataset.iterator = iterator;
      plannedDataset.last = plannedDataset.next = iterator.next();
      plannedDatasets[datasetIdx] = plannedDataset;
    }

    int sampleIdx = 0;

    visRange = null;

    while (true) {
      double currDomain = Double.POSITIVE_INFINITY;

      boolean isDone = true;
      for (int idx = 0; idx < numDatasets; idx++) {
	PlannedDataset plannedDataset = plannedDatasets[idx];
	if (plannedDataset == null || !plannedDataset.hasSamples())
	  continue;
	
	isDone = false;
	currDomain = Math.min(currDomain, plannedDataset.getDomain());
      }
      
      if (isDone)
	break;

      double range = 0.0;
      for (int idx = 0; idx < numDatasets; idx++) {
	PlannedDataset plannedDataset = plannedDatasets[idx];
	range += plannedDataset.rangeForDomain(currDomain);
	if (plannedDataset.getDomain() == currDomain) {
	  plannedDataset.calcLineTrend();
	}
	planning.rangeArray.set(idx, sampleIdx, range);
      }

      if (visRange == null) {
	visRange = new Interval(range, range);
      } else {
	visRange.expand(range);
      }

      planning.domainArray.set(sampleIdx, currDomain);

      sampleIdx++;
    }

    return planning;
  }

  @Override
  protected void drawDatasets() {
    Planning planning = planDraw(drawableDatasets, visDomain);

    beginDrawing();

    final int numDatasets = drawableDatasets.size();
    for (int idx = numDatasets - 1; idx >= 0; idx--) {
      drawDataset(planning, idx);
    }
  }

  private void drawDataset(Planning planning, 
      int datasetIndex) {
    DrawableDataset dds = drawableDatasets.get(datasetIndex);
    Dataset<T> dataSet = dds.dataset;
    
    if (planning.domainArray.size() < 2) {
      return;
    }

    beginCurve(dds);

    final int numSamples = planning.domainArray.size();
    int methodCallCount = 0;

    for (int sampleIdx = 0; sampleIdx < numSamples; sampleIdx++) {
      LocalTuple curvePt = new LocalTuple();
      curvePt.setXY(planning.domainArray.get(sampleIdx),
          planning.rangeArray.get(datasetIndex, sampleIdx));
      drawCurvePart(dds, (T) curvePt, methodCallCount++);
    }

    endCurve(dds);
  }

}