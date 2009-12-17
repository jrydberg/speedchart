/*
 * This file was original part of Timepedia Chronoscope.
 *
 * Copyright (C) Timepedia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package org.gwt.speedchart.client.util;


/**
 * Implementation of {@link Array2D} backed by a Java 2-dimensional array,
 * and is growable.
 * 
 * @author Chad Takahashi
 */
public final class JavaArray2D implements Array2D {
  private static final double GROWTH_FACTOR = 2;

  private static int newCapacity(int currLength, int requestedIndex) {
    double newLength = (double) Math.max(currLength, 1);
    while ((double) requestedIndex >= newLength) {
      newLength *= GROWTH_FACTOR;
    }
    return (int) newLength;
  }

  private double[][] a;
  private int[] columnCounts;
  private int rowCount;
  private Array1D[] rows;
  
  public JavaArray2D() {
    final int initialRowCapacity = 10;
    columnCounts = new int[initialRowCapacity];
    a = new double[initialRowCapacity][];
    for (int i = 0; i < initialRowCapacity; i++) {
      a[i] = new double[0];
    }
    rowCount = 0;
  }
  
  /**
   * Constructs a {@link JavaArray2D} object from the specified Java
   * 2-dimensional array.
   */
  public JavaArray2D(double[][] a) {
    init(a);
  }
  
  /**
   * Creates a 2-dimensional array having exactly 1 row.
   */
  public JavaArray2D(double[] row) {
    ArgChecker.isNotNull(row, "row");
    double[][] array2d = new double[1][];
    array2d[0] = row;
    init(array2d);
  }
  
  /**
   * Constructs a {@link JavaArray2D} object from the specified Java
   * 2-dimensional array.
   */
  private void init(double[][] a) {
    // Validate a[][]:
    ArgChecker.isNotNull(a, "a");
    // array must have at least 1 row
    if (a.length == 0) {
      throw new IllegalArgumentException("a.length must be > 0");
    }
    // All rows must be non-null
    for (int i = 0; i < a.length; i++) {
      if (a[i] == null) {
        throw new IllegalArgumentException("a[" + i + "] was null");
      }
    }

    rowCount = a.length;
    columnCounts = new int[rowCount];
    rows = new Array1D[rowCount];

    for (int i = 0; i < rowCount; i++) {
      columnCounts[i] = a[i].length;
    }

    this.a = a;
  }

  public void addRowByRef(double[] row) {
    ArgChecker.isNotNull(row, "row");
    final int newRowIndex = rowCount;
    ensureRowCapacity(newRowIndex);
    a[newRowIndex] = row;
    columnCounts[newRowIndex] = row.length;
  }
  
  public void addRowByValue(double[] row) {
    addRowByRef(Util.copyArray(row));
  }
  
  /**
   * Returns the value at the specified row and column
   */
  public double get(int row, int column) {
    assert (rowCount > 0)
      : "Attempted to call get() when array is empty";
    assert MathUtil.isBounded(row, 0, rowCount - 1) 
      : "row out of bounds: " + row;
    assert MathUtil.isBounded(column, 0, numColumns(row) - 1)
      : "column out of bounds: " + column;
    
    // gwt-chronoscope Issue #87 
    // (http://code.google.com/p/gwt-chronoscope/issues/detail?id=87)
    // Profiling revealed this method as exremely hot.  So switched to
    // using assertion, which can be compiled out in hosted and web mode.
    // The ChartBench.java test app, when running in web mode, indicated
    // a significant performance increase (21 FPS to 27 FPS).
    /*
    if (!MathUtil.isBounded(row, 0, rowCount - 1)) {
      throw new ArrayIndexOutOfBoundsException(row);
    }

    if (!MathUtil.isBounded(column, 0, numColumns(row) - 1)) {
      throw new ArrayIndexOutOfBoundsException(column);
    }
    */
    
    return a[row][column];
  }
  
  public Array1D getRow(int rowIndex) {
    assert MathUtil.isBounded(rowIndex, 0, rowCount - 1) 
      : "row out of bounds: " + rowIndex;
    
    Array1D row = rows[rowIndex];
    if (row == null) {
      row = new Array1DImpl(this, rowIndex);
      rows[rowIndex] = row;
    }
    return row;
  }
  

  public boolean isSameSize(Array2D other) {
    ArgChecker.isNotNull(other, "other");
    if (numRows() != other.numRows()) {
      return false;
    }

    for (int i = 0; i < numRows(); i++) {
      if (numColumns(i) != other.numColumns(i)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns the number of columns in the specified row (rows can have differing
   * number of columns).
   */
  public int numColumns(int rowIndex) {
    assert (rowIndex < this.rowCount) : "rowIndex out of bounds";
    return columnCounts[rowIndex];
  }

  /**
   * Returns the number of rows in this array
   */
  public int numRows() {
    return rowCount;
  }

  /**
   * Assigns the value at the specified row and column.
   */
  public void set(int rowIdx, int colIdx, double value) {
    ensureRowCapacity(rowIdx);
    
    int colCapacity = a[rowIdx].length;
    boolean needMoreColumnCapacity = (colIdx >= colCapacity);
    if (needMoreColumnCapacity) {
      int newColCapacity = newCapacity(colCapacity, colIdx);
      double[] newRow = new double[newColCapacity];
      double[] row = a[rowIdx];
      System.arraycopy(row, 0, newRow, 0, row.length);
      a[rowIdx] = newRow;
    }

    columnCounts[rowIdx] = Math.max(columnCounts[rowIdx], colIdx + 1);

    a[rowIdx][colIdx] = value;
  }
  
  private void ensureRowCapacity(int rowIdx) {
    int rowCapacity = a.length;
    boolean needMoreRowCapacity = (rowIdx >= rowCapacity);

    if (needMoreRowCapacity) {
      int newRowCapacity = newCapacity(rowCapacity, rowIdx);
      double[][] newA = new double[newRowCapacity][];
      for (int i = 0; i < rowCount; i++) {
        newA[i] = a[i];
      }
      for (int i = rowCount; i < newRowCapacity; i++) {
        newA[i] = new double[0];
      }
      a = newA;
      int[] newColumnCounts = new int[newRowCapacity];
      System.arraycopy(columnCounts, 0, newColumnCounts, 0, rowCapacity);
      columnCounts = newColumnCounts;
    }
    
    if ((rowIdx + 1) > rowCount) {
      rowCount = rowIdx + 1;
      rows = new Array1D[rowCount];
    }
  }
  
  private static final class Array1DImpl implements Array1D {
    private int row;
    private JavaArray2D parentArray;
    
    public Array1DImpl(JavaArray2D parentArray, int row) {
      this.parentArray = parentArray;
      this.row = row;
    }
    
    public double[] backingArray() {
      return this.parentArray.a[this.row];
    }
    
    public double get(int index) {
      assert (index < parentArray.columnCounts[row]) 
          : "index out of bounds: " + index;
      
      return this.parentArray.a[this.row][index];
    }
    
    public double getLast() {
      int arraySize = parentArray.columnCounts[row];
      if (arraySize > 0) {
        return this.parentArray.a[this.row][arraySize - 1];
      }
      else {
        throw new IllegalStateException("array is empty");
      }
    }
    
    public boolean isEmpty() {
      return size() == 0;
    }
    
    public int size() {
      return this.parentArray.columnCounts[row];
    }

    public void execFunction(ArrayFunction f) {
      f.exec(parentArray.a[row], parentArray.columnCounts[row]);
    }
    
    public double[] toArray() {
      return Util.copyArray(backingArray(), size());
    }
  }
}
