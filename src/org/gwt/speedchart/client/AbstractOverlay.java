package org.gwt.speedchart.client;

import org.gwt.speedchart.client.util.Interval;


public abstract class AbstractOverlay implements Overlay {

  private final Interval interval;

  private final OverlayUiProps props;

  public AbstractOverlay(Interval interval, OverlayUiProps props) {
    this.interval = interval;
    this.props = props;
  }

  public AbstractOverlay(double value, OverlayUiProps props) {
    this.interval = new Interval(value, value);
    this.props = props;
  }

  public Interval getInterval() {
    return this.interval;
  }

  public OverlayUiProps getUiProps() {
    return this.props;
  }

}
