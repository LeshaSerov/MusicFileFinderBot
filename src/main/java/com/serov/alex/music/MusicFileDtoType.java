package com.serov.alex.music;


import com.serov.alex.music.file.Action;
import com.serov.alex.music.file.Prophecy;
import com.serov.alex.music.file.Song;
import com.serov.alex.music.file.Speech;
import com.serov.alex.music.file.Verse;
import com.serov.alex.music.file.Worship;

public class MusicFileDtoType {

  static Action action = new Action();
  static Song song = new Song();
  static Speech speech = new Speech();
  static Prophecy prophecy = new Prophecy();
  static Verse verse = new Verse();
  static Worship worship = new Worship();

  public static String getType(String s){
    if (s == null || s.isEmpty())
      return null;
    if (action.getType().contains(s) || action.getDeterminantType().contains(s))
      return action.getType();
    if (song.getType().contains(s) || song.getDeterminantType().contains(s))
      return song.getType();
    if (speech.getType().contains(s) || speech.getDeterminantType().contains(s))
      return speech.getType();
    if (prophecy.getType().contains(s) || prophecy.getDeterminantType().contains(s))
      return prophecy.getType();
    if (verse.getType().contains(s) || verse.getDeterminantType().contains(s))
      return verse.getType();
    if (worship.getType().contains(s) || worship.getDeterminantType().contains(s))
      return worship.getType();

    if (action.getType().toLowerCase().contains(s) || action.getDeterminantType().toLowerCase().contains(s))
      return action.getType();
    if (song.getType().toLowerCase().contains(s) || song.getDeterminantType().toLowerCase().contains(s))
      return song.getType();
    if (speech.getType().toLowerCase().contains(s) || speech.getDeterminantType().toLowerCase().contains(s))
      return speech.getType();
    if (prophecy.getType().toLowerCase().contains(s) || prophecy.getDeterminantType().toLowerCase().contains(s))
      return prophecy.getType();
    if (verse.getType().toLowerCase().contains(s) || verse.getDeterminantType().toLowerCase().contains(s))
      return verse.getType();
    if (worship.getType().toLowerCase().contains(s) || worship.getDeterminantType().toLowerCase().contains(s))
      return worship.getType();

    return null;
  }

}
