package com.serov.alex.music;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class MusicFileLoader {
  public static List<MusicFile> processFilesAndCreateMusicFiles(String currentPath, String savedPath) {
    List<MusicFile> files = new ArrayList<>();
    String finalCurrentPath = currentPath + "\\";
    try (Stream<Path> paths = Files.walk(Paths.get(finalCurrentPath))) {
      FileUtils.deleteDirectory(new File(savedPath));
      paths.filter(Files::isRegularFile)
          .map(path -> path.toString().replace(finalCurrentPath, ""))
          .forEach(x -> {
            StringBuilder savePath = new StringBuilder(savedPath);
            StringBuilder filePath = new StringBuilder(finalCurrentPath);
            while (x.contains("\\")) {
              savePath.append("\\").append(x, 0, x.indexOf("\\"));
              filePath.append("\\").append(x, 0, x.indexOf("\\"));
              x = x.substring(x.indexOf("\\") + 1);
            }
            files.add(MusicFile.createMusicFile(x, filePath.toString(), savePath.toString()));
          });
    } catch (IOException e) {
      log.atWarn().log(e.getMessage());
    }
    return files;
  }

}
