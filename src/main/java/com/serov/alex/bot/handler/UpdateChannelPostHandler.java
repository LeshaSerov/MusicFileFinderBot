package com.serov.alex.bot.handler;

import static com.serov.alex.bot.executor.ExecuteMode.LOUD;

import com.google.gson.Gson;
import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.constant.MusicFileLoadingConstant;
import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.handler.condition.Condition;
import com.serov.alex.bot.state.BotState;
import com.serov.alex.bot.storage.TempStorage;
import com.serov.alex.music.MusicFileDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class UpdateChannelPostHandler implements UpdateHandler {

  final Condition isSendSpecialBotInSpecialChannel = (bot, update) -> {
    /*if (Long.toString(update.getChannelPost().getChat().getId()).equals(bot.getChannelId())) {
      return update.getChannelPost().getAuthorSignature()
          .equals(MusicFileLoadingConstant.SPECIAL_BOT);
    }*/
    return Long.toString(update.getChannelPost().getChat().getId()).equals(bot.getChannelId());
  };

  final Condition isStartLoading = (bot, update) -> {
    if (update.getChannelPost().getText() != null) {
      return update.getChannelPost().getText().equals(MusicFileLoadingConstant.RUN);
    }
    return false;
  };

  final Condition isEndLoading = (bot, update) -> {
    if (bot.getState() == BotState.LOADING_RUN || bot.getState() == BotState.LOADING_WAIT_FILE) {
      if (update.getChannelPost().getText() != null) {
        return update.getChannelPost().getText().equals(MusicFileLoadingConstant.END);
      }
    }
    return false;
  };

  final Condition isInputHashCodeFile = (bot, update) -> {
    if (bot.getState() == BotState.LOADING_RUN) {
      return update.getChannelPost().getText() != null;
    }
    return false;
  };

  final Condition isErrorSendFile = (bot, update) -> {
    if (bot.getState() == BotState.LOADING_WAIT_FILE) {
      if (update.getChannelPost().getText() != null) {
        return update.getChannelPost().getText()
            .equals(MusicFileLoadingConstant.ERROR_LOADING_FILE);
      }
    }
    return false;
  };

  final Condition isInputFile = (bot, update) -> {
    if (bot.getState() == BotState.LOADING_WAIT_FILE) {
      return update.getChannelPost().getAudio() != null;
    }
    return false;
  };

  private DeleteMessage getMethodDeleteInputMessage(Update update) {
    return DeleteMessage.builder()
        .chatId(update.getChannelPost().getChatId())
        .messageId(update.getChannelPost().getMessageId())
        .build();
  }

  private DeleteMessage getMethodDeleteMessageForId(Update update, int id) {
    return DeleteMessage.builder()
        .chatId(update.getChannelPost().getChatId())
        .messageId(id)
        .build();
  }

  @Override
  public void handle(MusicFileFinderBot bot, Executor executor, Update update) {
    if (isSendSpecialBotInSpecialChannel.check(bot, update)) {
      if (isStartLoading.check(bot, update)) {
        executor.execute(getMethodDeleteInputMessage(update), LOUD);
        bot.setState(BotState.LOADING_RUN);
        bot.setTempStorage(new TempStorage());
        executor.execute(new SendMessage(bot.getChannelId(), MusicFileLoadingConstant.FILE_EXIST),
            LOUD);
      } else if (isEndLoading.check(bot, update)) {
        executor.execute(getMethodDeleteInputMessage(update), LOUD);
        bot.setState(BotState.SEARCH);
        List<Integer> removedIdMessages = bot.getStorage()
            .saveLoadingMusicFiles(bot.getTempStorage());
        removedIdMessages.forEach(
            i -> executor.execute(getMethodDeleteMessageForId(update, i), LOUD));
        bot.setTempStorage(null);
        log.atInfo().log("Update saved!");
        bot.getFindStorage().setStringMusicFileMap(bot.getStorage().getStringMusicFileMap());
      } else if (isInputHashCodeFile.check(bot, update)) {
        executor.execute(getMethodDeleteInputMessage(update), LOUD);
        boolean containHashCode = bot.getStorage()
            .containHashCode(update.getChannelPost().getText());
        if (containHashCode) {
          bot.getTempStorage().getHashCodeList().add(update.getChannelPost().getText());
          executor.execute(new SendMessage(bot.getChannelId(), MusicFileLoadingConstant.FILE_EXIST),
              LOUD);
        } else {
          executor.execute(
              new SendMessage(bot.getChannelId(), MusicFileLoadingConstant.FILE_NOT_EXIST), LOUD);
          bot.setState(BotState.LOADING_WAIT_FILE);
        }
      } else if (isErrorSendFile.check(bot, update)) {
        executor.execute(getMethodDeleteInputMessage(update), LOUD);
        executor.execute(
            new SendMessage(bot.getChannelId(), MusicFileLoadingConstant.FILE_DOWNLOADED), LOUD);
        bot.setState(BotState.LOADING_RUN);
      } else if (isInputFile.check(bot, update)) {
        executor.execute(
            new SendMessage(bot.getChannelId(), MusicFileLoadingConstant.FILE_DOWNLOADED), LOUD);
        bot.setState(BotState.LOADING_RUN);
        try {
          Integer id = update.getChannelPost().getMessageId();
          String[] res = update.getChannelPost().getCaption().split("@");
          String hashCode = res[0];
          String json = res[1];
          MusicFileDto musicFileDTO = new Gson().fromJson(json, MusicFileDto.class);
          musicFileDTO.setSavePath(update.getChannelPost().getAudio().getFileId());
          bot.getTempStorage().getHashCodeList().add(hashCode);
          bot.getTempStorage().getMusicFileMap().put(hashCode, musicFileDTO);
          bot.getTempStorage().getStringIntegerMap().put(hashCode, id);
        } catch (Exception e) {
          executor.execute(getMethodDeleteInputMessage(update), LOUD);
          log.atInfo().log("Exception in deserialization: " + e.getMessage());
        }
      }
    }
  }
}
