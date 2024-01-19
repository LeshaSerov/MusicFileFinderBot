package com.serov.alex.bot.storage;

import com.serov.alex.music.MusicFileDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TempStorage {
  List<String> hashCodeList;
  Map<String, MusicFileDto> musicFileMap;
  Map<String, Integer> stringIntegerMap;

  public TempStorage() {
    hashCodeList = new ArrayList<>();
    musicFileMap = new HashMap<>();
    stringIntegerMap = new HashMap<>();
  }
}
