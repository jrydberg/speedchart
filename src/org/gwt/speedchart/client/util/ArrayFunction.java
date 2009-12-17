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
 * A function to be applied to an array of primitive <tt>double</tt> values
 * within a {@link Array1D} object.  Implementing classes are responsible for
 * defining whatever return value is necessary as an accessor method.
 * 
 * @see {@link Array1D#execFunction(ArrayFunction)}
 * 
 * @author chad takahashi
 */
public interface ArrayFunction {
  
  void exec(double[] data, int arrayLength);
  
}
