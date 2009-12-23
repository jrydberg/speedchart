package org.gwt.speedchart.client.util;


public final class JavaArray1D implements MutableArray1D {

  private double[] backingArray;
  private int size;

  private static final double GROWTH_FACTOR = 2;

  public JavaArray1D() {
    backingArray = new double[1];
  }

  private static int newCapacity(int currLength, int requestedIndex) {
    double newLength = (double) Math.max(currLength, 1);
    while ((double) requestedIndex >= newLength) {
      newLength *= GROWTH_FACTOR;
    }
    return (int) newLength;
  }

  public double[] toArray() {
    double[] array = new double[size];
    System.arraycopy(backingArray, 0, array, 0, size);
    return array;
  }

  public double[] backingArray() {
    return backingArray;
  }

  public double getLast() {
    return backingArray[size - 1];
  }

  public double get(int index) {
    return backingArray[index];
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  private void ensureCapacity(int idx) {
    int capacity = backingArray.length;
    boolean needMoreRowCapacity = (idx >= capacity);

    if (needMoreRowCapacity) {
      int newCapacity = newCapacity(capacity, idx);
      double[] newBackingArray = new double[newCapacity];
      
      System.arraycopy(backingArray, 0, newBackingArray, 0,
         capacity);
    }
  }

  public void set(int idx, double value) {
    ArgChecker.isLTE(idx, size + 1, "idx");
    ensureCapacity(idx);
    backingArray[idx] = value;
    size = Math.max(size, idx + 1);
  }
  
  public void execFunction(ArrayFunction f) {
    f.exec(backingArray, size);
  }

}
