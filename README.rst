==========
SpeedChart
==========

SpeedChart is an unholy merge of the graph part of SpeedTracer and the
data management functionality of Timefire Chronoscope.

The main idea is to start out with something and then simplify
it. Another idea is to add other functions like;

* sparklines (done!)


In the longer run I'm also interested in adding asynchronous fetching
of data, so that you can start out with a low-resolution dataset and
then request more finegrained data when the user zooms into a region.
The goal is to be able to graph network statistics with per-second
resolution for all history :)


SpeedChart uses the layout panel system that came with GWT 2.0, and
expects that onResize is called on launch.  You can do this with a
simple DeferredCommand and invoke onResize on the RootLayoutPanel.


Chronoscope is released under LGPL.
SpeedTracer is released under Apache License 2.0.

