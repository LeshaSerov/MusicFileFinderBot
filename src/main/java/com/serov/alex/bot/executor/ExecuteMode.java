package com.serov.alex.bot.executor;

public enum ExecuteMode {
  LOUD(false),
  QUIET(true);
  public final boolean isQuiet;

  ExecuteMode(boolean b) {
    isQuiet = b;
  }
}
