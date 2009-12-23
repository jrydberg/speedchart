package org.gwt.speedchart.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.graphics.client.Color;
import org.gwt.speedchart.client.data.MutableDataset2D;
import org.gwt.speedchart.client.data.BinaryMipMapStrategy;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;

import org.gwt.speedchart.client.util.TimeUnit;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

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
      rangeValues[i] = ry * 2000;
      if (ry > max)
	max = ry;
      if (ry < min)
	min = ry;
      d += (60 * 60 * 24);
    }

    return new MutableDataset2D(domainValues, rangeValues,
        BinaryMipMapStrategy.MEAN); // new Interval(min, max));
  }

  public Dataset getTimeseriesDataset() {
    int numSamples = (30 * 24 * 60);

    double d = 0;
    double max = 0, min = 1111111111;

    double[] domainValues = new double[numSamples];
    double[] rangeValues = new double[numSamples];

    for (int i = 0; i < numSamples; i++) {
      double ry = Random.nextDouble();

      domainValues[i] = d;
      rangeValues[i] = ry * 2000;

      d += (60 * 1000);
    }

    return new MutableDataset2D(domainValues, rangeValues,
        BinaryMipMapStrategy.MEAN);
  }

  public Dataset getRandomDataset() {
    int numSamples = 1000;

    double d = 0;
    double max = 0, min = 1111111111;

    double[] domainValues = new double[numSamples];
    double[] rangeValues = new double[numSamples];

    for (int i = 0; i < numSamples; i++) {
      double ry = Random.nextDouble();

      domainValues[i] = d;
      rangeValues[i] = ry * 2000;
      if (ry > max)
	max = ry;
      if (ry < min)
	min = ry;
      d += (60 * 60 * 24);
    }

    return new MutableDataset2D(domainValues, rangeValues,
        BinaryMipMapStrategy.MEAN); // new Interval(min, max));
  }

  private static final class DomainWidthClickHandler 
      implements ClickHandler {

    private final TimeUnit timeUnit;

    private final SparklineChart chart;

    public DomainWidthClickHandler(TimeUnit timeUnit,
        SparklineChart chart) {
      this.timeUnit = timeUnit;
      this.chart = chart;
    }

    public void onClick(ClickEvent event) {
      chart.setDomainWidth(timeUnit.ms());
    }
  }

  public Widget createSparklineExample() {
    GraphUiProps dsUiProps = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);

    SparklineChart chart = new SparklineChart();
    chart.addDataset(getTimeseriesDataset(), dsUiProps);
    Log.info("domain: start: " + chart.getTimelineModel().getLeftBound()
	     + "; end: " + chart.getTimelineModel().getRightBound());
    //chart.setDomainWidth(30 * 24 * 60 * 1000);
    chart.zoomAll();
    
    Button button1 = new Button("Month");
    button1.addClickHandler(new DomainWidthClickHandler(
        TimeUnit.MONTH, chart));

    Button button2 = new Button("Day");
    button2.addClickHandler(new DomainWidthClickHandler(
        TimeUnit.DAY, chart));

    Button button3 = new Button("Hour");
    button3.addClickHandler(new DomainWidthClickHandler(
        TimeUnit.HOUR, chart));
    
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(button1);
    buttonPanel.add(button2);
    buttonPanel.add(button3);

    final LayoutPanel panel = new LayoutPanel();
    panel.add(buttonPanel);
    panel.setWidgetTopHeight(buttonPanel, 0, Unit.PX, 50, Unit.PX);
    panel.setWidgetLeftRight(buttonPanel, 40, Unit.PCT, 40, Unit.PCT);

    panel.add(chart);
    panel.setWidgetTopHeight(chart, 100, Unit.PX, 50, Unit.PX);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);
    return panel;
  }

  public Widget createSpeedChartExample() {

    GraphUiProps dsUiProps1 = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);
    GraphUiProps dsUiProps2 = new GraphUiProps(Color.RED,
        Color.BLACK, 0);

    final SpeedChart chart = new SpeedChart();
    chart.addDataset(getRandomDataset(), dsUiProps1);
    chart.addDataset(getBasicDataset(), dsUiProps2);
    chart.zoomAll();
    
    final LayoutPanel panel = new LayoutPanel();
    panel.add(chart);
    panel.setWidgetTopHeight(chart, 50, Unit.PX, 50, Unit.PCT);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);
    return panel;
  }

  public Widget createAreaChartExample() {

    GraphUiProps dsUiProps1 = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);
    GraphUiProps dsUiProps2 = new GraphUiProps(Color.RED,
        Color.BLACK, 0);

    final AreaChart chart = new AreaChart();
    chart.addDataset(getBasicDataset(), dsUiProps1);
    chart.addDataset(getRandomDataset(), dsUiProps2);
    chart.zoomAll();
    
    final LayoutPanel panel = new LayoutPanel();
    panel.add(chart);
    panel.setWidgetTopHeight(chart, 50, Unit.PX, 50, Unit.PCT);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);
    return panel;
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final TabLayoutPanel tabPanel = new TabLayoutPanel(30, Unit.PX);
    
    tabPanel.add(createSpeedChartExample(), "SpeedChart");
    tabPanel.add(createSparklineExample(), "Sparklines");
    tabPanel.add(createAreaChartExample(), "AreaChart");

    RootLayoutPanel.get().add(tabPanel);

    DeferredCommand.add(new Command() {
      public void execute() {
	RootLayoutPanel.get().onResize();
      }
    });
  }
}
