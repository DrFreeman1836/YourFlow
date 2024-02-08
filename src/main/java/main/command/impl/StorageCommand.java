package main.command.impl;

import java.util.List;
import main.api.telegram.enums.ParseMode;
import main.command.AbstractCommand;
import main.exception.UserException;
import main.menu.InlineMenu;
import main.menu.StorageMenu;
import main.model.Storage;
import main.model.StorageCategory;
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
public class StorageCommand extends AbstractCommand {

  public static final String NAME = "Контент";

  private final StorageService storageService;

  @Autowired
  public StorageCommand(UserService userService,
      SendingMessageDecorator sendingMessage, StateService stateService,
      StorageService storageService) {
    super(userService, sendingMessage, stateService);
    this.storageService = storageService;
    commandLevels.add(CategoryStorageCommand.NAME);
    commandLevels.add(NAME);
  }

  @Override
  public void processing(Update update) throws UserException {
    User user = update.getMessage() != null ? update.getMessage().getFrom() : update.getCallbackQuery().getFrom();
    saveState(currentCommand(), user);
    StorageCategory category = storageService.findCategoryById(getNumberData(update.getCallbackQuery().getData()));
    List<Storage> storageCategories = storageService.findAllStorageByUsersAndCategory(category);
    sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), "Ваш контент:",
        StorageMenu.storageCategoryMenu(), true);
    if (storageCategories.isEmpty()) {
      sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), "Тут пока ничего не сохранено",
          StorageMenu.storageCategoryMenu(), true);
      return;
    }
    storageCategories.forEach(s -> {
      sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), s.getPreview(), ParseMode.NON, InlineMenu.toCategoryStorage(s.getId()), false);
    });
  }

  @Override
  public void postProcessing(Update update, String lastMessage) throws UserException {

  }

}
