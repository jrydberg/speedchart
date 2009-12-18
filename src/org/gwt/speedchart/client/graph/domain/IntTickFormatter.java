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
 * Formats domain axis ticks as plain integer values.
 * 
 * @author chad takahashi
 */
public class IntTickFormatter extends TickFormatter {
  private double tickValue;
  private double tickInterval;
  
  protected IntTickFormatter(int tickInterval, String longestPossibleLabel) {
    super(longestPossibleLabel);

    this.tickInterval = tickInterval;
    this.possibleTickSteps = new int[] {1, 2, 5, 10, 20, 50, 100, 200, 400, 500, 1000};
  }

  @Override
  public String formatTick() {
    return Long.toString((long)tickValue);
  }

  @Override
  public double getTickDomainValue() {
    return tickValue;
  }

  @Override
  public double getTickInterval() {
    return tickInterval;
  }

  @Override
  public int getSubTickStep(int primaryTickStep) {
    if (tickInterval == 1) {
      return primaryTickStep;
    }
    else if (tickInterval <= 10) {
      return (int)tickInterval;
    }
    else {
      return 2;
    }
  }

  @Override
  public int incrementTick(int numTickSteps) {
    tickValue += (numTickSteps * tickInterval);
    return numTickSteps;
  }

  @Override
  public void resetToQuantizedTick(double domainX, int tickStep) {
    final long dx = (long)domainX;
    final long t = ((long)tickStep * (long)tickInterval);
    
    this.tickValue = (double)(dx - (dx % t));
    /*
    System.out.println("TESTING: IntTickFormatter.resetToQuantizedTick: dx=" + dx +
        "; tick[idealStep=" + tickStep +
        "; interval=" + (long)tickInterval +
        "; QValue=" + (long)tickValue +
        "]");
    */
  }
  
  @Override
  public String toString() {
    return "tick:interval=" + (long)tickInterval;
  }
}
