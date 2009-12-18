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
 * Factory for obtaining a suitable {@link TickFormatter} object for a given
 * domain span.
 * 
 * @author chad takahashi
 */
public abstract class TickFormatterFactory {

  private double affinityFactor;
  private TickFormatter rootFormatter;
  private double cachedDomainWidth = Double.NEGATIVE_INFINITY;
  private TickFormatter cachedFormatter = null;
  
  public TickFormatterFactory() {
    this.affinityFactor = getAffinityFactor();
    this.rootFormatter = createRootTickFormatter();    
  }
  
  protected abstract TickFormatter createRootTickFormatter();
  
  /**
   * A value in the range (0.0, 1.0], which determines how readily the
   * {@link #findBestFormatter(double)} algorithm will "jump down to" the next
   * sub-formatter. The larger the factor, the more "affinity" the algorithm
   * will have for the current formatter.
   */
  protected double getAffinityFactor() {
    return 0.35;
  }
  
  /**
   * Finds the smallest-scale {@link TickFormatter} that engulfs the 
   * specified domain interval.
   */
  public final TickFormatter findBestFormatter(double domainWidth) {
    if (domainWidth == cachedDomainWidth) {
      return cachedFormatter;
    }

    TickFormatter tlf = rootFormatter;

    while (!tlf.isLeafFormatter()) {
      if (tlf.inInterval(domainWidth * affinityFactor)) {
        break;
      }
      tlf = tlf.subFormatter;
    }

    cachedDomainWidth = domainWidth;
    cachedFormatter = tlf;
    return tlf;
  }
  
  public final TickFormatter getLeafFormatter() {
    TickFormatter formatter = rootFormatter;
    while (!formatter.isLeafFormatter()) {
      formatter = formatter.subFormatter;
    }
    return formatter;
  }

  public final TickFormatter getRootFormatter() {
    return rootFormatter;
  }

}
