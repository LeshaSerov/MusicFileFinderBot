package com.serov.alex.bot.executor;

import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class Executor {

  private final MessageSender sender;
  private final SilentSender silent;
  private final int timeDelayInMilliseconds;

  public Executor(MessageSender sender, SilentSender silent, int timeDelayInMilliseconds1) {
    this.sender = sender;
    this.silent = silent;
    this.timeDelayInMilliseconds = timeDelayInMilliseconds1;
  }

  public <T extends Serializable, Method extends BotApiMethod<T>> boolean execute(Method method,
      ExecuteMode mode) {
    try {
      Thread.sleep(timeDelayInMilliseconds);
      if (mode.isQuiet) {
        silent.execute(method);
      } else {
        sender.execute(method);
      }
    } catch (Exception e) {
      log.atInfo().log("Execute Error: {}", e.getMessage());
      return false;
    }
    return true;
  }

  public boolean sendAudio(SendAudio sendAudio) {
    try {
      Thread.sleep(timeDelayInMilliseconds);
      sender.sendAudio(sendAudio);
    } catch (TelegramApiException e) {
      log.atInfo().log("Send Audio Error: ", e.getMessage());
      return false;
    }
    catch (InterruptedException e) {
      log.atInfo().log("Thread Sleep Error: ", e.getMessage());
      return false;
    } catch (Exception e) {
      log.atInfo().log("Send Audio Error: ", e.getMessage());
      return false;
    }
    return true;
  }
}
