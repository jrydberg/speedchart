/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.speedchart.client.fx;

import com.google.gwt.animation.client.Animation;
import org.gwt.speedchart.client.graph.TimelineModel;
import org.gwt.speedchart.client.util.Interval;

import java.util.List;
import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;


public class Zoom extends Animation {

  private boolean inProgress = false;

  private double leftStart;
  private double leftEnd;
  private double rightStart;
  private double rightEnd;
  
  private TimelineModel model;

  private List<AnimationListener> listeners;

  public Zoom(TimelineModel model) {
    this.model = model;
  }

  public void addListener(AnimationListener listener) {
    if (listeners == null) {
      listeners = new ArrayList<AnimationListener>();
    }
    listeners.add(listener);
  }

  public boolean isInProgress() {
    return inProgress;
  }

  @Override
  public void onCancel() {
    if (inProgress) {
      onComplete();
    }
  }

  @Override
  public void onComplete() {
    if (listeners != null) {
      for (AnimationListener listener : listeners)
	listener.animationStop();
    }
    inProgress = false;
    onUpdate(1.0);
  }

  @Override
  public void onStart() {
    if (listeners != null) {
      for (AnimationListener listener : listeners)
	listener.animationStart();
    }
  }

  @Override
  public void onUpdate(double progress) {
    double newLeftBound = leftStart + ((leftEnd - leftStart) * progress);
    double newRightBound = rightStart + ((rightEnd - rightStart) * progress);

    Interval newDomain;
    if (newLeftBound < newRightBound) {
      newDomain = new Interval(newLeftBound, newRightBound);
    } else {
      newDomain = new Interval(newRightBound, newLeftBound);
    }
    
    model.updateBounds(newDomain.getStart(), newDomain.getEnd());
  }

  public void zoom(int dur, Interval end) {
    leftStart = model.getLeftBound();
    leftEnd = end.getStart();
    rightStart = model.getRightBound();
    rightEnd = end.getEnd();
    this.run(dur);
  }

}
