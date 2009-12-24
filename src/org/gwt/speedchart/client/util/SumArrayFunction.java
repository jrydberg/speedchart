package org.gwt.speedchart.client.util;

public class SumArrayFunction implements ArrayFunction {

  private double sum;

  private int startIdx;
  private int stopIdx;

  public SumArrayFunction(int startIdx, int stopIdx) {
    this.startIdx = startIdx;
    this.stopIdx = stopIdx;
  }

  private Interval extrema;
  
  public void exec(double[] data, int arrayLength) {
    stopIdx = Math.min(arrayLength - 1, stopIdx);

    for (int i = startIdx; i <= stopIdx; i++) {
      sum += data[i];
    }
  }
  
  public double getSum() {
    return this.sum;
  }

}
