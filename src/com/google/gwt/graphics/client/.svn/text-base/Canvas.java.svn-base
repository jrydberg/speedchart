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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * 2D Graphics API. Native canvas implementation only. API mimics functionality
 * found in the JavaScript canvas API (see <a
 * href="http://developer.mozilla.org/en/docs/Canvas_tutorial">canvas
 * tutorial</a>).
 */
public class Canvas {
  @SuppressWarnings("unused")
  private JavaScriptObject canvasContext = null;
  private int coordHeight = 0;
  private int coordWidth = 0;
  private Element element;

  /**
   * Creates a Canvas element.
   * 
   * <p>
   * Screen size of canvas in pixels defaults to <b>300x150</b> pixels.
   * </p>
   */
  public Canvas() {
    element = createElement();
  }

  /**
   * Creates a Canvas object that adopts an external element.
   * 
   * <p>
   * Screen size of canvas in pixels defaults to whatever was configured for the
   * canvas element we are adopting.
   * </p>
   * 
   * @param element the externally created canvas element
   */
  public Canvas(Element element) {
    this.element = element;
    setGfxContext(element);
  }

  /**
   * Creates a Canvas element.
   * 
   * <p>
   * Screen size of canvas in pixels defaults to the coordinate space dimensions
   * for this constructor.
   * </p>
   * 
   * @param coordX the size of the coordinate space in the x direction
   * @param coordY the size of the coordinate space in the y direction
   */
  public Canvas(int coordX, int coordY) {
    element = createElement();
    setCoordSize(coordX, coordY);
  }

  /**
   * Creates a Canvas element.
   * 
   * <p>
   * Different coordinate spaces and pixel spaces will cause aliased scaling.
   * Use <code>scale(double,double)</code> and consistent coordinate and pixel
   * spaces for better results.
   * </p>
   * 
   * @param coordX the size of the coordinate space in the x direction
   * @param coordY the size of the coordinate space in the y direction
   * @param pixelX the CSS width in pixels of the canvas element
   * @param pixelY the CSS height in pixels of the canvas element
   */
  public Canvas(int coordX, int coordY, int pixelX, int pixelY) {
    element = createElement();
    setPixelWidth(pixelX);
    setPixelHeight(pixelY);

    setCoordSize(coordX, coordY);
  }

