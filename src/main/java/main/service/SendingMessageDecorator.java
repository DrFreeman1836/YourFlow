package main.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import main.api.telegram.enums.ParseMode;
import main.api.telegram.impl.TelegramRequestImpl;
import main.model.Users;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
@RequiredArgsConstructor
public class SendingMessageDecorator {

  private final TelegramRequestImpl telegramRequest;

  private final UserService userService;

  public void sendSimpleMessage(User user, Long chatId, String text, ParseMode parseMode, ReplyKeyboard markup, Boolean needClear) {
    Users users = userService.findUserByIdTelegram(user.getId());
    if (needClear)
      clearHistoryMessage(users);
    Long idMes = Objects.requireNonNull(telegramRequest.sendMessage(chatId, text, parseMode, markup).getBody())
        .getResult().getMessageId();
    userService.saveLastMessage(users, idMes);
  }

  public void sendSimpleMessage(User user, Long chatId, String text, ReplyKeyboard markup, Boolean needClear) {
    sendSimpleMessage(user, chatId, text, ParseMode.NON, markup, needClear);
  }

  public void sendSimpleMessage(User user, Long chatId, String text, Boolean needClear) {
    sendSimpleMessage(user, chatId, text, ParseMode.NON, null, needClear);
  }

  public Long sendInfoMessage(Long chatId, String text, ParseMode parseMode, ReplyKeyboard markup) {
    return Objects.requireNonNull(telegramRequest.sendMessage(chatId, text, parseMode, markup).getBody())
        .getResult().getMessageId();
  }

  private void clearHistoryMessage(Users users) {
    users.getLastMessageList().forEach(m -> {
      //todo: обработать ошибку bad request (id несуществующего сообщения)
      telegramRequest.deleteMessage(users.getChatId(), m.getIdLastMessage());
    });
    userService.clearLastMessage(users);
  }

  private void clearHistoryMessage(Users users, Long idMes) {
    users.getLastMessageList().forEach(m -> {
      //todo: обработать ошибку bad request (id несуществующего сообщения)
      if (idMes.equals(m.getIdLastMessage())) {
        telegramRequest.deleteMessage(users.getChatId(), m.getIdLastMessage());
      }
    });
    userService.clearLastMessage(users, idMes);
  }

  public void deleteMessageById(Long idMes, Long idTelegram) {
    Users users = userService.findUserByIdTelegram(idTelegram);
    clearHistoryMessage(users, idMes);
  }

}
