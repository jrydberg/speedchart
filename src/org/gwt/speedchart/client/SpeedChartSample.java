package org.gwt.speedchart.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.graphics.client.Color;

import org.gwt.speedchart.client.data.MutableDataset2D;
import org.gwt.speedchart.client.data.BinaryMipMapStrategy;
import org.gwt.speedchart.client.util.Interval;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SpeedChartSample implements EntryPoint {

  public Dataset getBasicDataset() {
    int numSamples = 1000;

    double d = 0;
    double max = 0, min = 1111111111;

    double[] domainValues = new double[numSamples];
    double[] rangeValues = new double[numSamples];

    for (int i = 0; i < numSamples; i++) {
      double tmp = 5.0 * (double) i / (double) numSamples;
      double ry = Math.sin(Math.PI * tmp) / Math.exp(tmp / 5.0);

      domainValues[i] = d;
      rangeValues[i] = ry;
      if (ry > max)
	max = ry;
      if (ry < min)
	min = ry;
      d += (60 * 60 * 24);
    }

    return new MutableDataset2D(domainValues, rangeValues,
				BinaryMipMapStrategy.MEAN, new Interval(min, max));
  }

  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final Button sendButton = new Button("Send");
    final TextBox nameField = new TextBox();
    nameField.setText("GWT User");


    // We can add style names to widgets
    sendButton.addStyleName("sendButton");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    final SpeedChart speedChart = new SpeedChart();
    RootPanel.get("nameFieldContainer").add(speedChart);

    GraphUiProps graphUiProps = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);
    GraphUiProps graphUiProps2 = new GraphUiProps(Color.RED,
        Color.BLACK, 0);

    final int n = 1300;
    int d = 60*60*24*300;
    double[] domainData = new double[n];
    double[] rangeData = new double[n];
    for (int i = 0; i < n; i++) {
      domainData[i] = d;
      rangeData[i] = Random.nextDouble() * 1.0;
      d += (60 * 60 * 24);
    }
    
    Dataset dataset = new MutableDataset2D(domainData, rangeData, 
        BinaryMipMapStrategy.MEAN);
    speedChart.addDataset(getBasicDataset(), graphUiProps);
    speedChart.addDataset(dataset, graphUiProps2);
    speedChart.fillWidth();
    //speedChart.redraw();

    Element divElement = Document.get().createDivElement();
    Style style = divElement.getStyle();
    style.setProperty("position", "absolute");
    style.setPropertyPx("left", -1000);
    style.setPropertyPx("top", -1000);
    divElement.setInnerText("HELLO WORLD");
    RootPanel.get().getElement().appendChild(divElement);
    Log.info("width is " + divElement.getClientWidth());

    //RootPanel.get("sendButtonContainer").add(sendButton);
  }
}
