package org.gwt.speedchart.client;

import org.gwt.speedchart.client.util.Interval;

/**
 * Properties object for look and feel of a chart.
 */
public class ChartUiProps {

  private Interval visRange;

  private boolean drawBorders; 

  public boolean isAutoZoomRangeTop() {
    return (visRange != null && Double.isNaN(visRange.getEnd()));
  }

  public boolean isAutoZoomRangeBottom() {
    return (visRange != null && Double.isNaN(visRange.getStart()));
  }

  public boolean isDrawBorders() {
    return drawBorders;
  }

  /**
   * Return visible range interval or {@code null} if no visible range
   * was set.
   */
  public Interval getVisibleRangeInterval() {
    return visRange;
  }

  /**
   * Set the visible range to specified interval.  Automaticly
   * disables autozoom for the range.  If any of the endpoints
   * are NaN they are ignored.
   */
  public void setVisibleRangeInterval(Interval visRange) {
    this.visRange = visRange;
  }

  /**
   * Set whether the chart should have a thin border below and next to
   * the range axis.
   */ 
  public void setDrawBorders(boolean drawBorders) {
    this.drawBorders = drawBorders;
  }

}
