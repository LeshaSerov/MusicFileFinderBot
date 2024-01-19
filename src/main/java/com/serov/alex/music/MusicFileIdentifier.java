package com.serov.alex.music;

import com.serov.alex.music.file.Action;
import com.serov.alex.music.file.Common;
import com.serov.alex.music.file.Prophecy;
import com.serov.alex.music.file.Song;
import com.serov.alex.music.file.Speech;
import com.serov.alex.music.file.Worship;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class MusicFileIdentifier {

  public static MusicFile determineType(String nameFile) {
    List<MusicFile> list = List.of(new Action(), new Song(), new Speech(), new Prophecy()
        , new Worship()
        , new Common()
    );
    Optional<MusicFile> optionalMusicFile = list.stream()
        .filter(Objects::nonNull)
        .filter(x -> Pattern.matches(x.getRegex(), nameFile))
        .findFirst();
    if (optionalMusicFile.isPresent()) {
      MusicFile musicFile = optionalMusicFile.get();
      Matcher matcher = Pattern.compile(musicFile.getRegex()).matcher(nameFile);
      if (matcher.find()) {
        String[] attributes = IntStream
            .rangeClosed(1, matcher.groupCount())
            .mapToObj(matcher::group)
            .toArray(String[]::new);
        musicFile.setAllTheAttributes(attributes);
        return musicFile;
      } else {
        throw new RuntimeException("Could not determine file type attributes: " + nameFile);
      }
    } else {
      throw new RuntimeException("Could not determine file type: " + nameFile);
    }
  }
}
