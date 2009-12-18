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
import org.gwt.speedchart.client.util.MathUtil;

import org.gwt.speedchart.client.graph.domain.TickFormatter;
import org.gwt.speedchart.client.graph.domain.TickFormatterFactory;
import org.gwt.speedchart.client.graph.domain.IntTickFormatterFactory;
import org.gwt.speedchart.client.graph.domain.DateTickFormatterFactory;

import com.allen_sauer.gwt.log.client.Log;


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

  private final Resources resources;

  private Element[] labelElements;

  private final Canvas canvas = new Canvas(1000, 20);
  
  private static final int SUB_TICK_HEIGHT = 3;

  private static final int TICK_HEIGHT = 6;

  private double minTickSize = -1;

  private TickFormatterFactory tickFormatterFactory
      = new IntTickFormatterFactory();

  public DomainAxis(Resources resources) {
    this.resources = resources;
    getElement().appendChild(canvas.getElement());
    setStyleName(resources.domainAxisCss().axis());
    getElement().getStyle().setProperty("position", "relative");
  }

  public void draw(Interval domain) {
    final double screenWidth = getElement().getOffsetWidth();

    final double domainWidth = domain.length();
    TickFormatter tickFormatter = getBestFormatter(domainWidth);

    final double boundsRightX = screenWidth;
    final double labelWidth = tickFormatter
        .getMaxTickLabelWidth(resources.domainAxisCss().tickLabel());
    final double labelWidthDiv2 = labelWidth / 2.0;
    final int maxTicksForScreen = calcMaxTicksForScreen(
        screenWidth, domainWidth, tickFormatter);
    final int idealTickStep = tickFormatter
        .calcIdealTickStep(domainWidth, maxTicksForScreen);
    //Log.info("dw=" + (long)domainWidth + "; maxTicks=" + maxTicksForScreen + 
    //    "; idealStep=" + idealTickStep);
    tickFormatter
      .resetToQuantizedTick(domain.getStart(), idealTickStep);

    boolean stillEnoughSpace = true; // enough space to draw another tick+label?
    boolean isFirstTick = true;
    double prevTickScreenPos = 0.0;
    int actualTickStep = 0;
    int tickIndex = 0;

//      Log.info("idealTickStep=" + idealTickStep +
//          "; maxTicks=" + maxTicksForScreen +
//          "; domainStart=" + (long)domain.getStart() +
//          "; domainLen=" + (long)domain.length() +
//          "; quantizedDomainValue=" + (long)tickFormatter.getTickDomainValue());

    if (labelElements == null || labelElements.length < maxTicksForScreen) {
      if (labelElements != null) {
	for (int i = 0; i < labelElements.length; i++) {
	  Element labelElement = labelElements[i];
	  if (labelElement != null) {
	    getElement().removeChild(labelElement);
	  }
	}
	labelElements = null;
      }

      labelElements = new Element[maxTicksForScreen];
    }

    canvas.clear();

    while (stillEnoughSpace && tickIndex < maxTicksForScreen) {
      double tickScreenPos = this.domainToScreenX(
          tickFormatter.getTickDomainValue(), screenWidth, domain);
      stillEnoughSpace = (tickScreenPos + labelWidthDiv2 < screenWidth);

//       Log.info("tickScreenPos=" + tickScreenPos + 
// 	       "; tickDomainValue=" + (long)tickFormatter.getTickDomainValue() +
// 	       "; boundsRightX=" + boundsRightX);

      if (stillEnoughSpace) {
        // Quantized tick date may have gone off the left edge; need to guard
        // against this case.
	if (tickScreenPos > labelWidthDiv2) {
	  String tickLabel = tickFormatter.formatTick();
	  drawTick(tickScreenPos, TICK_HEIGHT);
	  drawTickLabel(tickIndex, tickScreenPos, tickLabel, labelWidth);
	  tickIndex++;
	}
      }

      // Draw auxiliary sub-ticks
      if (!isFirstTick) {
        int subTickStep = tickFormatter.getSubTickStep(actualTickStep);
        if (subTickStep > 1) {
          double auxTickWidth = (tickScreenPos - prevTickScreenPos)
	      / subTickStep;
          double auxTickPos = prevTickScreenPos + auxTickWidth;
          for (int i = 0; i < subTickStep - 1; i++) {
            if (MathUtil.isBounded(auxTickPos, 0, boundsRightX)) {
              drawTick(auxTickPos, SUB_TICK_HEIGHT);
            }
            auxTickPos += auxTickWidth;
          }
        }
      }

      actualTickStep = tickFormatter.incrementTick(idealTickStep);
      prevTickScreenPos = tickScreenPos;
      isFirstTick = false;
    }

    if (tickIndex < labelElements.length) {
//       Log.info("tickIndex=" + tickIndex
// 	       + "; labelElements.length=" + labelElements.length);
      for (int i = tickIndex; i < labelElements.length; i++) {
	Element labelElement = labelElements[i];
	if (labelElement != null) {
	  getElement().removeChild(labelElement);
	  labelElements[i] = null;
	}
      }
    }
  }

  public TickFormatter getBestFormatter(double domainWidth) {
    return tickFormatterFactory.findBestFormatter(domainWidth);
  }

  public double getMinimumTickSize() {
    if (minTickSize == -1) {
      TickFormatter leafFormatter = tickFormatterFactory.getLeafFormatter();
      minTickSize = leafFormatter.getTickInterval();
    }
    return minTickSize;
  }

  public TickFormatterFactory getTickFormatterFactory() {
    return this.tickFormatterFactory;
  }

  public void setTickFormatterFactory(
      TickFormatterFactory tickFormatterFactory) {
    ArgChecker.isNotNull(tickFormatterFactory, "tickFormatterFactory");
    this.tickFormatterFactory = tickFormatterFactory;
  }

  /**
   * Calculates the maximum number of ticks that can visually fit on the domain
   * axis given the visible screen width and the max width of a tick label for
   * the specified {@link DateTickFormatter}.
   */
  private int calcMaxTicksForScreen(double screenWidth, double domainWidth, 
      TickFormatter tickFormatter) {

    // Needed to round screen width due to tiny variances that were causing the 
    // result of this method to fluctuate by +/- 1.
    //double screenWidth = Math.round(domainToScreenWidth(domainWidth, bounds));

    double maxLabelWidth = 15 + tickFormatter
      .getMaxTickLabelWidth(resources.domainAxisCss().tickLabel());

    return (int) (screenWidth / maxLabelWidth);
  }



  private double domainToScreenX(double dataX, double screenWidth, Interval domain) {
    return domain.getRatioFromPoint(dataX) * screenWidth;
  }

  private void drawTick(double ux, int tickLength) {
    canvas.setFillStyle(Color.BLACK);
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