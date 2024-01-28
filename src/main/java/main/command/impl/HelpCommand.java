package main.command.impl;

import main.api.telegram.impl.TelegramRequestImpl;
import main.command.AbstractCommand;
import main.exception.UserException;
import main.service.UserService;
import main.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class HelpCommand extends AbstractCommand {

  @Autowired
  public HelpCommand(UserService userService,
      TelegramRequestImpl telegramRequest,
      StateService stateService) {
    super("/help", userService, telegramRequest, stateService);
  }
  @Override
  public void processing(Update update) throws UserException {
    telegramRequest.sendMessage(update.getMessage().getChatId(), "help");
  }

  @Override
  public void postProcessing(Update update, String lastMessage) throws UserException {

  }
}
