package main;

import main.command.CommandStorage;
import main.command.impl.AddingCommand;
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
    System.out.println(key);

    User user = BotUtils.getUser(update);
    Long idMes = update.getMessage() != null ? update.getMessage().getMessageId().longValue() : null;
    try {
      if (idMes != null) {
        userService.saveLastMessage(user.getId(), idMes, false);
      }
    } catch (UserException ex) {
      stateService.addFirstMessage(user, idMes);
    }

    try {
      // обработка ввода пользователя

      if ("Назад".equals(key)) {
        try {
          stateService.getCurrentLevel(user).getCommand().backProcessing(update);
        } catch (IndexOutOfBoundsException ex) {
          CommandStorage.getMapCommand().get("/start").processing(update);
        }
      }
      if (AddingCommand.NAME.equals(key) || "Добавить задачу".equals(key)) {
        CommandStorage.getMapCommand().get(AddingCommand.NAME).processing(update, stateService.getClassFormByState(stateService.getCurrentLevel(user).getCommand()));
        return;
      }

      if (CommandStorage.getMapCommand().containsKey(key)) {
        CommandStorage.getMapCommand().get(key).processing(update);
      } else {
        stateService.getCurrentLevel(user).getCommand().postProcessing(update);
      }
    } catch (UserException ex) {
      ex.printStackTrace();
      sendMessage(ex.getMessage(), update);
    } catch (Exception ex) {
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
      userService.saveLastMessage(BotUtils.getUser(update).getId(), messId.longValue(), false);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

}
//todo: обработка ошибки по удалению старого сообщения и решение проблемы
//todo: обработка ошибки если список состояния пустой