package com.serov.alex.bot.regex.find;

import com.serov.alex.music.MusicFileDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindStorage {

  private Map<String, MusicFileDto> stringMusicFileMap;
  private Map<Integer, String> searchMap;

  public FindStorage(Map<String, MusicFileDto> stringMusicFileMap) {
    this.stringMusicFileMap = stringMusicFileMap;
    searchMap = new HashMap<>();
  }

  public int add(String update_id, String query) {
    int id = Objects.hash(update_id.hashCode());
    searchMap.put(id, query);
    return id;
  }
}
