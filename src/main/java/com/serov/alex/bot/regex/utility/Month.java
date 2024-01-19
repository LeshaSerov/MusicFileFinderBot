package com.serov.alex.bot.regex.utility;

import java.util.Objects;
import lombok.Getter;

@Getter
public enum Month {
  January("январь", 1),
  February("февраль", 2),
  March("март", 3),
  April("апрель", 4),
  May("май", 5),
  June("июнь", 6),
  July("июль", 7),
  August("август", 8),
  September("сентябрь", 9),
  October("октябрь", 10),
  November("ноябрь", 11),
  December("декабрь", 12);

  final String text;
  final int number;

  Month(String month, int number) {
    this.text = month;
    this.number = number;
  }

  private static boolean isStartWish(Month month, String s) {
    return month.text.startsWith(s)
        || (month.text.substring(0, month.text.length() - 1) + "я").startsWith(s)
        || month.text.toLowerCase().startsWith(s)
        || (month.text.toLowerCase().substring(0, month.text.length() - 1) + "я").startsWith(s);
  }

  public static Month startsWish(String s) {
    if (s == null || Objects.equals(s, "")) {
      return null;
    }
    if (isStartWish(January, s)) {
      return January;
    }
    if (isStartWish(February, s)) {
      return February;
    }
    if (isStartWish(March, s)) {
      return March;
    }
    if (isStartWish(April, s)) {
      return April;
    }
    if (isStartWish(May, s)) {
      return May;
    }
    if (isStartWish(June, s)) {
      return June;
    }
    if (isStartWish(July, s)) {
      return July;
    }
    if (isStartWish(August, s)) {
      return August;
    }
    if (isStartWish(September, s)) {
      return September;
    }
    if (isStartWish(October, s)) {
      return October;
    }
    if (isStartWish(November, s)) {
      return November;
    }
    if (isStartWish(December, s)) {
      return December;
    }
    return null;
  }
}
