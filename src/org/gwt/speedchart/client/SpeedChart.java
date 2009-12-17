package org.gwt.speedchart.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.graphics.client.Color;
import org.gwt.speedchart.client.graph.LineGraph;
import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.graph.axis.DomainAxis;
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
    String overviewGraph();
  }

  /**
   * Resources for {@link TimeLineGraph}.
   */
  public interface Resources extends DomainAxis.Resources {
    @Source("resources/SpeedChart.css")
    @Strict
    Css speedGraphCss();
  }

  private static final Resources resources = GWT.create(Resources.class);

  private TimelineModel mainModel;

  private LineGraph lineGraph;

  private final Zoom zoom;

  private final Pan pan;

  private class OverviewTimelineModel extends TimelineModel {
    public OverviewTimelineModel() {
      super(false, false);
    }
  }

  private DomainAxis domainAxis;

  private OverviewTimelineModel overviewModel;

  private LineGraph overviewGraph;

  static {
    StyleInjector.injectStylesheet(resources.domainAxisCss().getText()
        + resources.speedGraphCss().getText());
  }

  public SpeedChart() {
    setStyleName(resources.speedGraphCss().graphBase());

    mainModel = new TimelineModel(false, false);
    mainModel.updateBounds(0, 5);
    this.zoom = new Zoom(mainModel);

    overviewModel = new OverviewTimelineModel();
    overviewModel.updateBounds(0, 5);

    overviewGraph = new LineGraph(1000, 50);
    overviewGraph.setStyleName(resources.speedGraphCss()
        .overviewGraph());

    lineGraph = new LineGraph(1000, 400);
    lineGraph.setStyleName(resources.speedGraphCss()
        .mainGraph());
    zoom.addListener(lineGraph);

    this.pan = new Pan(mainModel, lineGraph);

    domainAxis = new DomainAxis(resources);

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
	  lineGraph.draw(new Interval(domainStart, domainEnd));
	  domainAxis.draw(new Interval(domainStart, domainEnd));
	}
      });    

    // Hookup window bounds observer to the main timeline model and
    // redraw the overview graphs based on changes to the model.
    overviewModel.addWindowBoundsObserver(
      new WindowBoundsObserver() {
	public void onWindowBoundsChange(double domainStart, 
	    double domainEnd) {
	  //Log.info("redraw to: " + domainStart + ", " + domainEnd);
	  overviewGraph.draw(new Interval(domainStart, domainEnd));
	}
      });    

    add(lineGraph);
    add(domainAxis);
    add(overviewGraph);
  }

  public void fillWidth() {
    // FIXME: use getDomainExtrema for each dataset instead 

    Interval widestDomain = lineGraph.calcWidestDomain();
    Log.info("widest domain: " + widestDomain);
    mainModel.updateBounds(widestDomain.getStart(),
        widestDomain.getEnd());
    overviewModel.updateBounds(widestDomain.getStart(),
        widestDomain.getEnd());
  }
  
//   private void sinkEvents() {
//     MainEventListener mainEventListener = new MainEventListener();
//     lineGraph.addMouseWheelListener(mainEventListener);
//   }

  public void addDataset(Dataset ds, GraphUiProps graphUiProps) {
    lineGraph.addDataset(ds, graphUiProps);
    
    GraphUiProps uiProps = new GraphUiProps(Color.LIGHT_GREY,
        Color.BLACK, 0);
    overviewGraph.addDataset(ds, uiProps);
  }

  public void redraw() {
    Interval mainDomain = new Interval(mainModel.getLeftBound(),
         mainModel.getRightBound());
    lineGraph.draw(mainDomain);
    domainAxis.draw(mainDomain);
    overviewGraph.draw(new Interval(overviewModel.getLeftBound(),
        overviewModel.getRightBound()));
  }

}
