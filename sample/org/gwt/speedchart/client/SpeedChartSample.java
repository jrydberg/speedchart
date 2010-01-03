package org.gwt.speedchart.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.graphics.client.Color;
import org.gwt.speedchart.client.data.ArrayDataset2D;
import org.gwt.speedchart.client.data.MutableDataset2D;
import org.gwt.speedchart.client.data.BinaryMipMapStrategy;
import org.gwt.speedchart.client.data.Mutation;
import org.gwt.speedchart.client.data.MipMap;
import org.gwt.speedchart.client.data.ZoomDataset2D;
import org.gwt.speedchart.client.data.ZoomDataset2D.DatasetModel;
import org.gwt.speedchart.client.data.ZoomDataset2D.Request;
import org.gwt.speedchart.client.data.ZoomDataset2D.Callback;
import org.gwt.speedchart.client.util.Array1D;
import org.gwt.speedchart.client.util.SumArrayFunction;
import org.gwt.speedchart.client.util.Util;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.graph.TimelineModel;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;

import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.util.TimeUnit;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

import java.util.Date;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SpeedChartSample implements EntryPoint {

  private void timeIt(InlineLabel label, Command command) {
    Date s1 = new Date();
    command.execute();
    Date s2 = new Date();
    label.setText("Operation took: " + (s2.getTime() - s1.getTime())
        + "ms");
  }

  public Dataset getZoomDataset(DatasetModel model) {
    return new ZoomDataset2D(model, new Interval(0, 
        24 * 60 * 60 * 1000));
  }

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

  public MutableDataset getSmallRealtimeDataset() {
    int numSamples = 10;

    double d = (double) new Date().getTime();

    double[] domainValues = new double[numSamples];
    double[] rangeValues = new double[numSamples];

    for (int i = 0; i < numSamples; i++) {
      double ry = Random.nextDouble();

      domainValues[i] = d;
      rangeValues[i] = ry * 2000;
      d += 1000;
    }

    return new MutableDataset2D(domainValues, rangeValues,
        BinaryMipMapStrategy.MEAN);
  }


  public Dataset getLargeDataset(final int numSamples) {
    double[] domainValues = new double[numSamples];
    double[] rangeValues = new double[numSamples];

    double span = 200;
    double initial = 2000;
    double d = 0;
    
    for (int i = 0; i < numSamples; i++) {
      double ry = initial + ((Random.nextDouble() - 0.5) * 200);
      domainValues[i] = d;
      rangeValues[i] = ry;
      initial = ry;
      d += 1000;
    }

    return new ArrayDataset2D(domainValues, rangeValues,
        BinaryMipMapStrategy.MEAN);
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

    final Dataset ds = getTimeseriesDataset();

    SparklineChart chart = new SparklineChart();
    chart.addDataset(ds, dsUiProps);
    Log.info("domain: start: " + chart.getTimelineModel().getLeftBound()
	     + "; end: " + chart.getTimelineModel().getRightBound());
    //chart.setDomainWidth(30 * 24 * 60 * 1000);
    chart.zoomAll();

    final InlineLabel countLabel = new InlineLabel();

    chart.getTimelineModel().addWindowBoundsObserver(
      new TimelineModel.WindowBoundsObserver() {
	public void onWindowBoundsChange(double left,
            double right) {
	  MipMap mipMap = ds.getMipMapChain().getMipMap(0);
	  Array1D domain = mipMap.getDomain();
	  SumArrayFunction sumFn = new SumArrayFunction(
              Util.binarySearch(domain, left), 
	      Util.binarySearch(domain, right));
	  mipMap.getRange(0).execFunction(sumFn);
	  countLabel.setText("Count: " + sumFn.getSum());
	}
      });
    
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

    panel.add(countLabel);
    panel.setWidgetTopHeight(countLabel, 50, Unit.PX, 50, Unit.PX);
    panel.setWidgetLeftRight(countLabel, 40, Unit.PCT, 40, Unit.PCT);

    panel.add(chart);
    panel.setWidgetTopHeight(chart, 100, Unit.PX, 50, Unit.PX);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);
    return panel;
  }

  private Dataset largeDataset;

  public Widget createLargeExample() {
    final GraphUiProps dsUiProps = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);
    final SpeedChart chart = new SpeedChart();

    final InlineLabel countLabel = new InlineLabel();

    Button button1 = new Button("Small");
    button1.addClickHandler(new ClickHandler() {
	public void onClick(ClickEvent event) {
	  if (largeDataset != null) {
	    chart.removeDataset(largeDataset);
	  }
	  timeIt(countLabel, new Command() {
	      public void execute() {
		largeDataset = getLargeDataset(40000);
		chart.addDataset(largeDataset, dsUiProps);
	      }
	    });
	}
      });

    Button button2 = new Button("Large");
    button2.addClickHandler(new ClickHandler() {
	public void onClick(ClickEvent event) {
	  if (largeDataset != null) {
	    chart.removeDataset(largeDataset);
	  }
	  timeIt(countLabel, new Command() {
	      public void execute() {
		largeDataset = getLargeDataset(100000);
		chart.addDataset(largeDataset, dsUiProps);
	      }
	    });
	}
      });

    Button button3 = new Button("Huge");
    button3.addClickHandler(new ClickHandler() {
	public void onClick(ClickEvent event) {
	  if (largeDataset != null) {
	    chart.removeDataset(largeDataset);
	  }
	  timeIt(countLabel, new Command() {
	      public void execute() {
		largeDataset = getLargeDataset(400000);
		chart.addDataset(largeDataset, dsUiProps);
	      }
	    });
	}
      });
    
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(button1);
    buttonPanel.add(button2);
    buttonPanel.add(button3);

    final LayoutPanel panel = new LayoutPanel();
    panel.add(buttonPanel);
    panel.setWidgetTopHeight(buttonPanel, 0, Unit.PX, 50, Unit.PX);
    panel.setWidgetLeftRight(buttonPanel, 40, Unit.PCT, 40, Unit.PCT);

    panel.add(countLabel);
    panel.setWidgetTopHeight(countLabel, 50, Unit.PX, 50, Unit.PX);
    panel.setWidgetLeftRight(countLabel, 40, Unit.PCT, 40, Unit.PCT);

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

  public Widget createStreamingExample() {

    GraphUiProps dsUiProps1 = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);

    final SpeedChart chart = new SpeedChart();
    final MutableDataset ds = getSmallRealtimeDataset();
    chart.addDataset(ds, dsUiProps1);
    chart.zoomAll();
    
    chart.getTimelineModel().setStreaming(true);
    chart.getTimelineModel().toggleFixBounds();

    Timer t = new Timer() {
	public void run() {
	  double d = (double) new Date().getTime();
	  ds.mutate(Mutation.append(d, Random.nextDouble() 
              * 2000));
	}
      };
    t.scheduleRepeating(1000);

    final LayoutPanel panel = new LayoutPanel();
    panel.add(chart);
    panel.setWidgetTopHeight(chart, 50, Unit.PX, 50, Unit.PCT);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);
    return panel;
  }

  public Widget createOverlayExample() {

    GraphUiProps dsUiProps1 = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);
    OverlayUiProps overlayUiProps1 = new OverlayUiProps(Color.GREEN);
    OverlayUiProps overlayUiProps2 = new OverlayUiProps(Color.ORANGE);

    final ChartUiProps chartUiProps = new ChartUiProps();
    chartUiProps.setDrawBorders(true);
    chartUiProps.setVisibleRangeInterval(new Interval(-200.0, 2200.0));

    final SpeedChart chart = new SpeedChart(chartUiProps);
    chart.addDataset(getBasicDataset(), dsUiProps1);
    chart.zoomAll();

    chart.addRangeOverlay(new RangeOverlay(1500.0, overlayUiProps1));
    chart.addDomainOverlay(new DomainOverlay(
        new Interval(5 * (60 * 60 * 24), 200 * (60 * 60 * 24)),
	overlayUiProps2));
    
    final LayoutPanel panel = new LayoutPanel();
    panel.add(chart);
    panel.setWidgetTopHeight(chart, 50, Unit.PX, 50, Unit.PCT);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);
    return panel;
  }

  private static class SampleDatasetModel implements DatasetModel {

    double[] units = new double[]{TimeUnit.SEC.ms(), 
				  TimeUnit.MIN.ms(), 
				  TimeUnit.HOUR.ms()};

    public SampleDatasetModel() {
    }

    public double[] getUnits() {
      return units;
    }

    public void requestData(Request request, final Callback callback) {
      Interval region = request.getRegion();
      int unitIdx = request.getUnitIdx();

      double tick = units[unitIdx];
      double d = region.getStart();
	  
      int numSamples = (int) (region.length() / tick);
      final double[] domain = new double[numSamples];
      final double[] range = new double[numSamples];

      for (int i = 0; i < numSamples; i++) {
	domain[i] = d;
	range[i] = Random.nextDouble() * 2000;
	d += tick;
      }

      Timer t = new Timer() {
	  public void run() {
	    callback.provideData(domain, range);
	  };
	};
      t.schedule(1500);
    }
  }

  public Widget createZoomDatasetExample() {

    GraphUiProps dsUiProps1 = new GraphUiProps(Color.BLUE,
        Color.BLACK, 0);

    final ChartUiProps chartUiProps = new ChartUiProps();
    chartUiProps.setDrawBorders(true);
    chartUiProps.setVisibleRangeInterval(new Interval(0.0, 2200.0));

    final SpeedChart chart = new SpeedChart(chartUiProps);
    final DatasetModel model = new SampleDatasetModel();
    chart.addDataset(getZoomDataset(model), dsUiProps1);
    chart.zoomAll();

    final LayoutPanel panel = new LayoutPanel();
    panel.add(chart);
    panel.setWidgetTopHeight(chart, 50, Unit.PX, 50, Unit.PCT);
    panel.setWidgetLeftRight(chart, 20, Unit.PCT, 20, Unit.PCT);

    InlineLabel doc = new InlineLabel(
        "The graph above does on-demand loading of its data "
        + "with a simulated 1.5 second delay. "
        + "Resolutions are hour, minute and second.");

    panel.add(doc);
    panel.setWidgetTopHeight(doc, 70, Unit.PCT, 100, Unit.PCT);
    panel.setWidgetLeftRight(doc, 20, Unit.PCT, 20, Unit.PCT);

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
    tabPanel.add(createStreamingExample(), "Steaming");
    tabPanel.add(createOverlayExample(), "Overlay");
    tabPanel.add(createLargeExample(), "Benchmark");
    tabPanel.add(createZoomDatasetExample(), "Zoom");

    RootLayoutPanel.get().add(tabPanel);

    DeferredCommand.add(new Command() {
      public void execute() {
	RootLayoutPanel.get().onResize();
      }
    });
  }
}
