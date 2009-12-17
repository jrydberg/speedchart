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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.graphics.client.charts.ColorCodedDataList;
import com.google.gwt.graphics.client.charts.ColorCodedValue;
import com.google.gwt.graphics.client.charts.PieChart;
import com.google.gwt.topspin.ui.client.Container;
import com.google.gwt.topspin.ui.client.DefaultContainerImpl;
import com.google.gwt.topspin.ui.client.Div;
import com.google.gwt.topspin.ui.client.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple visual tests of some of the chart widgets.
 */
public class ChartTest implements EntryPoint {

  PieChart.Resources resources = GWT.create(PieChart.Resources.class);

  public void onModuleLoad() {
    StyleInjector.injectStylesheet(resources.pieChartCss().getText()
        + resources.colorListCss().getText());
    List<ColorCodedValue> data = new ArrayList<ColorCodedValue>();
    data.add(new ColorCodedValue("A", 20, Color.LIGHT_GREY));
    data.add(new ColorCodedValue("B", 20, Color.LIGHTGREEN));
    data.add(new ColorCodedValue("C", 60, Color.INDIAN_RED));

    Div div = new Div(Root.getContainer());
    div.getElement().getStyle().setProperty("textAlign", "center");
    Container container = new DefaultContainerImpl(div.getElement()); 
      
    PieChart pieChart = new PieChart(container, data, resources);
    pieChart.showLegend();

    // Now lets test making a horizontal list
    new ColorCodedDataList(Root.getContainer(), data,
        data.size(), 100, true, false, resources);
  }
}
