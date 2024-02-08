package main.command.impl;

import main.command.AbstractCommand;
import main.command.CommandStorage;
import main.exception.UserException;
import main.form.FormProcessing;
import main.menu.StorageMenu;
import main.service.SendingMessageDecorator;
import main.service.StorageService;
import main.service.UserService;
import main.state.StateService;
import main.utils.BotUtils;
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
    super(userService, sendingMessage, stateService);
    this.formProcessing = formProcessing;
    commandLevels.add(NAME);
  }

  @Override
  public void processing(Update update) throws UserException {

  }

  public void processing(Update update, Class<?> clazz) throws UserException {
    stateService.setLastLevel(stateService.getLastMessage(BotUtils.getUser(update)), BotUtils.getUser(update));
    User user = update.getMessage().getFrom();
    saveState(currentCommand(), user);
    formProcessing.registerForm(user, clazz);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(),
        formProcessing.getRequestToUser(update), StorageMenu.backMenu(), true);
  }

  @Override
  public void postProcessing(Update update, String lastMessage) throws UserException {
    User user = update.getMessage().getFrom();
    String requestMessage = formProcessing.getRequestToUser(update);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), requestMessage,
        StorageMenu.backMenu(), false);
  }

  public void backProcessing(Update update, String lastMessage) throws UserException {
    clearState(BotUtils.getUser(update));
    formProcessing.clearState(BotUtils.getUser(update));
    String previousLevel = stateService.getLastLevel(BotUtils.getUser(update));
    CommandStorage.getMapCommand().get(previousLevel).processing(update);
    stateService.clearLastLevel(BotUtils.getUser(update));
  }

}
