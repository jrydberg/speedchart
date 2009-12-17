/*
 * This file is based on original work by Timepedia Chronoscope.
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

package org.gwt.speedchart.client.graph.axis;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Document;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Strict;
import com.google.gwt.graphics.client.Canvas;
import com.google.gwt.graphics.client.Color;
import com.google.gwt.user.client.ui.FlowPanel;
import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.Interval;


/**
 * @author Timepedia
 * @author Johan Rydberg &lt;johan.rydberg@gmail.com&gt;
 */

public class DomainAxis extends FlowPanel {

  /**
   * Css stylenames.
   */
  public interface Css extends CssResource {
    String axis();

    String tickLabel();
  }

  /**
   * Resources for {@link TimeLineGraph}.
   */
  public interface Resources extends ClientBundle {
    @Source("resources/DomainAxis.css")
    @Strict
    Css domainAxisCss();
  }

  private Resources resources;

  private Element[] labelElements;

  private final Canvas canvas = new Canvas(1000, 20);
  
  public DomainAxis(Resources resources) {
    this.resources = resources;
    getElement().appendChild(canvas.getElement());
    setStyleName(resources.domainAxisCss().axis());
    getElement().getStyle().setProperty("position", "relative");
  }

  public void draw(Interval domain) {
    final double screenWidth = getElement().getOffsetWidth();
    final double domainWidth = domain.length();

    final double labelWidth = 50;
    final double labelWidthDiv2 = labelWidth / 2.0;
    final int maxTicksForScreen = 5;
    final int idealTickStep = (int) domainWidth / maxTicksForScreen;
    
    boolean stillEnoughSpace = true; // enough space to draw another tick+label?
    boolean isFirstTick = true;
    double prevTickScreenPos = 0.0;
    int actualTickStep = 0;
    int tickIndex = 0;

    /*
    log("idealTickStep=" + idealTickStep +
        "; maxTicks=" + maxTicksForScreen +
        "; domainStart=" + (long)plot.getDomain().getStart() +
        "; domainLen=" + (long)plot.getDomain().length() +
        "; quantizedDomainValue=" + (long)tickFormatter.getTickDomainValue());
    */

    if (labelElements == null || labelElements.length < maxTicksForScreen) {
      if (labelElements != null) {
	for (int i = 0; i < labelElements.length; i++) {
	  Element labelElement = labelElements[i];
	  if (labelElement != null) {
	    getElement().removeChild(labelElements[i]);
	  }
	}
	labelElements = null;
      }

      labelElements = new Element[maxTicksForScreen];
    }

    double currentTickValue = domain.getStart();

    canvas.clear();
    while (stillEnoughSpace && tickIndex < maxTicksForScreen) {
      double tickScreenPos = domainToScreenX(currentTickValue, screenWidth, domain);
      stillEnoughSpace = (tickScreenPos + labelWidthDiv2 < screenWidth);

      /*
      log("tickScreenPos=" + tickScreenPos + 
          "; tickDomainValue=" + (long)tickFormatter.getTickDomainValue() +
          "; boundsRightX=" + boundsRightX);
      */

      if (stillEnoughSpace) {
        // Quantized tick date may have gone off the left edge; need to guard
        // against this case.
	String tickLabel = Double.toString(currentTickValue);
	drawTick(tickScreenPos, 6);
	drawTickLabel(tickIndex, tickScreenPos, tickLabel, labelWidth);
      }

      currentTickValue += idealTickStep;
      tickIndex++;
    }

    if (tickIndex < labelElements.length) {
      for (int i = tickIndex; i < labelElements.length; i++) {
	Element labelElement = labelElements[i];
	if (labelElement != null) {
	  getElement().removeChild(labelElement);
	  labelElements[i] = null;
	}
      }
    }
  }

  private double domainToScreenX(double dataX, double screenWidth, Interval domain) {
    return domain.getRatioFromPoint(dataX) * screenWidth;
  }

  private void drawTick(double ux, int tickLength) {
    canvas.setFillStyle(Color.RED);
    canvas.fillRect(ux, 0, 2, tickLength);
  }

  private void drawTickLabel(int tickIndex, double ux, String tickLabel, 
        double tickLabelWidth) {
    if (labelElements[tickIndex] == null) {
      Element labelElement = Document.get().createDivElement();
      labelElement.setClassName(resources.domainAxisCss().tickLabel());
      getElement().appendChild(labelElement);
      labelElements[tickIndex] = labelElement;
    }

    Element labelElement = labelElements[tickIndex];
    labelElement.getStyle().setPropertyPx("left",
        (int) (ux - tickLabelWidth / 2));
    labelElement.setInnerText(tickLabel);
  }

}