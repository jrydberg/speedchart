package org.gwt.speedchart.client.graph.axis;

import org.gwt.speedchart.client.util.Interval;

public interface RangeTickFormatter {

  int getMaxTickLabelHeight(String tickLabelClassName);

  double[] calcTickPositions(Interval visRange, int maxTicksForScreen);

  String format(double dataY);
}
