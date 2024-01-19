package com.serov.alex.bot.handler.determinant;

import com.serov.alex.exception.UnknownUpdateTypeException;
import org.telegram.telegrambots.meta.api.objects.Update;

public enum UpdateType {
  MESSAGE,
  INLINE_QUERY,
  CHOSEN_INLINE_QUERY,
  CALLBACK_QUERY,
  EDITED_MESSAGE,
  CHANNEL_POST,
  EDITED_CHANNEL_POST,
  SHIPPING_QUERY,
  PRE_CHECKOUT_QUERY,
  POLL,
  POLL_ANSWER,
  MY_CHAT_MEMBER,
  CHAT_MEMBER,
  CHAT_JOIN_REQUEST;

  public static UpdateType getType(Update update) {
    if (update.hasMessage())
      return MESSAGE;
    if (update.hasInlineQuery())
      return INLINE_QUERY;
    if (update.hasChosenInlineQuery())
      return CHOSEN_INLINE_QUERY;
    if (update.hasCallbackQuery())
      return CALLBACK_QUERY;
    if (update.hasEditedMessage())
      return EDITED_MESSAGE;
    if (update.hasChannelPost())
      return CHANNEL_POST;
    if (update.hasEditedChannelPost())
      return EDITED_CHANNEL_POST;
    if (update.hasShippingQuery())
      return SHIPPING_QUERY;
    if (update.hasPreCheckoutQuery())
      return PRE_CHECKOUT_QUERY;
    if (update.hasPoll())
      return POLL;
    if (update.hasPollAnswer())
      return POLL_ANSWER;
    if (update.hasMyChatMember())
      return MY_CHAT_MEMBER;
    if (update.hasChatMember())
      return CHAT_MEMBER;
    if (update.hasChatJoinRequest())
      return CHAT_JOIN_REQUEST;
    throw new UnknownUpdateTypeException(update);
  }
}
