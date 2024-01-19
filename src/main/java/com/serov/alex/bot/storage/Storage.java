package com.serov.alex.bot.storage;

import com.serov.alex.music.MusicFileDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.telegram.abilitybots.api.db.DBContext;


@Getter
@Setter
public class Storage {

  private final Map<String, MusicFileDto> stringMusicFileMap;
  private final Map<String, Integer> stringIntegerMap;
  private final Set<Long> longIdSet;

  DBContext dbContext;

  public Storage(DBContext db) {
    stringMusicFileMap = db.getMap("stringMusicFileMap");
    stringIntegerMap = db.getMap("stringIntegerMap");
    longIdSet = db.getSet("longIdList");
    dbContext = db;
  }

  public boolean containHashCode(String text) {
    return stringMusicFileMap.containsKey(text);
  }

  public List<Integer> saveLoadingMusicFiles(TempStorage tempStorage) {
    List<Integer> removeList = FilterCollection.filterCollectionByKeys(stringIntegerMap, tempStorage.getHashCodeList());
    FilterCollection.filterCollectionByKeys(stringMusicFileMap, tempStorage.getHashCodeList());
    stringMusicFileMap.putAll(tempStorage.getMusicFileMap());
    stringIntegerMap.putAll(tempStorage.getStringIntegerMap());
    dbContext.commit();
    return removeList;
  }

  public void add(Long id){
    if (longIdSet.add(id))
      dbContext.commit();
  }

  public void clear(){
    longIdSet.clear();
    dbContext.commit();
  }
}
