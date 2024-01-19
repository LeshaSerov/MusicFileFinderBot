package com.serov.alex.bot.handler.cap;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.executor.ExecuteMode;
import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.handler.UpdateHandler;
import com.serov.alex.bot.handler.condition.Condition;
import com.serov.alex.music.MusicFileDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultAudio;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultsButton;

public class UpdateChosenInlineQueryHandler implements
    UpdateHandler {


  @Override
  public void handle(MusicFileFinderBot bot, Executor executor, Update update) {

  }
}
