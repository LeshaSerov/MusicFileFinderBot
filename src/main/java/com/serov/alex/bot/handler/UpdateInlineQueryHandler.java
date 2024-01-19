package com.serov.alex.bot.handler;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.handler.condition.Condition;
import com.serov.alex.bot.regex.find.Searching;
import com.serov.alex.music.MusicFileDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultAudio;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultsButton;

@Slf4j
public class UpdateInlineQueryHandler implements UpdateHandler {

  Condition isAllowed = (bot, update) -> bot.getStorage().getLongIdSet()
      .contains(update.getInlineQuery().getFrom().getId());

  @Override
  public void handle(MusicFileFinderBot bot, Executor executor, Update update) {
    if (isAllowed.check(bot, update)) {
      String query = update.getInlineQuery().getQuery();
      List<Entry<String, MusicFileDto>> entryList = Searching.getEntryList(bot, query, 0, 50);
      List<InlineQueryResult> resultList = new ArrayList<>();
      IntStream.range(0, entryList.size())
          .peek(i -> {
            resultList.add(InlineQueryResultAudio.builder()
                .audioUrl(entryList.get(i).getValue().getSavePath())
                .id(Integer.toString(i + 1))
                .title(entryList.get(i).getValue().getTitle())
                .build());
          }).toArray();

      AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
      answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());
      answerInlineQuery.setResults(resultList);
      answerInlineQuery.setIsPersonal(true);
      if (entryList.size() < 45 && entryList.size() > 0) {
        int id = bot.getFindStorage().add(update.getInlineQuery().getId(), query);
        answerInlineQuery.setButton(InlineQueryResultsButton.builder().text(query)
            .startParameter(Integer.toString(id)).build());
      }
      try {
        bot.execute(answerInlineQuery);
      } catch (Exception e) {
        log.atInfo().log("AnswerInlineQuery Error: ", resultList);
        e.printStackTrace();
      }
    }
  }


}
