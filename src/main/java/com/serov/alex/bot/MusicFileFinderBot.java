package com.serov.alex.bot;

import com.serov.alex.bot.executor.Executor;
import com.serov.alex.bot.handler.UpdateCallbackQueryHandler;
import com.serov.alex.bot.handler.UpdateChannelPostHandler;
import com.serov.alex.bot.handler.UpdateHandler;
import com.serov.alex.bot.handler.UpdateInlineQueryHandler;
import com.serov.alex.bot.handler.UpdateMessageHandler;
import com.serov.alex.bot.handler.cap.UpdateChatJoinRequestHandler;
import com.serov.alex.bot.handler.cap.UpdateChatMemberHandler;
import com.serov.alex.bot.handler.cap.UpdateChosenInlineQueryHandler;
import com.serov.alex.bot.handler.cap.UpdateEditedChannelPostHandler;
import com.serov.alex.bot.handler.cap.UpdateEditedMessageHandler;
import com.serov.alex.bot.handler.cap.UpdateMyChatMemberHandler;
import com.serov.alex.bot.handler.cap.UpdatePollAnswerHandler;
import com.serov.alex.bot.handler.cap.UpdatePollHandler;
import com.serov.alex.bot.handler.cap.UpdatePreCheckoutQueryHandler;
import com.serov.alex.bot.handler.cap.UpdateShippingQueryHandler;
import com.serov.alex.bot.handler.determinant.UpdateType;
import com.serov.alex.bot.state.BotState;
import com.serov.alex.bot.storage.Storage;
import com.serov.alex.bot.storage.TempStorage;
import com.serov.alex.bot.regex.find.FindStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Setter
@Getter
@Component
public class MusicFileFinderBot extends AbilityBot {

  private final String channelId;
  FindStorage findStorage;
  private Storage storage;
  private TempStorage tempStorage;
  private BotState state;


  public MusicFileFinderBot(Environment environment) {
    super(environment.getProperty("BOT_TOKEN"), "MusicFileFinderBot");
    channelId = environment.getProperty("CHANNEL_ID");
    storage = new Storage(db);
    findStorage = new FindStorage(storage.getStringMusicFileMap());
    state = BotState.SEARCH;
  }

  @Override
  public void onUpdateReceived(Update update) {
    UpdateHandler handler = switch (UpdateType.getType(update)) {
      case MESSAGE -> new UpdateMessageHandler();
      case INLINE_QUERY -> new UpdateInlineQueryHandler();
      case CHOSEN_INLINE_QUERY -> new UpdateChosenInlineQueryHandler();
      case CALLBACK_QUERY -> new UpdateCallbackQueryHandler();
      case EDITED_MESSAGE -> new UpdateEditedMessageHandler();
      case CHANNEL_POST -> new UpdateChannelPostHandler();
      case EDITED_CHANNEL_POST -> new UpdateEditedChannelPostHandler();
      case SHIPPING_QUERY -> new UpdateShippingQueryHandler();
      case PRE_CHECKOUT_QUERY -> new UpdatePreCheckoutQueryHandler();
      case POLL -> new UpdatePollHandler();
      case POLL_ANSWER -> new UpdatePollAnswerHandler();
      case MY_CHAT_MEMBER -> new UpdateMyChatMemberHandler();
      case CHAT_MEMBER -> new UpdateChatMemberHandler();
      case CHAT_JOIN_REQUEST -> new UpdateChatJoinRequestHandler();
    };
    log.atInfo().log(state.name());
    //log.atInfo().log("Type update: {}", handler + " : " + update.toString());
    Executor executor = new Executor(sender, silent, 250);
    handler.handle(this, executor, update);
  }

  @Override
  public long creatorId() {
    return 1182966178;
  }

  public Long getTrustedChatId() {
    return -1001861671595L;
  }
}