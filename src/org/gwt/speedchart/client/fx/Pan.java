package org.gwt.speedchart.client.fx;

import com.google.gwt.animation.client.Animation;
import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.graph.AbstractGraph;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;

import java.util.List;
import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;


public class Pan {

  private final TimelineModel model;

  private class MouseHandler implements MouseDownHandler,
      MouseUpHandler, MouseMoveHandler {
    
    boolean isDragging = false;
    int startX;

    public void onMouseDown(MouseDownEvent event) {
      isDragging = true;
      startX = event.getX();
    }

    public void onMouseUp(MouseUpEvent event) {
      isDragging = false;
    }

    public void onMouseMove(MouseMoveEvent event) {
      if (isDragging) {
	int deltaX = startX - event.getX();
	startX = event.getX();

	double deltaDomain = graph.getDomainWidthByUserX(Math.abs(deltaX));
	if (deltaX < 0) {
	  deltaDomain = -deltaDomain;
	}

	Log.info("mouse moved: " + deltaDomain);

	model.updateBounds(model.getLeftBound() + deltaDomain,
	    model.getRightBound() + deltaDomain);
      }
    }
  }

  private final MouseHandler mouseHandler = new MouseHandler();
  
  private final AbstractGraph graph;

  public Pan(TimelineModel model, AbstractGraph sink) {
    this.model = model;
    sink.addMouseDownHandler(mouseHandler);
    sink.addMouseUpHandler(mouseHandler);
    sink.addMouseMoveHandler(mouseHandler);
    this.graph = sink;
  }

}

