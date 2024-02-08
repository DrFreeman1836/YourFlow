package main.command;

import java.util.LinkedList;
import javax.annotation.PostConstruct;
import main.exception.UserException;
import main.service.SendingMessageDecorator;
import main.service.UserService;
import main.state.StateService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class AbstractCommand {

  protected final SendingMessageDecorator sendingMessage;

  protected final UserService userService;

  protected final StateService stateService;

  protected final LinkedList<String> commandLevels;

  public AbstractCommand(UserService userService, SendingMessageDecorator sendingMessage, StateService stateService) {
    this.userService = userService;
    this.sendingMessage = sendingMessage;
    this.stateService = stateService;
    this.commandLevels = new LinkedList<>();
  }

  @PostConstruct
  public void addingCommand() {
    CommandStorage.addCommand(commandLevels.getLast(), this);
  }

  public abstract void processing(Update update) throws UserException;

  public void processing(Update update, Class<?> clazz) throws UserException {

  }

  public abstract void postProcessing(Update update, String lastMessage) throws UserException;

  public void backProcessing(Update update, String lastMessage) throws UserException {
    clearState(update.getMessage().getFrom());
    String previousLevel = previousCommand();
    CommandStorage.getMapCommand().get(previousLevel).processing(update);
  }

  protected void saveState(String message, User user) {
    stateService.clearLastMessage(user);
    stateService.setLastMessageMap(message, user);
  }

  protected void clearState(User user) {
    stateService.clearLastMessage(user);
  }

  protected String currentCommand() {
    return commandLevels.getLast();
  }

  protected String previousCommand() {
    //todo: обработать ошибку если в списке меньше двух записей
    return commandLevels.get(commandLevels.size() - 2);
  }

  protected Long getNumberData(String data) {
    return Long.valueOf(data.replaceAll("[^0-9]", ""));
  }

}
