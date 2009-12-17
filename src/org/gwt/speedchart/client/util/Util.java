package org.gwt.speedchart.client.util;

import org.gwt.speedchart.client.Dataset;
import org.gwt.speedchart.client.data.tuple.Tuple2D;

/**
 * Conglomeration of common utility functions.
 */
public final class Util {

  /**
   * Searches the specified array for the specified value using a binary
   * search algorithm.  If the array doesn't contain the specified value, 
   * then the array index of the next largest value is returned. The one 
   * exception to this rule is when <tt>value</tt> is greater than all 
   * values within the array, in which case the largest value in the array
   * is returned. 
   * <p> 
   * The array is assumed to be in sorted ascending order.
   *
   * @param a - The array to search on
   * @param value - The sought-after value
   */
  public static <T extends Tuple2D> int binarySearch(Array1D a, double value) {
    int low = 0;
    int high = a.size() - 1;

    while (low <= high) {
      int mid = (low + high) >> 1;
      double midVal = a.get(mid);

      if (midVal < value) {
        low = mid + 1;
      } else if (midVal > value) {
        high = mid - 1;
      } else {
        return mid; // key found
      }
    }

    return MathUtil.bound(low, 0, a.size() - 1);
  }

  /**
   * Returns a copy of the specified array.
   */
  public static double[] copyArray(double[] a) {
    if (a != null) {
      return copyArray(a, a.length);
    }
    else {
      return null;
    }
  }

  /**
   * Returns a copy of the first <tt>length</tt> elements of the specified array.
   */
  public static double[] copyArray(double[] a, int length) {
    if (a == null) {
      return null;
    }
    double[] copy = new double[length];
    System.arraycopy(a, 0, copy, 0, length);
    return copy;
  }
  
  /**
   * Returns a copy of the specified array.
   */
  public static int[] copyArray(int[] a) {
    if (a == null) {
      return null;
    }
    int[] copy = new int[a.length];
    System.arraycopy(a, 0, copy, 0, a.length);
    return copy;
  }

  /**
   * Finds the index of the array value that's the closest to 'searchValue' without
   * equaling or exceeding it.  If searchValue is not greater than a[0], then 
   * -1 is returned.
   * 
   * @param a - the array to search over, which is assumed to have at least 1 element
   * @param searchValue - the value to search for
   * @param startIndex - the index in the domain to start searching
   */
  public static int search(double[] a, double searchValue, int startIndex) {
    ArgChecker.isNonNegative(startIndex, "startIndex");
    
    final int domainLen = a.length;
    
    // TODO: replace linear search with a binary search!
    for (int i = startIndex; i < domainLen; i++) {
      if (a[i] >= searchValue) {
        return (i - 1);
      }
    }
    
    return domainLen - 1;
  }

  /**
   * Returns a copy of the specified 2-dimensional array.
   */
  public static double[][] copyArray(double[][] a) {
    double[][] copy = new double[a.length][];
    for (int i = 0; i < a.length; i++) {
      copy[i] = copyArray(a[i]);
    }
    return copy;
  }
  
  /**
   * Determines if a and b are equal, taking into consideration that a or b (or
   * both a and b) could be null.
   */
  public static boolean isEqual(Object a, Object b) {
    if (a == b) {
      return true;
    }

    if (a == null && b == null) {
      return true;
    }

    if ((a == null && b != null) || (b == null && a != null)) {
      return false;
    }

    return a.equals(b);
  }

  public static boolean isSameDomain(String url1, String url2) {
    // won't current handle case if hostpage is https: and url2 is relative
    int ss1 = url1.indexOf("//");
    int ss2 = url2.indexOf("//");
    if (ss1 == -1 || ss2 == -1) {
      return true;
    }

    String scheme1 = "http";
    if (ss1 != -1) {
      scheme1 = url1.substring(0, ss1);
      url1 = url1.substring(ss1 + 2);
    }

    String scheme2 = "http";
    if (ss2 != -1) {
      scheme2 = url2.substring(0, ss2);
      url2 = url2.substring(ss2 + 2);
    }
    if (scheme1.equals(scheme2)) {
      String parts1[] = url1.split("/");
      String parts2[] = url2.split("/");
      String hostpart1[] = parts1[0].split(":");
      String hostpart2[] = parts2[0].split(":");
      String port1 = scheme1.equals("https") ? "443" : "80";
      String port2 = port1;
      String host1 = hostpart1[0];
      String host2 = hostpart2[0];

      if (hostpart1.length > 1) {
        port1 = hostpart1[1];
      }
      if (hostpart2.length > 1) {
        port2 = hostpart2[1];
      }

      return host1.equals(host2) && port1.equals(port2);
    }
    return false;
  }

}
