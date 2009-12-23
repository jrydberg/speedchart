package org.gwt.speedchart.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import org.gwt.speedchart.client.data.MutableDataset2D;
import org.gwt.speedchart.client.graph.LineGraph;
import org.gwt.speedchart.client.graph.TimelineModel.WindowBoundsObserver;
import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.util.Interval;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractChart extends LayoutPanel implements RequiresResize {
  
  protected TimelineModel timelineModel;
  
  protected List<Dataset> datasets;

  private class TimelineObserver implements WindowBoundsObserver {

    public void onWindowBoundsChange(double domainStart,
        double domainEnd) {
      redraw();
    }
  }

  private final TimelineObserver timelineObserver = 
      new TimelineObserver();

  public AbstractChart(TimelineModel timelineModel) {
    setTimelineModel(timelineModel);
  }

  public void addDataset(Dataset ds, GraphUiProps graphUiProps) {
    if (datasets == null) {
      datasets = new ArrayList<Dataset>();
    }
    datasets.add(ds);
    timelineModel.onDatasetAdded(ds);

    if (ds instanceof MutableDataset2D) {
      ((MutableDataset2D) ds).addListener(timelineModel);
    }
  }

  public TimelineModel getTimelineModel() {
    return timelineModel;
  }

  /**
   * Return current visible domain interval.
   */
  public Interval getDomain() {
    return new Interval(timelineModel.getLeftBound(),
        timelineModel.getRightBound());
  }

  public boolean removeDataset(Dataset ds) {
    if (datasets != null) {
      datasets.remove(ds);

      if (ds instanceof MutableDataset2D) {
	timelineModel.onDatasetRemoved(ds);
	((MutableDataset2D) ds).removeListener(timelineModel);
      }

      return true;
    }
    return false;
  }

  public abstract void redraw();

  public void setTimelineModel(TimelineModel timelineModel) {
    if (this.timelineModel != null) {
      this.timelineModel.removeWindowBoundsObserver(timelineObserver);
    }

    this.timelineModel = timelineModel;
    timelineModel.addWindowBoundsObserver(timelineObserver);
  }

  @Override
  public void onResize() {
    super.onResize();
    if (datasets.size() != 0)
      redraw();
  }

//   @Override
//   public void onLoad() {
//     super.onLoad();
//     forceLayout();
//   }

  /**
   * Update timeline model of the chart so that it covers all
   * datasets.
   */
  public void zoomAll() {
    if (datasets.size() != 0) {
      Interval domain = null;
      for (Dataset ds : datasets) {
	if (domain == null) {
	  domain = ds.getDomainExtrema().copy();
	} else {
	  domain.expand(ds.getDomainExtrema());
	}
      }

      this.transitionTo(domain);
    }
  }

  protected void transitionTo(Interval newDomain) {
    getTimelineModel().updateBounds(newDomain.getStart(),
        newDomain.getEnd());
  }

}
