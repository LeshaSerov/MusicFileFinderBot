package com.serov.alex.bot.regex;

import com.serov.alex.bot.regex.utility.DayOfWeek;
import com.serov.alex.bot.regex.utility.Month;
import com.serov.alex.music.MusicFileDtoType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchRegex {

  static String nameDate = "date";
  static String nameDateTimesOfDay = "dateTimesOfDay";
  static String nameType = "type";
  static String nameDayOfWeek = "dayOfWeek";
  static String nameEx = "ex";
  static String name1MI = "Mode1MI";
  static String name1MS = "Mode1MS";
  static String name1YI = "Mode1YI";
  static String name2DIMI = "Mode2DIMI";
  static String name2DIMS = "Mode2DIMS";
  static String name2MIYI = "Mode2MIYI";
  static String name2MSYI = "Mode2MSYI";
  static String name3DIMIYI = "Mode3DIMIYI";
  static String name3DIMSYI = "Mode3DIMSYI";
  static String patternEx = "(?<" + nameEx + ">.*)";

  public static SearchingFile getSearchMusicFileDto(String query) {
    query = query.toLowerCase();
    SearchingFile file = new SearchingFile();
    file.setMorning(false);
    query = addDate(file, query);
    query = addTimesOfDay(file, query);
    query = addType(file, query);
    query = addDayOfWeek(file, query);

    while (query.startsWith(" ")) {
      query = query.substring(1);
    }

    Pattern dateRegex = Pattern.compile(patternEx);
    Matcher matcher = dateRegex.matcher(query);
    if (matcher.find()) {
      file.setNameFile(matcher.group(nameEx));
    }

    if (file.getDay() != null) {
      if (file.getDay().startsWith("0")) {
        file.setDay(file.getDay().substring(1));
      }
    }

    if (file.getMonth() != null) {
      if (file.getMonth().startsWith("0")) {
        file.setMonth(file.getMonth().substring(1));
      }
    }

    return file;
  }

  /*
  //День Месяц
  10 10
  10 окт
  10 10 утро
  10 окт вечер
  //Месяц
  10
  окт
  окт утро
  //Год
  2023
  //Месяц Год
  10 2023
  окт 2023
    10 10 2023
    10 окт 2023
    10 2023 вечер
  окт 2023 вечер
  10 10 2023 вечер
  10 окт 2023 вечер
  */
  private static String addDate(SearchingFile file, String query) {
    String separatorDate = "(?:[- .])";

    String dayDate = "\\d{1,2}";
    String monthIntDate = "\\d{1,2}";
    String monthStringDate = "[а-я]+";
    String yearDate = "\\d{2}";
//    String timesOfDay = "(?<" + nameDateTimesOfDay + ">" + "(утро)|(вечер)" + ")?";
    //(?:\d{2})?

    String pattern1MI = "(?<" + name1MI + ">" + monthIntDate + ")";
    String pattern1MS = "(?<" + name1MS + ">" + monthStringDate + ")";
    String pattern1YI = "(?<" + name1YI + ">" + "\\d{2}" + yearDate + ")";
    String pattern2DIMI = "(?<" + name2DIMI + ">" + dayDate + separatorDate + monthIntDate + ")";
    String pattern2DIMS = "(?<" + name2DIMS + ">" + dayDate + separatorDate + monthStringDate + ")";
    String pattern2MIYI =
        "(?<" + name2MIYI + ">" + monthIntDate + separatorDate + "\\d{2}" + yearDate + ")";
    String pattern2MSYI =
        "(?<" + name2MSYI + ">" + monthStringDate + separatorDate + "\\d{2}" + yearDate + ")";
    String pattern3DIMIYI =
        "(?<" + name3DIMIYI + ">" + dayDate + separatorDate + monthIntDate + separatorDate
            + "(?:\\d{2})?" + yearDate + ")";
    String pattern3DIMSYI =
        "(?<" + name3DIMSYI + ">" + dayDate + separatorDate + monthStringDate + separatorDate
            + "(?:\\d{2})?" + yearDate + ")";

    String patternMain = "(?<" + nameDate + ">"
        + "(?:"
        + pattern3DIMSYI
        + "|" + pattern3DIMIYI
        + "|" + pattern2MSYI
        + "|" + pattern2MIYI
        + "|" + pattern2DIMS
        + "|" + pattern2DIMI
        + "|" + pattern1YI
        + "|" + pattern1MS
        + "|" + pattern1MI
        + ")"
//        + separatorDate + "?"
//        + timesOfDay
        + ")?"
        + patternEx;

    Pattern pattern = Pattern.compile(patternMain);
    Matcher matcher = pattern.matcher(query);
    if (matcher.find()) {
      if (matcher.group(name1MI) != null) {
        file.setMonth(matcher.group(name1MI));
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      } else if (matcher.group(name1MS) != null) {
        Month month = Month.startsWish(matcher.group(name1MS));
        if (month != null) {
          file.setMonth(Integer.toString(month.getNumber()));
//          isTimesOfDay(file, matcher);
          return matcher.group(nameEx);
        } else {
          file.setDay(null);
          return query;
        }
      } else if (matcher.group(name1YI) != null) {
        file.setYear(matcher.group(name1YI));
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      } else if (matcher.group(name2DIMI) != null) {
        String[] date = matcher.group(name2DIMI).split(separatorDate);
        file.setDay(date[0]);
        file.setMonth(date[1]);
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      } else if (matcher.group(name2DIMS) != null) {
        String[] date = matcher.group(name2DIMS).split(separatorDate);
        file.setDay(date[0]);
        Month month = Month.startsWish(date[1]);
        if (month != null) {
          file.setMonth(Integer.toString(month.getNumber()));
//          isTimesOfDay(file, matcher);
          return matcher.group(nameEx);
        } else {
          file.setDay(null);
          return query;
        }
      } else if (matcher.group(name2MIYI) != null) {
        String[] date = matcher.group(name2MIYI).split(separatorDate);
        file.setMonth(date[0]);
        file.setYear(date[1]);
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      } else if (matcher.group(name2MSYI) != null) {
        String[] date = matcher.group(name2MSYI).split(separatorDate);
        Month month = Month.startsWish(date[0]);
        if (month != null) {
          file.setMonth(Integer.toString(month.getNumber()));
        } else {
          file.setDay(null);
          return query;
        }
        file.setYear(date[1]);
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      } else if (matcher.group(name3DIMIYI) != null) {
        String[] date = matcher.group(name3DIMIYI).split(separatorDate);
        file.setDay(date[0]);
        file.setMonth((date[1]));
        file.setYear(date[2]);
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      } else if (matcher.group(name3DIMSYI) != null) {
        String[] date = matcher.group(name3DIMSYI).split(separatorDate);
        file.setDay(date[0]);
        Month month = Month.startsWish(date[1]);
        if (month != null) {
          file.setMonth(Integer.toString(month.getNumber()));
        } else {
          file.setDay(null);
          return query;
        }
        file.setYear(date[2]);
//        isTimesOfDay(file, matcher);
        return matcher.group(nameEx);
      }
      return query;
    }
    return query;
  }

/*  private static void isTimesOfDay(SearchingFile file, Matcher matcher) {
    if (matcher.group(nameDateTimesOfDay) != null) {
      if (matcher.group(nameDateTimesOfDay).contains("утро")) {
        file.setMorning(true);
      } else if (matcher.group(nameDateTimesOfDay).contains("вечер")) {
        file.setEvening(true);
      }
    }
  }*/

  private static String addTimesOfDay(SearchingFile file, String query) {
    String typePattern = "( )?(?<" + nameDateTimesOfDay + ">" + "\\H*" + ")" + patternEx;
    Pattern pattern = Pattern.compile(typePattern);
    Matcher matcher = pattern.matcher(query);
    if (matcher.find()) {
      String timesOfDay = matcher.group(nameDateTimesOfDay);
      if (timesOfDay != null) {
        if (timesOfDay.contains("утро")) {
          file.setMorning(true);
          return matcher.group(nameEx);
        } else if (timesOfDay.contains("вечер")) {
          file.setEvening(true);
          return matcher.group(nameEx);
        }
      }
    }
    return query;
  }

  /*
  //Достаточно ввести несколько начальных букв или специальный символ
  Сценка z
  Слово a
  Пророчество x
  Песня s
  Стих v
  Служение ~
      */
  private static String addType(SearchingFile file, String query) {
    String typePattern = "( )?(?<" + nameType + ">" + "\\H*" + ")" + patternEx;
    Pattern pattern = Pattern.compile(typePattern);
    Matcher matcher = pattern.matcher(query);
    if (matcher.find()) {
      String type = MusicFileDtoType.getType(matcher.group(nameType));
      if (type != null) {
        file.setType(type);
        return matcher.group(nameEx);
      } else {
        return query;
      }
    }
    return query;
  }

  private static String addDayOfWeek(SearchingFile file, String query) {
    String typePattern = "( )?(?<" + nameDayOfWeek + ">" + "\\H*" + ")" + patternEx;
    Pattern pattern = Pattern.compile(typePattern);
    Matcher matcher = pattern.matcher(query);
    if (matcher.find()) {
      Integer dayOfWeek = new DayOfWeek().geDayOfWeek(matcher.group(nameDayOfWeek));
      if (dayOfWeek != null) {
        file.setDayOfWeek(dayOfWeek);
        return matcher.group(nameEx);
      } else {
        return query;
      }
    }
    return query;
  }

}
