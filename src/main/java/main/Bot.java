package main;

import main.command.CommandStorage;
import main.exception.UserException;
import main.service.UserService;
import main.state.StateService;
import main.utils.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

  private StateService stateService;

  private UserService userService;

  @Value("${bot.token}")
  private String token;

  @Value("${bot.name}")
  private String name;

  @Autowired
  public void setUserService(StateService stateService) {
    this.stateService = stateService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public String getBotUsername() {
    return name;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {

    String key = update.hasCallbackQuery()
        ? update.getCallbackQuery().getData()
        : update.getMessage().getText() == null ? null
            : update.getMessage().getText().replaceAll("[^ЁёА-я /start /help]", "");
    String mes = update.getMessage() != null ? update.getMessage().getText() : update.getCallbackQuery().getMessage().getText();

    System.out.println(key);
    User user = update.getMessage() == null ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();

    Long idMes = update.getMessage() != null ? update.getMessage().getMessageId().longValue() : null;
    try {
      if (idMes != null) {
        userService.saveLastMessage(user.getId(), idMes);
      }
    } catch (UserException ex) {
      if (idMes != null) {
        stateService.addFirstMessage(user, idMes);
      }
    }

    try {
      if ("Добавить".equals(key)) {
        CommandStorage.getMapCommand().get(key).processing(update, stateService.getClassFormByState(stateService.getLastMessage(user)));
      }
      if (CommandStorage.getMapCommand().containsKey(key)) {
        CommandStorage.getMapCommand().get(key).processing(update);
        return;
      }

      key = stateService.getLastMessage(user);
      if (CommandStorage.getMapCommand().containsKey(key) && !"Назад".equals(mes.replaceAll("[^ЁёА-я /start /help]", ""))) {
        CommandStorage.getMapCommand().get(key).postProcessing(update, key);
        return;
      } else {
        CommandStorage.getMapCommand().get(key).backProcessing(update, key);
        return;
      }
    } catch (UserException ex) {
      ex.printStackTrace();
      sendMessage(ex.getMessage(), update);
    } catch (NullPointerException ex) {
      ex.printStackTrace();
      sendMessage("Ошибка, выполните команду /start", update);
    }

  }

  private void sendMessage(String textMessage, Update update) {
    SendMessage message = new SendMessage();
    message.setChatId(String.valueOf(BotUtils.getChatId(update)));
    message.setText(textMessage);
    try {
      Integer messId = execute(message).getMessageId();
      userService.saveLastMessage(BotUtils.getUser(update).getId(), messId.longValue());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

}
//todo: рефактор этого класса
//todo: обработка ошибки по удалению старого сообщения и решение проблемы

//TODO: решение нахлабучки с командами (возмонжо, команды буду хранить состояние)(состояние в командах <user, linckedList> связаный список с кастомным классом в котором все состояние)