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
 * A 2-dimensional array that can contain a variable number of column elements
 * for a given row.
 * 
 * @author Chad Takahashi
 */
public interface Array2D {

  /**
   * Adds a new row to this array by reference.
   */
  public void addRowByRef(double[] row);
  
  /**
   * Adds a new row to this array by value.
   */
  public void addRowByValue(double[] row);

  /**
   * Returns the value at the specified row and column
   */
  double get(int row, int column);
  
  Array1D getRow(int rowIndex);
  
  /**
   * Returns true only if the other {@link Array2D} object has the same
   * number of rows, as well as the same number of columns-per-row, as this
   * object.
   */
  boolean isSameSize(Array2D other);

  /**
   * Returns the number of columns in the specified row (rows can have differing
   * number of columns).
   */
  int numColumns(int rowIndex);

  /**
   * Returns the number of rows in this array
   */
  int numRows();

  /**
   * Assigns the value at the specified row and column
   */
  void set(int rowIdx, int colIdx, double value);

}
