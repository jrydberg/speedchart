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

/**
 * If I say an image element is a JavascriptObject... then it is!
 */
public class ImageHandle extends JavaScriptObject {

  protected ImageHandle() {
    super();
  }

  public final native int getHeight()/*-{
    return this.height;
  }-*/;
  
  public final native String getUrl()/*-{
    return this.src;
  }-*/;
  
  public final native int getWidth()/*-{
    return this.width;
  }-*/;
  
  public final native boolean isLoaded()/*-{
    return this.__isLoaded;
  }-*/;
}
