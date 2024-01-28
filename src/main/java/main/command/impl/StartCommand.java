package main.command.impl;

import main.api.telegram.impl.TelegramRequestImpl;
import main.command.AbstractCommand;
import main.exception.UserException;
import main.service.UserService;
import main.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class StartCommand extends AbstractCommand {

  @Autowired
  private StartCommand(UserService userService, TelegramRequestImpl telegramRequest, StateService stateService) {
    super("/start", userService, telegramRequest, stateService);
  }

  @Override
  public void processing(Update update) {
    User user = update.getMessage().getFrom();
    String name = user.getFirstName() == null ? user.getUserName() : user.getFirstName();
    try {
      //userService.findUserByIdTelegram(user.getId());
      telegramRequest.sendMessage(update.getMessage().getChatId(), "start");
      //telegramRequest.sendMessage(update.getMessage().getChatId(), name + ", привет!", StorageMenu.getMainMenu());
    } catch (UserException ex) {
      //userService.saveUser(update);
      //telegramRequest.sendMessage(update.getMessage().getChatId(), name + ", привет!", StorageMenu.getMainMenu());
    }

  }

  @Override
  public void postProcessing(Update update, String lastMessage) {

  }

}
