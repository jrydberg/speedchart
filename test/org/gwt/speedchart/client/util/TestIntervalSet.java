package org.gwt.speedchart.client.util;

import junit.framework.TestCase;

public class TestIntervalSet extends TestCase {

  public void XtestAdd() {
    IntervalSet is = new IntervalSet();

    is.add(new Interval(1, 2));
    is.add(new Interval(2, 3));
    is.add(new Interval(0, 2));
    is.add(new Interval(5, 6));
    assertEquals(is.size(), 2);

    is.add(new Interval(3, 6));
    assertEquals(is.size(), 1);
    Interval i = is.get(0);
    assertEquals(i.getStart(), 0.0);
    assertEquals(i.getEnd(), 6.0);
  }

  public void XtestDifference() {
    IntervalSet is1 = new IntervalSet();
    is1.add(new Interval( 0,  5));
    is1.add(new Interval(10, 20));

    IntervalSet is2 = new IntervalSet();
    is2.add(new Interval( 3,  13));

    IntervalSet is3 = is2.difference(is1);
    System.out.println(is3);

    //IntervalSet is4 = is1.difference(is2);
    //System.out.println(is4);
  }


  public void testDifference2() {
    IntervalSet is1 = new IntervalSet();
    is1.add(new Interval( 0,  10));

    IntervalSet is2 = new IntervalSet();
    is2.add(new Interval( 2,  10));

    IntervalSet is3 = is2.difference(is1);
    System.out.println(is3);

    //IntervalSet is4 = is1.difference(is2);
    //System.out.println(is4);
  }

  

//   public static void testDiff1() {
//     IntervalSet is = new IntervalSet();

//     is.add(new Interval(0, 5));
//     IntervalSet d = is.difference(new Interval(2, 3));
//     if (d.size() != 2)
//       System.out.println("FAIL: testDiff: size=" + d.size());
//     Interval i1 = d.get(0);
//     if (!i1.equals(new Interval(0, 2)))
//       System.out.println("FAIL: testDiff: i1: " + i1);
//     Interval i2 = d.get(1);
//     if (!i2.equals(new Interval(3, 5)))
//       System.out.println("FAIL: testDiff: i2: " + i2);
//   }

//   public static void testDiff2() {

//     IntervalSet is = new IntervalSet();

//     is.add(new Interval(0, 20));
//     is.add(new Interval(30, 60));
//     IntervalSet d = is.difference(new Interval(10, 40));
//     if (d.size() != 2)
//       System.out.println("FAIL: testDiff: size=" + d.size());
//   }

//   public static void testDiffSame() {

//     IntervalSet is = new IntervalSet();

//     is.add(new Interval(0, 20));
//     IntervalSet d = is.difference(new Interval(0, 20));
//     if (d.size() != 0)
//       System.out.println("FAIL: testSame: size=" + d.size());
//   }

//     public static Test suite() {
// 	suite.addTest(new MathTest("testAdd"));
// 	suite.addTest(new MathTest("testDivideByZero"));
// 	return suite;
//     }

}
