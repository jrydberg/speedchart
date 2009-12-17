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

package org.gwt.speedchart.client.data;

import org.gwt.speedchart.client.util.ArgChecker;

import java.util.Collection;

/**
 * Represents some form of change to the stucture of an {@link XYDataset}.
 * Typical examples are:
 * <ul>
 * <li> Appending a new datapoint to the end of a dataset
 * <li> Updating the Y-value of an existing datapoint within a dataset
 * <li> Removing a datapoint from a dataset
 * </ul>
 * 
 * @author Chad Takahashi
 */
public abstract class Mutation {

  /**
   * Appends the specified datapoint to the end of a dataset.
   * 
   * @param x The X-value of the datapoint to be appended
   * @param y The Y-value of the datapoint to be appended
   */
  public static AppendMutation append(double x, double y) {
    AppendMutation mutation = new AppendMutation();
    mutation.x = x;
    mutation.y = y;
    return mutation;
  }

  /**
   * Updates the Y-value of an existing datapoint within a dataset.
   * 
   * @param pointIndex The 0-based index of the datapoint whose Y-value is to be
   *          modified.
   * @param y The new Y-value.
   */
  public static RangeMutation setY(int pointIndex, double y) {
    RangeMutation mutation = new RangeMutation();
    mutation.pointIndex = pointIndex;
    mutation.y = y;
    return mutation;
  }

  /**
   * Mutation composed of 1 or more mutations to be applied to a dataset as a
   * batch update.
   */
  public static BatchMutation batch(Collection<Mutation> mutations) {
    ArgChecker.isNotNull(mutations, "mutations");
    BatchMutation mutation = new BatchMutation();
    mutation.mutations = mutations;
    return mutation;
  }

  /**
   * See {@link #append(double, double)}.
   */
  public static final class AppendMutation extends Mutation {
    double x, y;

    private AppendMutation() { /* not publicly constructable */
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }

    public String toString() {
      return "[x=" + x + "; y=" + y + "]";
    }
  }

  /**
   * See {@link #setY(int, double)}.
   * 
   * @author chad
   * 
   */
  public static final class RangeMutation extends Mutation {
    int pointIndex;
    double y;

    private RangeMutation() { /* not publicly constructable */
    }

    public int getPointIndex() {
      return pointIndex;
    }

    public double getY() {
      return y;
    }

    public String toString() {
      return "[pointIndex=" + pointIndex + "; y=" + y + "]";
    }
  }

  /**
   * See {@link #batch(Collection)}.
   */
  public static final class BatchMutation extends Mutation {
    private Collection<Mutation> mutations;

    /**
     * Returns the constituent mutations in this mutation batch.
     */
    public Collection<Mutation> getAll() {
      return mutations;
    }
  }
}
