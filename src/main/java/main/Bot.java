package main;

import main.command.CommandStorage;
import main.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class Bot extends TelegramLongPollingBot {

  private StateService stateService;

  @Value("${bot.token}")
  private String token;

  @Value("${bot.name}")
  private String name;

  @Autowired
  public void setUserService(StateService stateService) {
    this.stateService = stateService;
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
        ? update.getCallbackQuery().getData()//.replaceAll("\\d|-", "")
        : update.getMessage().getText() == null ? null
            : update.getMessage().getText().replaceAll("[^ЁёА-я /start /help]", "");
    String mes = update.getMessage() != null ? update.getMessage().getText() : update.getCallbackQuery().getMessage().getText();

    System.out.println(key);
    User user = update.getMessage() == null ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
    if (CommandStorage.getMapCommand().containsKey(key)) {
      CommandStorage.getMapCommand().get(key).processing(update);
      return;
    }

    key = stateService.getMessage(user);
    if (CommandStorage.getMapCommand().containsKey(key)) {
      CommandStorage.getMapCommand().get(key).postProcessing(update, key);
      return;
    }

  }

}
