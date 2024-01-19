package com.serov.alex.music.file;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.serov.alex.music.MusicFile;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prophecy extends MusicFile {

  @Override
  public String getType() {
    return "Пророчество";
  }

  @Override
  public String getDeterminantType() {
    return "x";
  }

  @Override
  public String getRegex() {
    return MusicFile.START_REGEX + " Пророчество (.+).mp3$";
  }

  @Override
  protected void setAllTheAttributes(String... args) {
    super.setAttributesByDefault(args);
    this.setTitle(args[4]);
  }

  @Override
  protected Mp3File createMp3FileWithAllTheAttributes(String sourcePath, String nameFile)
      throws InvalidDataException, UnsupportedTagException, IOException {
    Mp3File mp3File = super.createMp3FileByDefault(sourcePath, nameFile);
    ID3v2 id3v2 = mp3File.getId3v2Tag();
    mp3File.setId3v2Tag(id3v2);
    id3v2.setTitle(this.getTitle());
    id3v2.setAlbum("Общие пророчества " + this.getCreationDate().getYear());
    id3v2.setArtist(this.getTitle());
    return mp3File;
  }

  @Override
  protected String determineTheNameOfTheMusicFile(MusicFile musicFile) {
    return this.toAlphabetic(this.getCreationDate().getMonthValue())
        + this.getCreationDate().getDayOfMonth()
        + " "
        + (this.isEvening() ? "e " : "")
        + getDeterminantType()
        + " "
        + this.getTitle();
  }

  @Override
  protected String defineASpecialPath(String savePath, MusicFile musicFile) {
    return savePath + "//Пророчества//";
  }

}
