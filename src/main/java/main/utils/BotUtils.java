package main.utils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class BotUtils {

  public static User getUser(Update update) {
    return update.getMessage() != null ? update.getMessage().getFrom()
        : update.getCallbackQuery().getFrom();
  }

  public static Long getChatId(Update update) {
    return update.getMessage() != null ? update.getMessage().getChatId()
        : update.getCallbackQuery().getMessage().getChatId();
  }

}
