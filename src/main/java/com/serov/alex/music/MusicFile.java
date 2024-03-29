package com.serov.alex.music;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MusicFile {

  private String type;
  private String nameFile;
  private String savePath;


  private String title;
  private boolean isEvening;
  private LocalDate creationDate;

  protected static final String START_REGEX = "^(\\d\\d)\\-(\\d\\d)\\-(\\d\\d)(-в){0,1}";

  public static MusicFile createMusicFile(String nameFile, String sourcePath,
      String savePath) {
    try {
      MusicFile musicFile = MusicFileIdentifier.determineType(nameFile);
      Mp3File mp3File = musicFile.createMp3FileWithAllTheAttributes(sourcePath, nameFile);
      musicFile.nameFile = musicFile.determineTheNameOfTheMusicFile(musicFile);
      musicFile.type = musicFile.getClass().getSimpleName();
      for (String directoryToSave : musicFile.getThePathsToSave(savePath, musicFile)) {
        try {
          Path pathMusicFile = Path.of(directoryToSave + "//"
              + musicFile.determineTheNameOfTheMusicFile(musicFile) + ".mp3");
          musicFile.ensurePathExists(Path.of(directoryToSave));
          mp3File.save(pathMusicFile.toString());
          musicFile.setCreationDateForMusicFile(pathMusicFile);
        } catch (IOException e) {
          log.atInfo().log(e.getMessage());
        }
      }
      musicFile.savePath = savePath + "//"
          + musicFile.determineTheNameOfTheMusicFile(musicFile) + ".mp3";
      log.atInfo().log("Load musicFile: " + musicFile.getType() + "@" + musicFile.getNameFile());
      return musicFile;
    } catch (Exception e) {
      log.atWarn().log(e.getMessage());
    }
    return null;
  }

  protected String toAlphabetic(int i) {
    i++;
    if (i < 0) {
      return "-" + toAlphabetic(-i - 1);
    } else if (i < 26) {
      return "" + (char) ('A' + i);
    } else {
      return toAlphabetic(i / 26 - 1) + (char) ('A' + i % 26);
    }
  }

  public abstract String getType();
  public abstract String getDeterminantType();

  protected abstract String getRegex();

  protected abstract void setAllTheAttributes(String... args);

  protected void setAttributesByDefault(String... args) {
    this.setCreationDate(
        LocalDate.of(
            Integer.parseInt("20" + args[2]),
            Integer.parseInt(args[1]),
            Integer.parseInt(args[0])
        )
    );
    this.setEvening(args[3] != null && args[3].contains("-в"));
  }

  protected abstract Mp3File createMp3FileWithAllTheAttributes(String sourcePath, String nameFile)
      throws InvalidDataException, UnsupportedTagException, IOException;

  protected Mp3File createMp3FileByDefault(String sourcePath, String nameFile)
      throws InvalidDataException, UnsupportedTagException, IOException {
    Mp3File mp3File = new Mp3File(sourcePath + "//" + nameFile);
    ID3v2 id3v2 = new ID3v24Tag();
    mp3File.setId3v2Tag(id3v2);
    id3v2.setEncoder(EncodedText.CHARSET_UTF_8);
    id3v2.setTitle(this.getTitle());
    id3v2.setYear(this.getCreationDate().getYear() + "");
    return mp3File;
  }

  protected abstract String determineTheNameOfTheMusicFile(MusicFile musicFile);

  protected String toStringCreationDate() {
    String dayToString = ""
        + (this.getCreationDate().getDayOfMonth() < 10
        ? "0" + this.getCreationDate().getDayOfMonth()
        : this.getCreationDate().getDayOfMonth());
    String monthToString = ""
        + (this.getCreationDate().getMonthValue() < 10
        ? "0" + this.getCreationDate().getMonthValue()
        : this.getCreationDate().getMonthValue());
    return " - "
        + dayToString
        + "."
        + monthToString
        + "."
        + this.getCreationDate().getYear();
  }

  protected abstract String defineASpecialPath(String savePath, MusicFile musicFile);

  protected String[] getThePathsToSave(String savePath, MusicFile musicFile) {
    //String basePathToTheMusicFile = savePath;
    String specialPathToTheMusicFile = musicFile.defineASpecialPath(savePath, musicFile);
    return new String[]{savePath, specialPathToTheMusicFile};
  }

  private void ensurePathExists(Path path) {
    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
      } catch (IOException e) {
        throw new RuntimeException("Failed to create directories: " + path, e);
      }
    }
  }

  private void setCreationDateForMusicFile(Path pathFile) {
    try {
      Instant instant = creationDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
      Files.setAttribute(pathFile, "basic:creationTime",
          FileTime.from(instant), NOFOLLOW_LINKS);
      Files.setAttribute(pathFile, "basic:lastModifiedTime",
          FileTime.from(instant), NOFOLLOW_LINKS);
      File file = new File(pathFile.toString());
      if (!file.setReadOnly()) {
        throw new RuntimeException("Cannot set read-only property.",
            new IOException(pathFile.toString()));
      }
    } catch (IOException e) {
      throw new RuntimeException("Cannot change the creation time.", e);
    }
  }

}
