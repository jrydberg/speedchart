package org.gwt.speedchart.client.util;

/**
 * Calculates the smallest interval in an {@link Array1D} object. 
 * If the size of the array is 0 or 1, then 0 is returned as the 
 * minimum interval.
 * 
 * @author chad takahashi
 */
public class MinIntervalArrayFunction implements ArrayFunction {
  private double minInterval;
  
  public void exec(double[] data, int arrayLength) {
    double min = Double.MAX_VALUE;
    
    if (arrayLength < 2) {
      // An interval requires at least 2 points, so in this case, just return 0.
      min = 0.0;
    }
    else {
      double prevValue = data[0];
      for (int i = 1; i < arrayLength; i++) {
        double currValue = data[i];
        min = Math.min(min, Math.abs(currValue - prevValue));
        prevValue = currValue;
      }
    }
    
    this.minInterval = min;
  }
  
  public double getMinInterval() {
    return this.minInterval;
  }

}
