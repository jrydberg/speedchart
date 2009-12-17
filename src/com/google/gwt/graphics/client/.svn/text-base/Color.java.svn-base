/*
 * Copyright 2009 Google Inc.
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

/**
 * Simple Wrapper specifying a color in RGB format. Provides various methods for
 * converting to String representations of the specified color for easy
 * compatibility with various APIs
 */
public class Color {

  /*
   * Some basic color strings that are often used for the web. Compiler should
   * optimize these out.
   */
  public static final Color GREEN = new Color("#23ef24");
  public static final Color RED = new Color("#ff0000");
  public static final Color BLUE = new Color("#318ce0");
  public static final Color BLACK = new Color("#000000");
  public static final Color ORANGE = new Color("#f88247");
  public static final Color GREY = new Color("#a9a9a9");
  public static final Color LIGHTGREY = new Color("#eeeeee");
  public static final Color DARKGREY = new Color("#333333");
  public static final Color YELLOW = new Color("yellow");
  public static final Color PINK = new Color("#ff00ff");
  public static final Color BLUEVIOLET = new Color("#8a2be2");
  public static final Color CYAN = new Color("#5fa2e0");
  public static final Color PEACH = new Color("#ffd393");
  public static final Color WHITE = new Color("#ffffff");
  public static final Color INDIAN_RED = new Color("#cd5c5c");
  public static final Color SKY_BLUE = new Color("#c6defa");
  public static final Color LIGHT_GREY = new Color("#aaaaaa");
  public static final Color LIGHTGREEN = new Color("#67ef68");
  public static final Color LIMEGREEN = new Color("#aff616");
  public static final Color DARKGREEN = new Color("#52b453");
  public static final Color MIDNIGHT_BLUE = new Color("#7483aa");
  public static final Color PALE_GREEN = new Color("#98FB98");
  public static final Color BROWN = new Color("#ab8f38");

  private String colorStr = "";

  public Color(int r, int g, int b) {
    this.colorStr = "rgb(" + r + "," + g + "," + b + ")";
  }

  public Color(int r, int g, int b, float a) {
    this.colorStr = "rgba(" + r + "," + g + "," + b + "," + a + ")";
  }

  // We allow a user to supply a valid CSS3String
  // ... or Browser will not know what to do with it!
  public Color(String colorStr) {
    this.colorStr = colorStr;
  }

  public String toString() {
    return this.colorStr;
  }
}
