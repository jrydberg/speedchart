package org.gwt.speedchart.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.graphics.client.Color;

import com.google.gwt.user.client.ui.LayoutPanel;
//import com.google.gwt.layout.client.Layout;
//import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.dom.client.Style.Unit;

import org.gwt.speedchart.client.graph.LineGraph;
import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.graph.axis.DomainAxis;
import org.gwt.speedchart.client.graph.axis.RangeAxis;
import org.gwt.speedchart.client.graph.TimelineModel.WindowBoundsObserver;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.fx.Zoom;
import org.gwt.speedchart.client.fx.Pan;

import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Strict;
import com.google.gwt.resources.client.ClientBundle;

import com.allen_sauer.gwt.log.client.Log;


public class SparklineChart extends AbstractChart {

  /**
   * Css stylenames.
   */
  public interface Css extends CssResource {
    String sparkline();

  }

  /**
   * Resources for {@link TimeLineGraph}.
   */
  public interface Resources extends ClientBundle {
    @Source("resources/SparklineChart.css")
    @Strict
    Css sparklineCss();
  }

  private final Resources resources;

  private LineGraph lineGraph;

  private static boolean styleInjected;

  private final Zoom zoom;

  public SparklineChart() {
    this(new TimelineModel(true, false));
  }

  public SparklineChart(TimelineModel timelineModel) {
    this((Resources) GWT.create(Resources.class), timelineModel, null);
  }

  public SparklineChart(Resources resources) {
    this(resources, new TimelineModel(true, false), null);
  }

  public SparklineChart(Resources resources, 
      TimelineModel timelineModel, ChartUiProps chartUiProps) {
    super(timelineModel);

    if (chartUiProps == null) {
      chartUiProps = new ChartUiProps();
      chartUiProps.setDrawBorders(false);
    }

    this.resources = resources;
    setStyleName(resources.sparklineCss().sparkline());
    
    // Create the line graph with any size what-so-ever since it will
    // be re-sized anyway.
    lineGraph = new LineGraph(chartUiProps);

    // Add zoom that we use for transitions, but do not add the graph
    // as listener since we do want to transition with full graphics.
    zoom = new Zoom(getTimelineModel());

    add(lineGraph);
    setWidgetLeftRight(lineGraph, 0, Unit.PX, 0, Unit.PX);
    setWidgetTopBottom(lineGraph, 0, Unit.PX, 0, Unit.PX);

    if (!styleInjected) {
      styleInjected = true;

      final Resources defaultResources = GWT.create(
          Resources.class);
      StyleInjector.injectStylesheet(defaultResources
          .sparklineCss().getText());
    }
  }

  public void fillWidth() {
    // FIXME: use getDomainExtrema for each dataset instead 
    Interval widestDomain = lineGraph.calcWidestDomain();
    timelineModel.updateBounds(widestDomain.getStart(),
        widestDomain.getEnd());
  }
  
  @Override
  public void addDataset(Dataset ds, GraphUiProps graphUiProps) {
    lineGraph.addDataset(ds, graphUiProps);
    super.addDataset(ds, graphUiProps);
  }

  @Override
  public void redraw() {
    if (datasets.size() != 0) {
      lineGraph.draw(getDomain());
    }
  }

  @Override
  public boolean removeDataset(Dataset ds) {
    lineGraph.removeDataset(ds);
    return super.removeDataset(ds);
  }

  /**
   * Set the width of the current visible domain window.  The new
   * domain window will be aligned with the previous right window.
   *
   * @param domainWidth new domain width
   */
  public void setDomainWidth(double domainWidth) {
    Interval newDomain = new Interval(
        getTimelineModel().getRightBound() - domainWidth,
        getTimelineModel().getRightBound());
    transitionTo(newDomain);
    //getTimelineModel().updateBounds(newDomain.getStart(),
    //    newDomain.getEnd());
  }

  @Override
  protected void transitionTo(Interval newDomain) {
    zoom.zoom(300, newDomain);
  }
}
