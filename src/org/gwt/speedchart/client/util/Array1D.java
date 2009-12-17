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
 * A 1-dimensional array of primitive <tt>double</tt> values, whose
 * primary use is as a row in an {@link Array2D} array.
 * 
 * @see {@link Array2D#getRow(int)}.
 * 
 * @author chad takahashi
 */
public interface Array1D {
  
  /**
   * Returns the primitive <tt>double</tt> array that backs this
   * object.  Note that the length of the backing array might 
   * be greater than what {@link #size()} reports if this is
   * a growable array.
   * <p>
   * This method should only be used when maximum performance 
   * is needed.
   */
  double[] backingArray();
  
  /**
   * Returns the value at the index-th position in this array.
   */
  double get(int index);
  
  /**
   * Returns the last value in this array.
   */
  double getLast();
  
  /**
   * Returns the number of elements in this array.
   */
  int size();
  
  /**
   * Returns true if this array has 0 elements.
   */
  boolean isEmpty();
  
  /**
   * Applies the specified function to the elements in this array.
   */
  void execFunction(ArrayFunction f);
  
  /**
   * Returns a copy of this array as a primitive double array.
   */
  double[] toArray();
}
