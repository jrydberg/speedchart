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

package org.gwt.speedchart.client.graph.domain;

/**
 * Formats domain ticks as generic integers.
 *
 * @author chad takahashi
 */
public final class IntTickFormatterFactory extends TickFormatterFactory {

  public IntTickFormatterFactory() {
  }

  private static final String[] ZERO_STRINGS = {"", "0", "00", "000", "0000",
      "00000", "000000", "0000000", "00000000", "000000000", "0000000000",
      "00000000000", "000000000000"};

  @Override
  protected TickFormatter createRootTickFormatter() {
    int[] domainLengths = {1, 10, 20, 50, 100, 200, 400, 500, 1000, 10000,
        100000, 1000000, 10000000, 100000000, 1000000000};

    IntTickFormatter prev = null;
    for (int i = 0; i < domainLengths.length; i++) {
      final int domainLength = domainLengths[i];

      // This creates a dummy label whose length corresponds to a typical range of
      // svn rev#'s (i.e. it assumes that revs started at 1 and are consecutive).
      // TODO: Modify TickFormatter framework to allow for dynamic tick label 
      // width calcs.
      String prototypeLabel = ZERO_STRINGS[1 + Integer.toString(domainLength)
          .length()];

      IntTickFormatter curr = new IntTickFormatter(domainLength,
          prototypeLabel);
      if (prev != null) {
        curr.subFormatter = prev;
        prev.superFormatter = curr;
      }

      prev = curr;
    }

    return prev;
  }

  @Override
  protected double getAffinityFactor() {
    return 0.25;
  }

}
