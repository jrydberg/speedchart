package org.gwt.speedchart.client;

import org.gwt.speedchart.client.util.Interval;


public class DomainOverlay extends AbstractOverlay {

  public DomainOverlay(Interval interval, OverlayUiProps props) {
    super(interval, props);
  }

  public DomainOverlay(double value, OverlayUiProps props) {
    super(value, props);
  }

}
