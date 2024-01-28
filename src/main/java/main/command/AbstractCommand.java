package main.command;

import main.api.telegram.impl.TelegramRequestImpl;
import main.exception.UserException;
import main.service.UserService;
import main.state.StateService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class AbstractCommand {

  protected final String name;

  protected final TelegramRequestImpl telegramRequest;

  protected final UserService userService;

  protected final StateService stateService;

  public AbstractCommand(String name, UserService userService, TelegramRequestImpl telegramRequest, StateService stateService) {
    this.name = name;
    this.userService = userService;
    this.telegramRequest = telegramRequest;
    this.stateService = stateService;
    init();
  }

  private void init() {
    CommandStorage.addCommand(name, this);
  }

  public abstract void processing(Update update) throws UserException;

  public abstract void postProcessing(Update update, String lastMessage) throws UserException;


  protected void saveState(User user) {
    // сохранить состояние
  }

  protected void clearState(User user) {
    // очистиьт состояние
  }

}