  /**
   * Draws an arc. If the context has a non-empty path, then the method must add
   * a straight line from the last point in the path to the start point of the
   * arc.
   * 
   * @param x center X coordinate
   * @param y center Y coordinate
   * @param radius radius of drawn arc
   * @param startAngle angle measured from positive X axis to start of arc CW
   * @param endAngle angle measured from positive X axis to end of arc CW
   * @param antiClockwise direction that the arc line is drawn
   */
  public native void arc(double x, double y, double radius, double startAngle,
      double endAngle, boolean antiClockwise) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).arc(x,y,radius,startAngle,endAngle,antiClockwise);
  }-*/;

  /**
   * Erases the current path and prepares it for a new path.
   */
  public native void beginPath() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).beginPath();
  }-*/;

  /**
   * Clears the entire canvas.
   */
  public void clear() {
    // we used local references instead of looking up the attributes
    // on the DOM element
    this.clearRect(0, 0, coordWidth, coordHeight);
  }

  public native void clearRect(double startX, double startY, double width,
      double height) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).clearRect(startX,startY,width,height);
  }-*/;

  /**
   * Sets the current path as a clipping region.
   */
  public native void clip() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).clip();
  }-*/;

  /**
   * Closes the current path. "Closing" simply means that a line is drawn from
   * the last element in the path back to the first.
   */
  public native void closePath() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).closePath();
  }-*/;

  /**
   * Creates a LinearGradient Object for use as a fill or stroke style.
   * 
   * @param x0 x coord of start point of gradient
   * @param y0 y coord of start point of gradient
   * @param x1 x coord of end point of gradient
   * @param y1 y coord of end point of gradient
   * @return returns the CanvasGradient
   */
  public CanvasGradient createLinearGradient(double x0, double y0, double x1,
      double y1) {
    return new LinearGradient(x0, y0, x1, y1, getElement());
  }

  /**
   * Creates a RadialGradient Object for use as a fill or stroke style.
   * 
   * @param x0 x coord of origin of start circle
   * @param y0 y coord of origin of start circle
   * @param r0 radius of start circle
   * @param x1 x coord of origin of end circle
   * @param y1 y coord of origin of end circle
   * @param r1 radius of the end circle
   * @return returns the CanvasGradient
   */
  public CanvasGradient createRadialGradient(double x0, double y0, double r0,
      double x1, double y1, double r1) {
    return new RadialGradient(x0, y0, r0, x1, y1, r1, getElement());
  }

  /**
   * 
   * Does nothing if the context's path is empty. Otherwise, it connects the
   * last point in the path to the given point <b>(x, y)</b> using a cubic
   * BŽzier curve with control points <b>(cp1x, cp1y)</b> and <b>(cp2x,
   * cp2y)</b>. Then, it must add the point <b>(x, y)</b> to the path.
   * 
   * This function corresponds to the
   * <code>bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y)</code> method in canvas
   * element Javascript API.
   * 
   * @param cp1x x coord of first Control Point
   * @param cp1y y coord of first Control Point
   * @param cp2x x coord of second Control Point
   * @param cp2y y coord of second Control Point
   * @param x x coord of point
   * @param y x coord of point
   */
  public native void cubicCurveTo(double cp1x, double cp1y, double cp2x,
      double cp2y, double x, double y) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).bezierCurveTo(cp1x,cp1y,cp2x,cp2y,x,y);
  }-*/;

  /**
   * Draws an input image to a specified position on the canvas. Size defaults
   * to the default dimensions of the image.
   * 
   * @param img the image to be drawn
   * @param offsetX x coord of the top left corner in the destination space
   * @param offsetY y coord of the top left corner in the destination space
   */
  public void drawImage(ImageHandle img, double offsetX, double offsetY) {
    drawImage(img, offsetX, offsetY, img.getWidth(), img.getHeight());
  }

  /**
   * Draws an input image at a given position on the canvas. Resizes image
   * according to specified width and height.
   * 
   * <p>
   * We recommend that the pixel and coordinate spaces be the same to provide
   * consistent positioning and scaling results
   * </p>
   * 
   * @param img The image to be drawn
   * @param offsetX x coord of the top left corner in the destination space
   * @param offsetY y coord of the top left corner in the destination space
   * @param width the size of the image in the destination space
   * @param height the size of the image in the destination space
   */
  public void drawImage(ImageHandle img, double offsetX, double offsetY,
      double width, double height) {

    drawImage(img, 0, 0, img.getWidth(), img.getHeight(), offsetX, offsetY,
        width, height);
  }

  public native void drawImage(ImageHandle img, double sourceX, double sourceY,
      double sourceWidth, double sourceHeight, double destX, double destY,
      double destWidth, double destHeight) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).drawImage(img,sourceX,sourceY,sourceWidth,sourceHeight,destX,destY,destWidth,destHeight);
  }-*/;

  /**
   * Fills the current path according to the current fillstyle.
   */
  public native void fill() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).fill();
  }-*/;

  /**
   * Fills a rectangle of the specified dimensions, at the specified start
   * coords, according to the current fillstyle.
   * 
   * @param startX x coord of the top left corner in the destination space
   * @param startY y coord of the top left corner in the destination space
   * @param width destination width of image
   * @param height destination height of image
   */
  public native void fillRect(double startX, double startY, double width,
      double height) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).fillRect(startX,startY,width,height);
  }-*/;

  public int getCoordHeight() {
    return coordHeight;
  }

  public int getCoordWidth() {
    return coordWidth;
  }

  public Element getElement() {
    return element;
  }

  /**
   * Adds a line from the last point in the current path to the point defined by
   * x and y.
   * 
   * @param x x coord of point
   * @param y y coord of point
   */
  public native void lineTo(double x, double y) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).lineTo(x,y);
  }-*/;

  /**
   * Makes the last point in the current path be <b>(x,y)</b>.
   * 
   * @param x x coord of point
   * @param y y coord of point
   */
  public native void moveTo(double x, double y) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).moveTo(x,y);
  }-*/;

  /**
   * Does nothing if the context has an empty path. Otherwise it connects the
   * last point in the path to the given point <b>(x, y)</b> using a quadratic
   * BŽzier curve with control point <b>(cpx, cpy)</b>, and then adds the given
   * point <b>(x, y)</b> to the path.
   * 
   * @param cpx x coord of the control point
   * @param cpy y coord of the control point
   * @param x x coord of the point
   * @param y y coord of the point
   */

  public native void quadraticCurveTo(double cpx, double cpy, double x, double y) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).quadraticCurveTo(cpx,cpy,x,y);
  }-*/;

  /**
   * Adds a rectangle to the current path, and closes the path.
   * 
   * @param x x coord of the top left corner of the rectangle
   * @param y y coord of the top left corner of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public native void rect(double x, double y, double width, double height) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).rect(x,y,width,height);
  }-*/;

  /**
   * Convenience function for resizing the canvas with consistent coordinate and
   * screen pixel spaces. Equivalent to doing:
   * 
   * <pre><code>
   * canvas.setPixelHeight(height);
   * canvas.setPixelWidth(width);
   * </code></pre>
   * 
   * @param width
   * @param height
   */
  public void resize(int width, int height) {
    setPixelHeight(height);
    setPixelWidth(width);
  }

  /**
   * Restores the last saved context from the context stack.
   */
  public native void restoreContext() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).restore();
  }-*/;

  /**
   * Adds a rotation of the specified angle to the current transform.
   * 
   * @param angle the angle to rotate by, <b>in radians</b>
   */
  public native void rotate(double angle) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).rotate(angle);
  }-*/;

  /**
   * Saves the current context to the context stack.
   */
  public native void saveContext() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).save();
  }-*/;

  /**
   * Adds a scale transformation to the current transformation matrix.
   * 
   * @param x ratio that we must scale in the X direction
   * @param y ratio that we must scale in the Y direction
   */
  public native void scale(double x, double y) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).scale(x,y);
  }-*/;

  /**
   * Sets the coordinate space of the Canvas.
   * <p>
   * This will erase the canvas contents!
   * </p>
   * 
   * @param width the size of the x component of the coordinate space
   * @param height the size of the y component of the coordinate space
   */
  public void setCoordSize(int width, int height) {
    setCoordWidth(width);
    setCoordHeight(height);
  }

  public void setElement(Element element) {
    this.element = element;
  }

  /**
   * Set the current Fill Style to the specified color gradient.
   * 
   * @param gradient {@link CanvasGradient}
   */
  public native void setFillStyle(CanvasGradient gradient) /*-{
    var gradObj = gradient.@com.google.gwt.graphics.client.CanvasGradient::getObject()();
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).fillStyle = gradObj;
  }-*/;

  /**
   * Set the current Fill Style to the specified color.
   * 
   * @param color {@link Color}
   */
  public void setFillStyle(Color color) {
    setFillStyle(color.toString());
  }

  /**
   * Set the global transparency to the specified alpha.
   * 
   * @param alpha alpha value
   */
  public native void setGlobalAlpha(double alpha) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).globalAlpha = alpha;
  }-*/;

  /**
   * Sets the current context's linewidth. Line width is the thickness of a
   * stroked line.
   * 
   * @param width the width of the stroked line.
   */
  public native void setLineWidth(double width) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).lineWidth = width;
  }-*/;

  /**
   * Sets the height of the canvas in pixels. Also defines the height of the
   * coordinate space as an integer value.
   * 
   * @param height the height of the canvas
   */
  public void setPixelHeight(int height) {
    setPixelHeight(getElement(), height);
  }

  /**
   * Sets the CSS property in pixels for the canvas.
   * 
   * @param width width of the canvas
   */
  public void setPixelWidth(int width) {
    setPixelWidth(getElement(), width);
  }

  /**
   * Set the current Stroke Style to the specified color gradient.
   * 
   * @param gradient {@link CanvasGradient}
   */
  public native void setStrokeStyle(CanvasGradient gradient) /*-{
    var gradObj = gradient.@com.google.gwt.graphics.client.CanvasGradient::getObject()();
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).strokeStyle = gradObj;
  }-*/;

  /**
   * Set the current Stroke Style to the specified color.
   * 
   * @param color {@link Color}
   */
  public void setStrokeStyle(Color color) {
    setStrokeStyle(color.toString());
  }

  /**
   * Strokes the current path according to the current stroke style.
   */
  public native void stroke() /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).stroke();
  }-*/;

  /**
   * Strokes a rectangle defined by the supplied arguments.
   * 
   * @param startX x coord of the top left corner
   * @param startY y coord of the top left corner
   * @param width width of the rectangle
   * @param height height of the rectangle
   */
  public native void strokeRect(double startX, double startY, double width,
      double height) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).strokeRect(startX,startY,width,height);
  }-*/;

  /**
   * <code>The transform(m11, m12, m21, m22, dx, dy)</code> method must multiply
   * the current transformation matrix with the input matrix. Input described
   * by:
   * 
   * <pre> m11   m21   dx
   * m12   m22   dy
   * 0      0     1 
   *</pre>
   * 
   * @param m11 top left cell of 2x2 rotation matrix
   * @param m12 top right cell of 2x2 rotation matrix
   * @param m21 bottom left cell of 2x2 rotation matrix
   * @param m22 bottom right cell of 2x2 rotation matrix
   * @param dx Translation in X direction
   * @param dy Translation in Y direction
   */
  public native void transform(double m11, double m12, double m21, double m22,
      double dx, double dy) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).transform(m11,m12,m21,m22,dx,dy);
  }-*/;

  /**
   * Applies a translation (linear shift) by x in the horizontal and by y in the
   * vertical.
   * 
   * @param x amount to shift in the x direction
   * @param y amount to shift in the y direction
   */
  public native void translate(double x, double y) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).translate(x,y);
  }-*/;

  private Element createElement() {
    Element elem = Document.get().createElement("canvas");
    setGfxContext(elem);
    return elem;
  }

  // This is called from JSNI function. Ignore warning.
  @SuppressWarnings("unused")
  private void setCanvasContext(JavaScriptObject ctx) {
    this.canvasContext = ctx;
  }

  private void setCoordHeight(int height) {
    coordHeight = height;
    getElement().setPropertyInt("height", height);
  }

  private void setCoordWidth(int width) {
    coordWidth = width;
    getElement().setPropertyInt("width", width);
  }

  private native void setFillStyle(String colorStr) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).fillStyle = colorStr;
  }-*/;

  private native void setGfxContext(Element e) /*-{
    this.@com.google.gwt.graphics.client.Canvas::setCanvasContext(Lcom/google/gwt/core/client/JavaScriptObject;)(e.getContext('2d'));
  }-*/;

  private void setPixelHeight(Element elem, int height) {
    elem.getStyle().setPropertyPx("height", height);
  }

  private void setPixelWidth(Element elem, int width) {
    elem.getStyle().setPropertyPx("width", width);
  }

  private native void setStrokeStyle(String colorStr) /*-{
    (this.@com.google.gwt.graphics.client.Canvas::canvasContext).strokeStyle = colorStr;
  }-*/;

}
