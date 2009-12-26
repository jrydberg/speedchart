package org.gwt.speedchart.client.graph;

import org.gwt.speedchart.client.ChartUiProps;
import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.data.tuple.Tuple2D;


public class LineGraph<T extends Tuple2D> extends AbstractGraph<T> {

  public LineGraph(ChartUiProps chartUiProps) {
    super(chartUiProps);
  }
}
