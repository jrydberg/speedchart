package org.gwt.speedchart.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.graphics.client.Color;
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


public class SpeedChart extends VerticalPanel {

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

  private static final Resources resources = GWT.create(Resources.class);

  private TimelineModel mainModel;

  private LineGraph lineGraph;

  private final Zoom zoom;

  private final Pan pan;

  private DomainAxis domainAxis;

  private RangeAxis rangeAxis;

  static {
    StyleInjector.injectStylesheet(resources.domainAxisCss().getText()
	+ resources.rangeAxisCss().getText()
        + resources.speedGraphCss().getText());
  }

  public SpeedChart() {
    setStyleName(resources.speedGraphCss().graphBase());

    mainModel = new TimelineModel(false, false);
    mainModel.updateBounds(0, 5);
    this.zoom = new Zoom(mainModel);

    lineGraph = new LineGraph(1000, 400);
    lineGraph.setStyleName(resources.speedGraphCss()
        .mainGraph());
    zoom.addListener(lineGraph);

    this.pan = new Pan(mainModel, lineGraph);

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

	double domainWidth = mainModel.getBounds().length();
	// delta is auto coerced to double
	//Log.info("domainWidth is " + domainWidth);
	//Log.info("deltaY is " + deltaY);
	double deltaFraction = 1.0 + (deltaY / 10.0);
	double newDomainWidth = domainWidth * deltaFraction;
	double domainDelta = newDomainWidth - domainWidth;
	//Log.info("domainDelta: " + domainDelta);

	// move the bounds
	double newLeft = mainModel.getLeftBound() + domainDelta;
	double newRight = mainModel.getRightBound() - domainDelta;
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


    // Hookup window bounds observer to the main timeline model and
    // redraw the main graphs based on changes to the model.
    mainModel.addWindowBoundsObserver(
      new WindowBoundsObserver() {
	public void onWindowBoundsChange(double domainStart, 
	    double domainEnd) {
	  //Log.info("redraw to: " + domainStart + ", " + domainEnd);
	  redraw();
	}
      });    

    HorizontalPanel horizPanel = new HorizontalPanel();
    horizPanel.add(rangeAxis);
    horizPanel.add(lineGraph);
    add(horizPanel);

    //add(rangeAxis);
    //add(lineGraph);
    add(domainAxis);
  }

  public void fillWidth() {
    // FIXME: use getDomainExtrema for each dataset instead 

    Interval widestDomain = lineGraph.calcWidestDomain();
    Log.info("widest domain: " + widestDomain);
    mainModel.updateBounds(widestDomain.getStart(),
        widestDomain.getEnd());
  }
  
//   private void sinkEvents() {
//     MainEventListener mainEventListener = new MainEventListener();
//     lineGraph.addMouseWheelListener(mainEventListener);
//   }

  public void addDataset(Dataset ds, GraphUiProps graphUiProps) {
    lineGraph.addDataset(ds, graphUiProps);
  }

  public void redraw() {
    Interval mainDomain = new Interval(mainModel.getLeftBound(),
         mainModel.getRightBound());
    lineGraph.draw(mainDomain);
    domainAxis.draw(mainDomain);
    rangeAxis.draw(lineGraph.getVisRange());
  }

}
