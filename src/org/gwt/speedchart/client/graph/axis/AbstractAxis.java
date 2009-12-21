package org.gwt.speedchart.client.graph.axis;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;


public class AbstractAxis extends FlowPanel {

  protected Element[] labelElements;

  protected void allocLabels(int numLabels) {
    if (labelElements == null || labelElements.length < numLabels) {
      if (labelElements != null) {
	for (int i = 0; i < labelElements.length; i++) {
	  Element labelElement = labelElements[i];
	  if (labelElement != null) {
	    getElement().removeChild(labelElement);
	  }
	}
	labelElements = null;
      }

      labelElements = new Element[numLabels];
    }
  }
  
  protected Element getLabelElement(int labelIndex, String cssClassName) {
    if (labelElements[labelIndex] == null) {
      Element labelElement = Document.get().createDivElement();
      labelElement.setClassName(cssClassName);
      getElement().appendChild(labelElement);
      labelElements[labelIndex] = labelElement;
    }
    return labelElements[labelIndex];
  }
				    
  protected void cleanUnusedLabels(int firstUnusedLabelIndex) {
    for (int i = firstUnusedLabelIndex; i < labelElements.length; i++) {
      Element labelElement = labelElements[i];
      if (labelElement != null) {
	getElement().removeChild(labelElement);
	labelElements[i] = null;
      }
    }
  }

}