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
public class Speech extends MusicFile {

  private String number;
  private String part;

  @Override
  public String getType() {
    return "Слово";
  }

  @Override
  public String getDeterminantType() {
    return "a";
  }

  @Override
  public String getRegex() {
    return MusicFile.START_REGEX + " (\\d\\d)(-\\d\\d){0,1} (.+)\\.mp3$";
  }

  @Override
  protected void setAllTheAttributes(String... args) {
    super.setAttributesByDefault(args);
    this.setNumber(args[4]);
    this.setPart(args[5] != null ? "@" + args[5].substring(1) : "");
    this.setTitle(args[6]);
  }

  @Override
  protected Mp3File createMp3FileWithAllTheAttributes(String sourcePath, String nameFile)
      throws InvalidDataException, UnsupportedTagException, IOException {
    Mp3File mp3File = super.createMp3FileByDefault(sourcePath, nameFile);
    ID3v2 id3v2 = mp3File.getId3v2Tag();
    mp3File.setId3v2Tag(id3v2);
    id3v2.setTitle(this.getNumber() + this.getPart());
    id3v2.setAlbum("Проповеди (" + this.getCreationDate().getYear() + ")");
    id3v2.setArtist(this.getTitle());
    id3v2.setTrack(this.getNumber());
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
        + this.getNumber()
        + this.getPart()
        + " "
        + this.getTitle();
  }

  @Override
  protected String defineASpecialPath(String savePath, MusicFile musicFile) {
    return savePath + "//Проповеди//" + this.getTitle() + "//";
  }

}
