package com.serov.alex.bot.regex;

import com.serov.alex.music.MusicFileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchingFile extends MusicFileDto {
  boolean isMorning;
  Integer dayOfWeek;
}
