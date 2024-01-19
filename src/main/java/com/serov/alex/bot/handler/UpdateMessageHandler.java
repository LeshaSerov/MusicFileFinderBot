package com.serov.alex.bot.handler;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.executor.ExecuteMode;
import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.handler.condition.Condition;
import com.serov.alex.bot.keyboard.KeyboardCreator;
import com.serov.alex.bot.state.BotState;
import com.serov.alex.bot.regex.find.Searching;
import com.serov.alex.music.MusicFileDto;
import java.util.Map.Entry;
import java.util.Objects;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateMessageHandler implements UpdateHandler {

  Condition isSendMusicFile = (bot, update) -> {
    if (update.getMessage().getText() == null) {
      return false;
    } else if (!update.getMessage().getText().startsWith("/start")) {
      return false;
    }
    int id = Integer.parseInt(update.getMessage().getText().replaceAll("/start ", ""));
    return bot.getFindStorage().getSearchMap().containsKey(id);
  };

  Condition isTrustedChat = (bot, update) -> {
    return update.getMessage().getChatId().equals(bot.getTrustedChatId());
  };

  Condition isCleanTrustedMembers = (bot, update) -> {
    if (update.getMessage().getText() == null) {
      return false;
    } else if (!update.getMessage().getText().startsWith("/clean")) {
      return false;
    } else {
      return Objects.equals(bot.getTrustedChatId(), update.getMessage().getChatId());
    }
  };

  Condition isChatBot = (bot, update) -> {
      if (Objects.equals(update.getMessage().getChatId(), update.getMessage().getFrom().getId())) {
          return bot.getState() == BotState.SEARCH;
      }
    return false;
  };

  Condition isAllowed = (bot, update) -> bot.getStorage().getLongIdSet()
      .contains(update.getMessage().getFrom().getId());

  @Override
  public void handle(MusicFileFinderBot bot, Executor executor, Update update) {
    if (isTrustedChat.check(bot, update)) {
      if (isCleanTrustedMembers.check(bot, update)) {
        bot.getStorage().clear();
        executor.execute(
            new SendMessage(Long.toString(update.getMessage().getChatId()), "Список очищен"),
            ExecuteMode.LOUD);
      } else {
        bot.getStorage().add(update.getMessage().getFrom().getId());
      }
    }
    if (isAllowed.check(bot, update)) {
      if (isSendMusicFile.check(bot, update)) {
        int id = Integer.parseInt(update.getMessage().getText().replaceAll("/start ", ""));
        String query = bot.getFindStorage().getSearchMap().get(id);
        executor.execute(new SendMessage(Long.toString(update.getMessage().getChatId()), query),
            ExecuteMode.LOUD);
        for (Entry<String, MusicFileDto> entry : Searching.getEntryList(bot,
            query, 0, 50)) {
          InputFile inputFile = new InputFile(entry.getValue().getSavePath());
          SendAudio sendAudio = SendAudio.builder()
              .chatId(update.getMessage().getChatId())
              .audio(inputFile)
              .build();
          executor.sendAudio(sendAudio);
        }
      } else if (isChatBot.check(bot, update)) {
        String query = update.getMessage().getText();
          while (query.endsWith(" ")) {
              query = query.substring(0, query.length() - 1);
          }
        KeyboardCreator.sendMessageWithKeyboard(bot, executor, update, query, 0, true);
        //executor.execute(new SendMessage(Long.toString(update.getMessage().getChatId()), update.getMessage().getText()), ExecuteMode.LOUD);
      }
    }
  }
}

