package com.serov.alex.music;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MusicFileHashCode {

  public static String getHashCode(String path) {
    try {
      byte[] data = Files.readAllBytes(Paths.get(path));
      byte[] hash = MessageDigest.getInstance("MD5").digest(data);
      return new BigInteger(1, hash).toString(16);
    } catch (IOException | NoSuchAlgorithmException e) {
      return "";
    }
  }

}
