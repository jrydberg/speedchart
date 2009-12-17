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

import org.gwt.speedchart.client.Dataset;

/**
 * Returned as the result of {@link Dataset#getBestMipMapForInterval} containing the
 * resulting MipMap, start, and end index.
 */
public class MipMapRegion {

  private MipMap mipMap;

  private int startIndex, endIndex;

  public MipMapRegion(MipMap mipMap, int startIndex, int endIndex) {
    this.endIndex = endIndex;
    this.mipMap = mipMap;
    this.startIndex = startIndex;
  }

  public int getEndIndex() {

    return endIndex;
  }

  public MipMap getMipMap() {
    return mipMap;
  }

  public int getStartIndex() {
    return startIndex;
  }
}
