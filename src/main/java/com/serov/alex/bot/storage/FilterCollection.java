package com.serov.alex.bot.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FilterCollection {

  public static  <K, V> List<V> filterCollectionByKeys(Map<K, V> collection, List<K> keysToSave) {
    List<V> removedValues = new ArrayList<>();
    Iterator<Entry<K, V>> iterator = collection.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<K, V> entry = iterator.next();
      if (!keysToSave.contains(entry.getKey())) {
        removedValues.add(entry.getValue());
        iterator.remove();
      }
    }
    return removedValues;
  }

}
