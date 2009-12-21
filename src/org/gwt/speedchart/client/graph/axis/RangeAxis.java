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
 * @author Johan Rydberg &lt;johan.rydberg@gmail.com&gt;
 */

public class RangeAxis extends AbstractAxis {

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
    @Source("resources/RangeAxis.css")
    @Strict
    Css rangeAxisCss();
  }

  private final Resources resources;

  private Element[] labelElements;

  private final Canvas canvas = new Canvas(40, 400);

  private static final int SUB_TICK_WIDTH = 3;

  private static final int TICK_WIDTH = 6;

  private RangeTickFormatter tickFormatter = 
      new DefaultRangeTickFormatter();

  public RangeAxis(Resources resources) {
    this.resources = resources;
    setStyleName(resources.rangeAxisCss().axis());
    getElement().appendChild(canvas.getElement());
  }

  /**
   * Draw range.
   */
  public void draw(Interval visRange) {
    final int screenHeight = getElement().getOffsetHeight();
    final double rangeWidth = visRange.length();

    final double labelHeight = tickFormatter
        .getMaxTickLabelHeight(resources.rangeAxisCss().tickLabel());
    final double labelHeightDiv2 = labelHeight / 2.0;

    int maxTicksForScreen = (int) (screenHeight / labelHeight);
    double[] tickPositions = tickFormatter.calcTickPositions(visRange,
        maxTicksForScreen);

    allocLabels(maxTicksForScreen);
    canvas.clear();

    int labelIndex = 0;
    for (int i = 0; i < tickPositions.length; i++) {
      double tickPosition = tickPositions[i];
      if (tickPosition >= visRange.getStart() 
	  && tickPosition <= visRange.getEnd()) {
	int screenPos = rangeToScreenY(screenHeight, visRange, 
	    tickPosition);
	drawTick(screenPos, TICK_WIDTH);
	drawTickLabel(labelIndex, screenPos - (int) labelHeightDiv2, 
	    tickFormatter.format(tickPosition));
	labelIndex++;
      }
    }

    cleanUnusedLabels(labelIndex);
  }

  private int rangeToScreenY(int screenHeight, Interval visRange, 
			    double dataY) {
    return screenHeight - (int) (visRange.getRatioFromPoint(dataY) 
        * screenHeight);
  }

  private void drawTickLabel(int labelIndex, int screenPos, String tickLabel) {
    Element labelElement = getLabelElement(labelIndex, 
        resources.rangeAxisCss().tickLabel());
    labelElement.getStyle().setPropertyPx("top", screenPos);
    labelElement.setInnerText(tickLabel);
  }

  private void drawTick(int screenPos, int tickWidth) {
    canvas.setFillStyle(Color.BLACK);
    canvas.fillRect(canvas.getCoordWidth() - tickWidth, screenPos, 
        tickWidth, 2);
  }
}