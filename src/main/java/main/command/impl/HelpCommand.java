package main.command.impl;

import main.api.telegram.impl.TelegramRequestImpl;
import main.command.AbstractCommand;
import main.exception.UserException;
import main.service.SendingMessageDecorator;
import main.service.UserService;
import main.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class HelpCommand extends AbstractCommand {

  @Autowired
  public HelpCommand(UserService userService,
      SendingMessageDecorator sendingMessage,
      StateService stateService) {
    super(userService, sendingMessage, stateService);
    commandLevels.add("/help");
  }
  @Override
  public void processing(Update update) throws UserException {

  }

  @Override
  public void backProcessing(Update update, String lastMessage) throws UserException {

  }

  @Override
  public void postProcessing(Update update, String lastMessage) throws UserException {

  }
}
