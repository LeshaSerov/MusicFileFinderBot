package com.serov.alex.bot.handler;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.constant.CallbackButtonConstant;
import com.serov.alex.bot.executor.ExecuteMode;
import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.handler.condition.Condition;
import com.serov.alex.bot.keyboard.KeyboardCreator;
import com.serov.alex.bot.state.BotState;
import com.serov.alex.bot.state.KeyboardState;
import com.serov.alex.bot.regex.find.Searching;
import com.serov.alex.music.MusicFileDto;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Objects;

public class UpdateCallbackQueryHandler implements
        UpdateHandler {

    Condition isAllowed = (bot, update) -> bot.getStorage().getLongIdSet()
            .contains(update.getCallbackQuery().getFrom().getId());

    Condition isChatBot = (bot, update) -> {
        if (Objects.equals(update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getFrom().getId()))
            return bot.getState() == BotState.SEARCH;
        return false;
    };

    private static DeleteMessage getMethodDeleteInputMessage(Update update) {
        return DeleteMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build();
    }

    @Override
    public void handle(MusicFileFinderBot bot, Executor executor, Update update) {
        if (isAllowed.check(bot, update) && isChatBot.check(bot, update)) {
            KeyboardState state = KeyboardState.getState(update.getCallbackQuery().getData());
            String query = update.getCallbackQuery().getMessage().getText().split("\n")[1];
            int skip = Integer.parseInt(update.getCallbackQuery().getData().split(CallbackButtonConstant.buttonModeSeparator)[1]);

            switch (state) {
                case Forward, Back -> {
                    KeyboardCreator.sendMessageWithKeyboard(bot, executor, update, query, skip, false);
                }
                case Download -> {
                    executor.execute(getMethodDeleteInputMessage(update), ExecuteMode.LOUD);
                    for (Map.Entry<String, MusicFileDto> entry : Searching.getEntryList(bot,
                            query, skip, CallbackButtonConstant.numberOfMusicFilesDisplayed)) {
                        InputFile inputFile = new InputFile(entry.getValue().getSavePath());
                        SendAudio sendAudio = SendAudio.builder()
                                .chatId(update.getCallbackQuery().getFrom().getId())
                                .audio(inputFile)
                                .build();
                        if (!executor.sendAudio(sendAudio))
                            break;
                    }
                    KeyboardCreator.sendMessageWithKeyboard(bot, executor, update, query, skip, true);
                }
                case File -> {
                    executor.execute(getMethodDeleteInputMessage(update), ExecuteMode.LOUD);
                    String key = update.getCallbackQuery().getData().split(CallbackButtonConstant.buttonModeSeparator)[2];
                    InputFile inputFile = new InputFile(bot.getFindStorage().getStringMusicFileMap().get(key).getSavePath());
                    SendAudio sendAudio = SendAudio.builder()
                            .chatId(update.getCallbackQuery().getFrom().getId())
                            .audio(inputFile)
                            .build();
                    executor.sendAudio(sendAudio);
                    KeyboardCreator.sendMessageWithKeyboard(bot, executor, update, query, skip, true);
                }
                case Delete -> {
                    executor.execute(getMethodDeleteInputMessage(update), ExecuteMode.LOUD);
                }
            }

        }

    }
}

