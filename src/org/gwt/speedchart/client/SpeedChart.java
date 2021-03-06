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

import com.allen_sauer.gwt.log.client.Log;


public class SpeedChart extends AbstractChart {

  /**
   * Css stylenames.
   */
  public interface Css extends CssResource {
    String graphBase();

    String mainGraph();

    String domainAxis();
    
    String rangeAxis();
  }

  /**
   * Resources for {@link TimeLineGraph}.
   */
  public interface Resources extends DomainAxis.Resources,
      RangeAxis.Resources {
    @Source("resources/SpeedChart.css")
    @Strict
    Css speedGraphCss();
  }

  private final Resources resources;

  private LineGraph lineGraph;

  private final Zoom zoom;

  private final Pan pan;

  private DomainAxis domainAxis;

  private RangeAxis rangeAxis;

  static {
    final Resources resources = GWT.create(Resources.class);
    StyleInjector.injectStylesheet(resources.domainAxisCss().getText()
	+ resources.rangeAxisCss().getText()
        + resources.speedGraphCss().getText());
  }

  public SpeedChart() {
    this(new TimelineModel(false, true));
  }

  public SpeedChart(ChartUiProps chartUiProps) {
    this((Resources) GWT.create(Resources.class), 
        new TimelineModel(false, true), chartUiProps);
  }

  public SpeedChart(TimelineModel timelineModel) {
    this((Resources) GWT.create(Resources.class), timelineModel, null);
  }

  public SpeedChart(Resources resources, TimelineModel timelineModel,
      ChartUiProps chartUiProps) {
    super(timelineModel);
    
    if (chartUiProps == null) {
      chartUiProps = new ChartUiProps();
      chartUiProps.setDrawBorders(true);
    }

    this.resources = resources;
    setStyleName(resources.speedGraphCss().graphBase());

    this.zoom = new Zoom(getTimelineModel());

    lineGraph = new LineGraph(chartUiProps);
    lineGraph.setStyleName(resources.speedGraphCss().mainGraph());
    zoom.addListener(lineGraph);

    this.pan = new Pan(getTimelineModel(), lineGraph);

    domainAxis = new DomainAxis(resources);
    domainAxis.addStyleName(resources.speedGraphCss().domainAxis());

    rangeAxis = new RangeAxis(resources);
    rangeAxis.addStyleName(resources.speedGraphCss().rangeAxis());

    lineGraph.addMouseWheelHandler(new MouseWheelHandler() {
      public void onMouseWheel(MouseWheelEvent event) {
	int deltaY = -event.getDeltaY();
	if (deltaY == 0) {
	  return;
	}

	if (deltaY > 5)
	  deltaY = 3;
	if (deltaY < -5)
	  deltaY = -3;

	double domainWidth = getTimelineModel().getBounds().length();
	// delta is auto coerced to double
	//Log.info("domainWidth is " + domainWidth);
	//Log.info("deltaY is " + deltaY);
	double deltaFraction = 1.0 + (deltaY / 10.0);
	double newDomainWidth = domainWidth * deltaFraction;
	double domainDelta = newDomainWidth - domainWidth;
	//Log.info("domainDelta: " + domainDelta);

	// move the bounds
	double newLeft = getTimelineModel().getLeftBound() + domainDelta;
	double newRight = getTimelineModel().getRightBound() - domainDelta;
	// catch cross over
	newLeft = Math.min(newLeft, newRight);
	newRight = Math.max(newLeft, newRight);
	// Cap at bounds
	//newLeft = Math.max(0, newLeft);
	//newLeft = Math.max(min, newLeft);
	//newRight = Math.min(max, newRight);

	Log.info("zoom to: " + newLeft + ", " + newRight);
	zoom.zoom(300, new Interval(newLeft, newRight));
      }
    });

    add(rangeAxis);
    setWidgetLeftWidth(rangeAxis, 0, Unit.PX, 40, Unit.PX);
    setWidgetTopBottom(rangeAxis, 0, Unit.PX, 20, Unit.PX);

    add(lineGraph);
    setWidgetLeftRight(lineGraph, 40, Unit.PX, 0, Unit.PX);
    setWidgetTopBottom(lineGraph, 0, Unit.PX, 20, Unit.PX);

    add(domainAxis);
    setWidgetLeftRight(domainAxis, 40, Unit.PX, 0, Unit.PX);
    setWidgetBottomHeight(domainAxis, 0, Unit.PX, 20, Unit.PX);
  }

  @Override
  public void addDataset(Dataset ds, GraphUiProps graphUiProps) {
    lineGraph.addDataset(ds, graphUiProps);
    super.addDataset(ds, graphUiProps);
  }

  public void addDomainOverlay(Overlay overlay) {
    lineGraph.addDomainOverlay(overlay);
  }

  public void addRangeOverlay(Overlay overlay) {
    lineGraph.addRangeOverlay(overlay);
  }

  @Override
  public void redraw() {
    Interval domain = getDomain();
    lineGraph.draw(domain);
    if (datasets.size() != 0) {
      domainAxis.draw(domain);
      rangeAxis.draw(lineGraph.getVisRange());
    }
  }

  @Override
  public boolean removeDataset(Dataset ds) {
    lineGraph.removeDataset(ds);
    return super.removeDataset(ds);
  }

  public void removeDomainOverlay(Overlay overlay) {
    lineGraph.removeDomainOverlay(overlay);
  }

  public void removeRangeOverlay(Overlay overlay) {
    lineGraph.removeRangeOverlay(overlay);
  }

}
