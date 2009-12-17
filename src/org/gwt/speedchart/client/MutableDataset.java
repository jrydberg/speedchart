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

package org.gwt.speedchart.client;

import org.gwt.speedchart.client.data.DatasetListener;
import org.gwt.speedchart.client.data.Mutation;
import org.gwt.speedchart.client.data.tuple.Tuple2D;

/**
 * Dataset that permits certain types of mutations (e.g. appending new data
 * points, modifying the state of existing data points).
 * 
 * @author chad takahashi
 */
public interface MutableDataset<T extends Tuple2D> extends Dataset<T> {

  /**
   * Adds the specified listener to the collection of listeners to be notified
   * when changes to this dataset occur.
   */
  void addListener(DatasetListener<T> listener);

  /**
   * Removes the specified listener from the collection of listeners to be
   * notified when changes to this dataset occur.
   */
  public void removeListener(DatasetListener<T> listener);

  /**
   * Applies the specified mutation to this dataset.
   */
  public void mutate(Mutation mutation);

}
