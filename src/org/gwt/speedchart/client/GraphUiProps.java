/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.speedchart.client;

import com.google.gwt.graphics.client.Color;

/**
 * Properties object for look and feel of a graph.
 */
public class GraphUiProps {
  private final Color graphColor;
  
  private final Color strokeColor;
  
  public GraphUiProps(Color graphColor, Color strokeColor,
      double yAxisScaleCap) {
    this.graphColor = graphColor;
    this.strokeColor = strokeColor;
  }

  public boolean isAutoZoomVisibleRange() {
    return true;
  }

  public Color getGraphColor() {
    return graphColor;
  }
  
  public Color getStrokeColor() {
    return strokeColor;
  }

}
