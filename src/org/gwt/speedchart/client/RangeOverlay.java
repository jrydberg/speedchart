package org.gwt.speedchart.client;

import org.gwt.speedchart.client.util.Interval;


public class RangeOverlay extends AbstractOverlay {

  public RangeOverlay(Interval interval, OverlayUiProps props) {
    super(interval, props);
  }

  public RangeOverlay(double value, OverlayUiProps props) {
    super(value, props);
  }

}
