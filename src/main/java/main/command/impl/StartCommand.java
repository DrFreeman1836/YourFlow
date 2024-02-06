package main.command.impl;

import main.api.telegram.enums.ParseMode;
import main.api.telegram.impl.TelegramRequestImpl;
import main.command.AbstractCommand;
import main.exception.UserException;
import main.menu.StorageMenu;
import main.service.SendingMessageDecorator;
import main.service.UserService;
import main.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class StartCommand extends AbstractCommand {

  @Autowired
  private StartCommand(UserService userService, SendingMessageDecorator sendingMessage, StateService stateService) {
    super(userService, sendingMessage, stateService);
    commandLevels.add("/start");
  }

  @Override
  public void processing(Update update) {
    User user = update.getMessage().getFrom();
    String name = user.getFirstName() == null ? user.getUserName() : user.getFirstName();
    try {
      userService.findUserByIdTelegram(user.getId());
      sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), name + ", привет!", StorageMenu.getMainMenu(), true);
    } catch (UserException ex) {
      Long idInfoMessage = sendingMessage.sendInfoMessage(update.getMessage().getChatId(), "Тут будет инфа!!!", ParseMode.NON ,StorageMenu.getMainMenu());
      userService.saveUser(update, idInfoMessage);
      saveFirstMessage(user);
      sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), name + ", привет!", StorageMenu.getMainMenu(), true);
    } finally {
      clearState(user);
    }

  }

  @Override
  public void postProcessing(Update update, String lastMessage) {

  }

  @Override
  public void backProcessing(Update update, String lastMessage) throws UserException {

  }

  private void saveFirstMessage(User user) {
    stateService.getFirstMessage(user).forEach(m -> {
      userService.saveLastMessage(user.getId(), m);
    });
    stateService.clearFirstMessage(user);
  }

}
