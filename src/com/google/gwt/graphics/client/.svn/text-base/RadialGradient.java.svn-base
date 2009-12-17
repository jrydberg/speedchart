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
package com.google.gwt.graphics.client;

import com.google.gwt.dom.client.Element;

/**
 *  Default deferred binding of Gradient Factory will create instances of this class
 *  for RadialGradients.
 */
public class RadialGradient extends CanvasGradient {

  public RadialGradient(double x0, double y0, double r0,
      double x1, double y1, double r1, Element domElement) {
    createNativeGradientObject(x0,y0,r0,x1,y1,r1, domElement);
  }

  public void addColorStop(double offset, Color color) {
    addNativeColorStop(offset,color.toString());
  }

  private native void addNativeColorStop(double offset, String color) /*-{
    (this.@com.google.gwt.graphics.client.CanvasGradient::nativeGradient).addColorStop(offset,color);
  }-*/;

  private native void createNativeGradientObject(double x0, double y0, double r0, double x1, double y1, double r1, Element c) /*-{
    var ctx = c.getContext('2d');
    var gradient = ctx.createRadialGradient(x0,y0,r0,x1,y1,r1);
    this.@com.google.gwt.graphics.client.CanvasGradient::setNativeGradient(Lcom/google/gwt/core/client/JavaScriptObject;)(gradient);
  }-*/;
}
