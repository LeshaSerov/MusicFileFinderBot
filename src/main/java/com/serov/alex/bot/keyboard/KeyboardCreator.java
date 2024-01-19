package com.serov.alex.bot.keyboard;

import com.serov.alex.bot.MusicFileFinderBot;
import com.serov.alex.bot.executor.ExecuteMode;
import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.regex.find.Searching;
import com.serov.alex.music.MusicFile;
import com.serov.alex.music.MusicFileDto;
import com.serov.alex.music.file.Prophecy;
import com.serov.alex.music.file.Worship;
import java.util.Objects;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.serov.alex.bot.constant.CallbackButtonConstant.*;
import static com.serov.alex.bot.constant.CallbackButtonConstant.buttonModeSeparator;

public interface KeyboardCreator {

    private static String getNameMusicFile(MusicFileDto file, int index) {
        StringBuilder stringBuilder = new StringBuilder(index + ". ");
        boolean isSep = false;
        if (file.getDay() != null) {
            stringBuilder.append(file.getDay());
            isSep = true;
        }
        if (file.getMonth() != null) {
            if (isSep) {
                stringBuilder.append("-");
            }
            isSep = true;
            stringBuilder.append(file.getMonth());
        }
        if (file.getYear() != null) {
            if (isSep) {
                stringBuilder.append("-");
            }
            isSep = true;
            stringBuilder.append(file.getYear());
        }
        if (file.isEvening()) {
            stringBuilder.append("-в ");
        }
        if (isSep) {
            stringBuilder.append(" ");
        }

        if (file.getNumber() != null) {
            stringBuilder.append(file.getNumber());
            if (file.getPart() != null) {
                stringBuilder.append(" ").append(file.getPart() + "часть ");
            }
        } else if (Objects.equals(file.getType(), new Worship().getType()))
        {
            if (file.getPart() != null) {
                stringBuilder.append(" ").append(file.getPart());
            }
        }

        if (file.getType() != null) {
          if (Objects.equals(file.getType(), new Prophecy().getType()))
            stringBuilder.append(file.getType()).append(" ");
        }

        if (file.getTitle() != null && !Objects.equals(file.getType(), new Worship().getType()))
            stringBuilder.append(file.getTitle());
        if (file.getComposition() != null)
            stringBuilder.append(" - ").append(file.getComposition());
        if (file.getVerse() != null)
            stringBuilder.append(" - ").append(file.getVerse());
        return stringBuilder.toString();
    }

    static void sendMessageWithKeyboard(MusicFileFinderBot bot, Executor executor, Update update, String query, int skip, boolean isNewMessage) {
        int count = numberOfMusicFilesDisplayed;
        List<Map.Entry<String, MusicFileDto>> entryList = Searching.getEntryList(bot, query, skip, count);
        StringBuilder stringBuilder = new StringBuilder("Результат поиска:\n");
        stringBuilder.append(query).append("\n\n");
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        keyboardList.add(buttonList);
        int index = skip + 1;
        for (Map.Entry<String, MusicFileDto> entry : entryList) {
            stringBuilder.append(getNameMusicFile(entry.getValue(), index)).append("\n");
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(Integer.toString(index++))
                    .callbackData(buttonFileData
                            + buttonModeSeparator
                            + skip
                            + buttonModeSeparator
                            + entry.getKey())
                    .build();
            buttonList.add(button);
            //List<InlineKeyboardButton> rowButtonsList = new ArrayList<>();
            //rowButtonsList.add(button);
            //keyboardList.add(rowButtonsList);
        }
        List<InlineKeyboardButton> menu = new ArrayList<>();
        if (skip > 0) {
            menu.add(InlineKeyboardButton.builder()
                    .text(buttonBackText)
                    .callbackData(buttonBackData
                            + buttonModeSeparator
                            + (skip - count))
                    .build());
        }
        menu.add(InlineKeyboardButton.builder()
                .text(buttonDeleteText)
                .callbackData(buttonDeleteData
                        + buttonModeSeparator
                        + skip)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(buttonDownloadText)
                .callbackData(buttonDownloadData
                        + buttonModeSeparator
                        + skip)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(buttonForwardText)
                .callbackData(buttonBackData
                        + buttonModeSeparator
                        + (skip + count))
                .build());
        keyboardList.add(menu);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboardList)
                .build();
        if (update.hasCallbackQuery() && isNewMessage) {
            SendMessage message = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getFrom().getId())
                    .text(stringBuilder.toString())
                    .replyMarkup(inlineKeyboardMarkup)
                    .build();
            executor.execute(message, ExecuteMode.LOUD);
        } else if (update.hasCallbackQuery()) {
            EditMessageText messageText = EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getFrom().getId())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text(stringBuilder.toString())
                    .replyMarkup(inlineKeyboardMarkup)
                    .build();
            executor.execute(messageText, ExecuteMode.LOUD);
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getFrom().getId())
                    .text(stringBuilder.toString())
                    .replyMarkup(inlineKeyboardMarkup)
                    .build();
            executor.execute(message, ExecuteMode.LOUD);
        }
    }
}
