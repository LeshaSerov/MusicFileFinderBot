package com.serov.alex.bot.regex.find;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.regex.SearchRegex;
import com.serov.alex.bot.regex.SearchingFile;
import com.serov.alex.bot.regex.utility.DayOfWeek;
import com.serov.alex.music.MusicFileDto;
import com.serov.alex.music.MusicFileDtoComparator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public interface Searching {

    static boolean containTextInMusicFileDto(MusicFileDto musicFileDto,
                                             SearchingFile searchingFile) {

        if (searchingFile.getYear() != null) {
            if (!Objects.equals(searchingFile.getYear(), musicFileDto.getYear())) {
                return false;
            }
        }
        if (searchingFile.getMonth() != null) {
            if (!Objects.equals(searchingFile.getMonth(), musicFileDto.getMonth())) {
                return false;
            }
        }
        if (searchingFile.getDay() != null) {
            if (!Objects.equals(searchingFile.getDay(), musicFileDto.getDay())) {
                return false;
            }
        }
        if (searchingFile.isEvening()) {
            if (!musicFileDto.isEvening()) {
                return false;
            }
        }
        if (searchingFile.isMorning()) {
            if (musicFileDto.isEvening()) {
                return false;
            }
        }
        if (searchingFile.getType() != null) {
            if (!Objects.equals(searchingFile.getType(), musicFileDto.getType())) {
                return false;
            }
        }

        if (searchingFile.getDayOfWeek() != null){
            Calendar calendar = new GregorianCalendar(
                Integer.parseInt(musicFileDto.getYear()),
                Integer.parseInt(musicFileDto.getMonth()) - 1,
                Integer.parseInt(musicFileDto.getDay()) - 1
            );
            int searchingFileDayOfWeek = searchingFile.getDayOfWeek();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (searchingFileDayOfWeek != dayOfWeek) {
                return false;
            }
        }

        return musicFileDto.getNameFile().contains(searchingFile.getNameFile())
            || musicFileDto.getNameFile().toLowerCase().contains(searchingFile.getNameFile());
/*        if (searchingFile.getTitle() != null) {
            if (searchingFile.getComposition() != null) {
                if (musicFileDto.getTitle() == null) {
                    return false;
                }
                return ((musicFileDto.getTitle().contains(searchingFile.getTitle()))
                        || (musicFileDto.getComposition() != null
                        && musicFileDto.getComposition().contains(searchingFile.getComposition()))
                        || (musicFileDto.getVerse() != null
                        && musicFileDto.getVerse().contains(searchingFile.getVerse())));
            }

            return (musicFileDto.getTitle() != null
                    && musicFileDto.getTitle().contains(searchingFile.getTitle()))
                    || (musicFileDto.getComposition() != null
                    && musicFileDto.getComposition().contains(searchingFile.getTitle()))
                    || (musicFileDto.getVerse() != null
                    && musicFileDto.getVerse().contains(searchingFile.getTitle())
            );
        }
        return true;
 */
    }

    static List<Map.Entry<String, MusicFileDto>> getEntryList(MusicFileFinderBot bot, String query, int skip, int limit) {
        if (skip < 0){
            return new ArrayList<>();
        }
        SearchingFile searchingFile = SearchRegex.getSearchMusicFileDto(query);
        return bot.getFindStorage().getStringMusicFileMap()
                .entrySet().stream()
                .filter((entry) -> Searching.containTextInMusicFileDto(entry.getValue(), searchingFile))
                .sorted((e1, e2) -> new MusicFileDtoComparator().compare(e1.getValue(), e2.getValue()))
                .skip(skip)
                .limit(limit)
                .toList();
    }
}
