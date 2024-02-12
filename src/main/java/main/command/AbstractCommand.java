package main.command;

import javax.annotation.PostConstruct;
import main.exception.UserException;
import main.service.SendingMessageDecorator;
import main.service.UserService;
import main.state.StateService;
import main.utils.BotUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractCommand {

  protected final SendingMessageDecorator sendingMessage;

  protected final UserService userService;

  protected final StateService stateService;

   protected final String name;

  public AbstractCommand(UserService userService, SendingMessageDecorator sendingMessage, StateService stateService, String name) {
    this.userService = userService;
    this.sendingMessage = sendingMessage;
    this.stateService = stateService;
    this.name = name;
  }

  @PostConstruct
  public void addingCommand() {
    CommandStorage.addCommand(this.name, this);
  }

  public void processing(Update update) throws UserException {
    saveState(update);
  }

  public void processing(Update update, Class<?> clazz) throws UserException {
    saveState(update);
  }

  public void postProcessing(Update update) throws UserException {
    saveState(update);
  }

  public void backProcessing(Update update) throws UserException {
    AbstractCommand previousLevel = stateService.getPreviousLevel(BotUtils.getUser(update), true).getCommand();
    previousLevel.processing(update);
  }

  public void saveState(Update update) {
    stateService.addLevel(this, update);
  }

}
