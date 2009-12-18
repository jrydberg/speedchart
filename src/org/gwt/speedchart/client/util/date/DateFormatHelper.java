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

package org.gwt.speedchart.client.util.date;

import org.gwt.speedchart.client.util.MathUtil;
import org.gwt.speedchart.client.util.DateFormatter;

import java.util.Date;


/**
 * Utilities for formatting a {@Link Date} into different date
 * representations.
 *
 * @author chad takahashi
 */
public final class DateFormatHelper {

  static final String[] MONTH_LABELS = createMonthLabels();

  // Used by pad(int) to efficiently convert ints in the range [0..59] to a
  // zero-padded 2-digit string.
  static final String[] TWO_DIGIT_NUMS = new String[]{"00", "01", "02", "03",
      "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
      "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
      "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
      "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51",
      "52", "53", "54", "55", "56", "57", "58", "59",};

  private static final String[] HOURS_OF_DAY = createHoursOfDayLabels();

  public static DateFormatter getDateFormatter(String format) {
    return DateFormatterFactory.getInstance().getDateFormatter(format);
  }

  /**
   * Uses GWT DateTimeFormat class to obtain hour-of-day labels (e.g. "9am").
   */
  private static String[] createHoursOfDayLabels() {
    DateFormatter fmt = getDateFormatter("HH:00"); // h=hour, a=AM/PM
    String[] hourLabels = new String[24];
    for (int h = 0; h < hourLabels.length; h++) {
      hourLabels[h] = fmt
          .format(new Date(1970 - 1970, 0, 1, h, 0, 0).getTime());
    }
    return hourLabels;
  }

  /**
   * Uses GWT DateTimeFormat class to obtain abbreviated month labels to ensure
   * local-specificity.
   */
  private static String[] createMonthLabels() {
    DateFormatter fmt = getDateFormatter("MMM"); // "Jan", "Feb", ...
    String[] monthLabels = new String[12];
    for (int m = 0; m < monthLabels.length; m++) {
      monthLabels[m] = fmt.format(new Date(1970 - 1970, m, 1).getTime());
    }
    return monthLabels;
  }

  /**
   * Returns an abbreviated month string for the specified date.
   */
  private static String formatMonth(Date d) {
    return MONTH_LABELS[d.getMonth()];
  }

  /**
   * Formats day and month as "dd-MMM" (e.g. "31-Oct").
   *
   * @param d - The date to be formatted
   */
  public String dayAndMonth(Date d) {
    return pad(d.getDay()) + "-" + formatMonth(d);
  }

  /**
   * Formats the hour of the day.
   *
   * @param hourOfDay - a value in the range [0, 23]
   */
  public String hour(int hourOfDay) {
    return HOURS_OF_DAY[hourOfDay];
    // return pad(hourOfDay) + ":00";
  }

  /**
   * Formats hour and minute as "hh:mm".
   *
   * @param d - The date to be formatted
   */
  public String hourAndMinute(Date d) {
    return pad(d.getHours()) + ":" + pad(d.getMinutes());
  }

  /**
   * Formats hour minute and second as "hh:mm:ss".
   *
   * @param d - The date to be formatted
   */
  public String hourMinuteSecond(Date d) {
    return pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":" + pad(
        d.getSeconds());
  }

  /**
   * Formats month and year as "mmm'yy" (e.g. "Aug-23").
   *
   * @param d - The date to be formatted
   */
  public String monthAndYear(Date d) {
    return formatMonth(d) + "'" + twoDigitYear(d);
  }

  /**
   * Returns a 0-padded 2-digit number from the specified integer (e.g. pad(6)
   * returns "06", pad(59) returns "59").
   */
  public String pad(int num) {
    return TWO_DIGIT_NUMS[num];
    // return num < 10 ? "0" + num : "" + num;
  }

  /**
   * Formats hour:minute:second:1/10sec (e.g. "23:56:05:04").
   *
   * @param d - The date to be formatted
   */
  public String tenthOfSecond(Date d) {
    int tenthSecond = MathUtil.mod((int) d.getTime() / 100, 10);
    return pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":"
        + pad(d.getSeconds()) + "." + pad(tenthSecond);
  }

  /**
   * Formats a 2 digit year string from the specified date. For example, the
   * date '2006-05-25' returns the string "06".
   */
  public String twoDigitYear(Date d) {
    String yr = String.valueOf(d.getYear());
    return yr.substring(yr.length() - 2);
  }
}
