package com.serov.alex.bot.handler;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.executor.Executor;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
  void handle(MusicFileFinderBot bot, Executor executor, Update update);
}
