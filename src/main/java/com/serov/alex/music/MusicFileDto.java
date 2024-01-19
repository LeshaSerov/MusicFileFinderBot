package com.serov.alex.music;

import com.serov.alex.music.file.Action;
import com.serov.alex.music.file.Common;
import com.serov.alex.music.file.Prophecy;
import com.serov.alex.music.file.Song;
import com.serov.alex.music.file.Speech;
import com.serov.alex.music.file.Verse;
import com.serov.alex.music.file.Worship;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MusicFileDto implements Serializable {

  private String type;
  private String nameFile;
  private String title;
  private String composition;
  private String number;
  private String part;
  private String verse;
  private String savePath;
  private String day;
  private String month;
  private String year;
  private boolean evening;

  public MusicFileDto(MusicFile musicFile) {
    nameFile = musicFile.getNameFile();
    type = musicFile.getType();
    title = musicFile.getTitle();
    evening = musicFile.isEvening();
    savePath = musicFile.getSavePath();
    year = String.valueOf(musicFile.getCreationDate().getYear());
    month = String.valueOf(musicFile.getCreationDate().getMonthValue());
    day = String.valueOf(musicFile.getCreationDate().getDayOfMonth());
    if (musicFile instanceof Common) {
    } else if (musicFile instanceof Action) {
    } else if (musicFile instanceof Prophecy) {
    } else if (musicFile instanceof Song) {
      composition = ((Song) musicFile).getComposition();
    } else if (musicFile instanceof Speech) {
      number = ((Speech) musicFile).getNumber();
      part = ((Speech) musicFile).getPart();
    } else if (musicFile instanceof Verse) {
      verse = ((Verse) musicFile).getVerse();
    } else if (musicFile instanceof Worship)
    {
      part = ((Worship) musicFile).getPart();
    }
  }

}
