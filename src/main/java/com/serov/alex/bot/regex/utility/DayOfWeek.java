package com.serov.alex.bot.regex.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DayOfWeek {

  public String getToString(Calendar calendar) {
    DateFormat dateFormat = new SimpleDateFormat("EEEE", new Locale("ru"));
    return dateFormat.format(calendar.getTime());
  }

  public boolean containDayOfWeek(String s, int year, int month, int day) {
    Calendar calendar = new GregorianCalendar(year, month, day);
    String dayToString = getToString(calendar).toLowerCase();
    return dayToString.contains(s);
  }

  public Integer geDayOfWeek(String s) {
    if (s == null || s.isEmpty()) {
      return null;
    }

    if (containDayOfWeek(s, 2023, 11, 4)) {
      return 1;
    }
    if (containDayOfWeek(s, 2023, 11, 5)) {
      return 2;
    }
    if (containDayOfWeek(s, 2023, 11, 6)) {
      return 3;
    }
    if (containDayOfWeek(s, 2023, 11, 7)) {
      return 4;
    }
    if (containDayOfWeek(s, 2023, 11, 8)) {
      return 5;
    }
    if (containDayOfWeek(s, 2023, 11, 9)) {
      return 6;
    }
    if (containDayOfWeek(s, 2023, 11, 10)) {
      return 7;
    }
    return null;
  }

}
