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
    super(userService, sendingMessage, stateService, NAME);
    this.storageService = storageService;
  }

  @Override
  public void processing(Update update) throws UserException {
    super.processing(update);
    User user = BotUtils.getUser(update);
    Long data = update.hasCallbackQuery() ? BotUtils.getNumberData(update.getCallbackQuery().getData())
        : BotUtils.getNumberData(stateService.getCurrentLevel(user).getUpdate().getCallbackQuery().getData());
    StorageCategory category = storageService.findCategoryById(data);
    List<Storage> storageCategories = storageService.findAllStorageByUsersAndCategory(category);
    sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), "Ваш контент:",
        StorageMenu.storageCategoryMenu(), true);
    if (storageCategories.isEmpty()) {
      sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), "Тут пока ничего не сохранено",
          StorageMenu.storageCategoryMenu(), true);
      return;
    }
    storageCategories.forEach(s -> {
      sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), s.toString(), ParseMode.NON, InlineMenu.toStorage(s.getId()), false);
    });
  }

  @Override
  public void postProcessing(Update update) throws UserException {
    User user = BotUtils.getUser(update);
    String data = update.getCallbackQuery() == null ? null : update.getCallbackQuery().getData();
    if (data == null) return;
    Long idMes = update.getCallbackQuery().getMessage().getMessageId().longValue();
    if (data.startsWith("d")) {
      storageService.deleteStorageById(BotUtils.getNumberData(data));
      sendingMessage.deleteMessageById(idMes, user.getId());
    }
  }

}
