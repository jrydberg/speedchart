package org.gwt.speedchart.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Johan Rydberg
 */
public class DOMUtil {

  private static DOMUtil INSTANCE;

  public static DOMUtil get() {
    if (INSTANCE == null) {
      INSTANCE = new DOMUtil();
    }
    return INSTANCE;
  }

  private Element stringWidthElement;

  private void createStringWidthElement() {
    stringWidthElement = Document.get().createDivElement();
    Style style = stringWidthElement.getStyle();
    style.setProperty("position", "absolute");
    style.setPropertyPx("left", -6000);
    style.setPropertyPx("top", -6000);
    RootPanel.get().getElement().appendChild(stringWidthElement);
  }

  public int stringHeight(String text, String className) {
    if (stringWidthElement == null) {
      createStringWidthElement();
    }
    stringWidthElement.setInnerText(text);
    stringWidthElement.setClassName(className);
    return stringWidthElement.getClientHeight();
  }

  /**
   * Return screen width of the given text evaluated with the
   * specified CSS class name.
   */
  public int stringWidth(String text, String className) {
    if (stringWidthElement == null) {
      createStringWidthElement();
    }
    stringWidthElement.setInnerText(text);
    stringWidthElement.setClassName(className);
    return stringWidthElement.getClientWidth();
  }

}