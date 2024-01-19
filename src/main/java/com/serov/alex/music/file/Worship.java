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
public class Worship extends MusicFile {

  private String part;
  private String about;

  @Override
  public String getType() {
    return "Служение";
  }

  @Override
  public String getDeterminantType() {
    return "~";
  }

  @Override
  protected String getRegex() {
    return MusicFile.START_REGEX + "( ~\\d\\d){0,1} {0,1}(.*)\\.mp3$";
  }

  @Override
  protected void setAllTheAttributes(String... args) {
    super.setAttributesByDefault(args);
    this.part = args[4] != null ? args[4].substring(2) : "";
    if (args[5] != null) {
      this.setAbout(args[5]);
    }
  }

  @Override
  protected Mp3File createMp3FileWithAllTheAttributes(String sourcePath, String nameFile)
      throws InvalidDataException, UnsupportedTagException, IOException {
    Mp3File mp3File = super.createMp3FileByDefault(sourcePath, nameFile);
    ID3v2 id3v2 = mp3File.getId3v2Tag();
    mp3File.setId3v2Tag(id3v2);
    id3v2.setAlbum("Служения (" + this.getCreationDate().getYear() + ")");
    id3v2.setTitle("" + getPart());
    id3v2.setTrack(this.part);
    return mp3File;
  }

  @Override
  protected String determineTheNameOfTheMusicFile(MusicFile musicFile) {
    return this.toAlphabetic(this.getCreationDate().getMonthValue())
        + this.getCreationDate().getDayOfMonth()
        + " "
        + (this.isEvening() ? "e " : "")
        + getDeterminantType()
        + (this.getPart() != null ? " " + this.getPart(): "")
        + (this.getAbout() != null ? " " + this.getAbout(): "");
  }

  @Override
  protected String defineASpecialPath(String savePath, MusicFile musicFile) {
    String s = "" + this.getCreationDate().getMonthValue();
    s = s.length() < 2 ? "0" + s: s;
    return savePath + "//Служения//" + s + "//";
  }
}
