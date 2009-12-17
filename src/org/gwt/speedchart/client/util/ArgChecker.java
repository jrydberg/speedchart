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
 * Conglomeration of static convenience methods that validate input arguments
 * and throw a nicely-formatted <tt>IllegalArgumentException</tt> for illegal
 * arguments.
 * 
 * @author Chad Takahashi
 */
public final class ArgChecker {

  public static void isInRange(int value, int low, int high, String valueName) {
    if (value < low) {
      throw new IllegalArgumentException("Minimum value for " + quote(valueName) + " is " + low + ": " +value);
    }
    if (value > high) {
      throw new IllegalArgumentException("Maximum value for " + quote(valueName) + " is " + high + ": " +value);
    }
  }
  
  // Feel free to add more validation methods as necessary

  public static int isNonNegative(int value, String argName) {
    if (value < 0) {
      throw new IllegalArgumentException(quote(argName) + " was negative: "
          + value);
    }
    return value;
  }

  public static double isNonNegative(double value, String argName) {
    if (value < 0) {
      throw new IllegalArgumentException(quote(argName) + " was negative: "
          + value);
    }
    return value;
  }

  public static double isLTE(double value, double max, String argName) {
    if (value > max) {
      throw new IllegalArgumentException(quote(argName) + " was >= " + max
          + ": " + value);
    }
    return value;
  }

  public static double isLTE(int value, int max, String argName) {
    if (value > max) {
      throw new IllegalArgumentException(quote(argName) + " was >= " + max
          + ": " + value);
    }
    return value;
  }
  
  public static double isGT(double value, double min, String argName) {
    if (value <= min) {
      throw new IllegalArgumentException(quote(argName) + " was <= " + min
          + ": " + value);
    }
    return value;
  }

  public static double isGT(int value, int min, String argName) {
    if (value <= min) {
      throw new IllegalArgumentException(quote(argName) + " was <= " + min
          + ": " + value);
    }
    return value;
  }
  
  public static double isGTE(int value, int min, String argName) {
    if (value < min) {
      throw new IllegalArgumentException(quote(argName) + " was < " + min
          + ": " + value);
    }
    return value;
  }

  public static double isNormalDouble(double value, String argName) {
    if (Double.isInfinite(value)) {
      throw new IllegalArgumentException(quote(argName) + " was Infinite");
    } else if (Double.isNaN(value)) {
      throw new IllegalArgumentException(quote(argName) + " was NaN");
    }
    return value;
  }

  public static Object isNotNull(Object obj, String argName) {
    if (obj == null) {
      throw new IllegalArgumentException(quote(argName) + " was null");
    }
    return obj;
  }

  /**
   * Wraps a string in single quotes
   */
  private static String quote(String s) {
    return "'" + s + "'";
  }
}
