package main.command.impl;

import main.command.AbstractCommand;
import main.exception.UserException;
import main.form.FormProcessing;
import main.menu.StorageMenu;
import main.service.SendingMessageDecorator;
import main.service.UserService;
import main.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class AddingCommand extends AbstractCommand {

  public static final String NAME = "Добавить";

  private final FormProcessing formProcessing;

  @Autowired
  public AddingCommand(UserService userService,
      SendingMessageDecorator sendingMessage, StateService stateService, FormProcessing formProcessing) {
    super(userService, sendingMessage, stateService, NAME);
    this.formProcessing = formProcessing;
  }

  @Override
  public void processing(Update update) throws UserException {

  }

  public void processing(Update update, Class<?> clazz) throws UserException {
    super.processing(update);
    User user = update.getMessage().getFrom();
    formProcessing.registerForm(user, clazz);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(),
        formProcessing.getRequestToUser(update), StorageMenu.backMenu(), true);
  }

  @Override
  public void postProcessing(Update update) throws UserException {
    User user = update.getMessage().getFrom();
    String requestMessage = formProcessing.getRequestToUser(update);
    if (requestMessage.equalsIgnoreCase("Выберите приоритет...")) {
      sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), requestMessage,
          StorageMenu.getPriority(), false);
    } else {
      sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), requestMessage,
          StorageMenu.backMenu(), false);
    }
  }

}
