package com.serov.alex.music;

import java.util.Comparator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicFileDtoComparator implements Comparator<MusicFileDto> {

  @Override
  public int compare(MusicFileDto obj1, MusicFileDto obj2) {
    int nameFileComparison = obj1.getNameFile().compareTo(obj2.getNameFile());
    if (nameFileComparison != 0) {
      return nameFileComparison;
    }
    int typeComparison = obj1.getType().compareTo(obj2.getType());
    if (typeComparison != 0) {
      return typeComparison;
    }
    // Сравниваем по дате
    int dateComparison = compareDates(obj1, obj2);
    if (dateComparison != 0) {
      return dateComparison;
    }
    // Сравниваем по номеру трека
    int trackNumberComparison = compareTrackNumbers(obj1, obj2);
    if (trackNumberComparison != 0) {
      return trackNumberComparison;
    }
    // Сравниваем по названию
    int titleComparison = compareTitles(obj1, obj2);
    if (titleComparison != 0) {
      return titleComparison;
    }
    // Сравниваем по композиции
    return compareCompositions(obj1, obj2);
  }

  private int compareDates(MusicFileDto obj1, MusicFileDto obj2) {
    int yearComparison = compareYears(obj1, obj2);
    if (yearComparison != 0) {
      return yearComparison;
    }
    int monthComparison = compareMonths(obj1, obj2);
    if (monthComparison != 0) {
      return monthComparison;
    }
    int dayComparison = compareDays(obj1, obj2);
    if (dayComparison != 0) {
      return dayComparison;
    }
    return Boolean.compare(obj1.isEvening(), obj2.isEvening());
  }

  private int compareYears(MusicFileDto obj1, MusicFileDto obj2) {
    if (obj1.getYear() != null && obj2.getYear() != null) {
      String years1 = obj1.getYear();
      String years2 = obj2.getYear();
      years1 = years1.length() == 4 ? years1 : "20" + years1;
      years2 = years2.length() == 4 ? years2 : "20" + years2;
      return years1.compareTo(years2);
    } else if (obj1.getYear() != null) {
      return -1;
    } else if (obj2.getYear() != null) {
      return 1;
    }
    return 0;
  }

  private int compareMonths(MusicFileDto obj1, MusicFileDto obj2) {
    if (obj1.getMonth() != null && obj2.getMonth() != null) {
      String months1 = obj1.getMonth();
      String months2 = obj2.getMonth();
      months1 = months1.length() == 2 ? months1 : "0" + months1;
      months2 = months2.length() == 2 ? months2 : "0" + months2;
      return months1.compareTo(months2);
    } else if (obj1.getMonth() != null) {
      return -1;
    } else if (obj2.getMonth() != null) {
      return 1;
    }
    return 0;
  }

  private int compareDays(MusicFileDto obj1, MusicFileDto obj2) {
    if (obj1.getDay() != null && obj2.getDay() != null) {
      String days1 = obj1.getDay();
      String days2 = obj2.getDay();
      days1 = days1.length() == 2 ? days1 : "0" + days1;
      days2 = days2.length() == 2 ? days2 : "0" + days2;
      return days1.compareTo(days2);
    } else if (obj1.getDay() != null) {
      return -1;
    } else if (obj2.getDay() != null) {
      return 1;
    }
    return 0;
  }

  private int compareTrackNumbers(MusicFileDto obj1, MusicFileDto obj2) {
    if (obj1.getNumber() != null && obj2.getNumber() != null) {
      if (obj1.getNumber().equals(obj2.getNumber())) {
        if (obj1.getPart() != null && obj2.getPart() != null) {
          return obj1.getPart().compareTo(obj2.getPart());
        } else if (obj1.getPart() != null) {
          return -1;
        } else if (obj2.getPart() != null) {
          return 1;
        }
      }
      return obj1.getNumber().compareTo(obj2.getNumber());
    }
    if (obj1.getNumber() != null) {
      return -1;
    }
    if (obj2.getNumber() != null) {
      return 1;
    }
    return 0;
  }

  private int compareTitles(MusicFileDto obj1, MusicFileDto obj2) {
    if (obj1.getTitle() != null && obj2.getTitle() != null) {
      return obj1.getTitle().compareTo(obj2.getTitle());
    } else if (obj1.getTitle() != null) {
      return -1;
    } else if (obj2.getTitle() != null) {
      return 1;
    }
    return 0;
  }

  private int compareCompositions(MusicFileDto obj1, MusicFileDto obj2) {
    if (obj1.getComposition() != null && obj2.getComposition() != null) {
      return obj1.getComposition().compareTo(obj2.getComposition());
    } else if (obj1.getComposition() != null) {
      return -1;
    } else if (obj2.getComposition() != null) {
      return 1;
    }
    return 0;
  }

}
