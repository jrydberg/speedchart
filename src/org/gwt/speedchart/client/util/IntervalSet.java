package org.gwt.speedchart.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;


public class IntervalSet implements Iterable<Interval>{

  private List<Interval> intervals = new ArrayList<Interval>();;

  public IntervalSet() {
  }

  public IntervalSet(IntervalSet other) {
    for (Interval i : other)
      add(i);
  }

  public Iterator<Interval> iterator() {
    return intervals.iterator();
  }
  
  public void add(Interval r) {
    if (r.length() != 0) {
      List<Interval> newIntervals = new ArrayList<Interval>();

      for (Interval i : intervals) {
	if (i.overlaps(r) || i.adjacentTo(r))
	  r = r.join(i);
	else
	  newIntervals.add(i);
      }
      newIntervals.add(r);
      Collections.sort(newIntervals);
      this.intervals = newIntervals;
    }
  }

  /**
   * Return a new {@link IntervalSet} that holds the intervals that is
   * present in the other set but that is not in this set.
   */
  public IntervalSet difference(IntervalSet other) {

    IntervalSet result = new IntervalSet(this);

    for (Interval j : other) {
      IntervalSet temp = new IntervalSet();
      for (Interval i : result) {
	if (i.overlaps(j)) {
	  if (i.contains(j))
	    continue;
	  else if (j.contains(i)) {
	    if (i.getStart() < j.getStart()) {
	      System.out.println("add11: " + new Interval(i.getStart(), j.getStart()));
	      temp.add(new Interval(i.getStart(), j.getStart()));
	    }
	    if (j.getEnd() < i.getEnd()) {
	      System.out.println("add12: " + new Interval(j.getEnd(), i.getEnd()));
	      temp.add(new Interval(j.getEnd(), i.getEnd()));
	    }
	  } else if (j.comesBefore(i)) {
	      System.out.println("add2: " + new Interval(j.getEnd(), i.getEnd()));
	    temp.add(new Interval(j.getEnd(), i.getEnd()));
	  } else {
	      System.out.println("add3: " + new Interval(i.getStart(), j.getStart()));
	    temp.add(new Interval(i.getStart(), j.getStart()));
	  }
	} else {
	  temp.add(i.copy());
	}
      }
      result = temp;
    }
    
    return result;
  }

  public String toString() {
    return intervals.toString();
  }

  public int size() {
    return intervals.size();
  }

  public Interval get(int idx) {
    return intervals.get(idx);
  }

}