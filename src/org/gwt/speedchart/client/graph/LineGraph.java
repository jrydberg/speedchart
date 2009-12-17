package org.gwt.speedchart.client.graph;

import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.data.tuple.Tuple2D;


public class LineGraph<T extends Tuple2D> extends AbstractGraph<T> {

  public LineGraph(int width, int height) {
    super(width, height);
  }
}
