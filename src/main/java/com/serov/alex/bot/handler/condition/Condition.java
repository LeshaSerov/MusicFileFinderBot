package com.serov.alex.bot.handler.condition;

import com.serov.alex.bot.MusicFileFinderBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface Condition {

  boolean check(MusicFileFinderBot bot, Update update);

}
