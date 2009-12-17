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

package org.gwt.speedchart.client.data.tuple;

/**
 * An n-dimensional vector of primitive <tt>double</tt> values, where 
 * <i>n</i> is denoted by {@link #size()}.
 */
public interface Tuple2D {

  /**
   * Returns the value that the specified index within this tuple
   * 
   * @param index - The 0-based index
   */
  double getRange(int index);
  
  /**
   * The domain value of the data point that this tuple represents.
   */
  double getDomain();

  /**
   * The range value (or if this is a tuple having 3 or more dimensions,
   * the first of several range values).
   */
  double getRange0();
  
  /**
   * The number of values in this tuple (also referred to as the tuple's 
   * <i>dimension</i>).
   */
  int size();
  
}
