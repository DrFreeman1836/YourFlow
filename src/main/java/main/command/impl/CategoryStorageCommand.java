package main.command.impl;

import java.util.List;
import main.api.telegram.enums.ParseMode;
import main.command.AbstractCommand;
import main.command.CommandStorage;
import main.exception.UserException;
import main.menu.InlineMenu;
import main.menu.StorageMenu;
import main.model.StorageCategory;
import main.model.Users;
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
public class CategoryStorageCommand extends AbstractCommand {

  public static final String NAME = "Хранилище";

  private final StorageService storageService;

  @Autowired
  public CategoryStorageCommand(UserService userService,
      SendingMessageDecorator sendingMessage,
      StateService stateService, StorageService storageService) {
    super(userService, sendingMessage, stateService, NAME);
    this.storageService = storageService;
  }

  @Override
  public void processing(Update update) throws UserException {
    super.processing(update);
    User user = update.getMessage().getFrom();
    Users users = userService.findUserByIdTelegram(user.getId());
    List<StorageCategory> storageCategories = storageService.findAllCategoriesByUsers(users);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), "Ваши категории:",
        StorageMenu.storageCategoryMenu(), true);
    if (storageCategories.isEmpty()) {
      sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), "У Вас пока нет сохраненных категорий",
          StorageMenu.storageCategoryMenu(), true);
      return;
    }
    storageCategories.forEach(s -> {
      sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), s.getName(), ParseMode.NON, InlineMenu.toCategoryStorage(s.getId()), false);
    });
  }

  @Override
  public void postProcessing(Update update) throws UserException {
    User user = BotUtils.getUser(update);
    String data = update.getCallbackQuery() == null ? null : update.getCallbackQuery().getData();
    if (data == null) return;
    Long idMes = update.getCallbackQuery().getMessage().getMessageId().longValue();
    if (data.startsWith("d")) {
      storageService.deleteCategoryById(BotUtils.getNumberData(data));
      sendingMessage.deleteMessageById(idMes, user.getId());
    } else if (data.startsWith("s")) {
      CommandStorage.getMapCommand().get("Контент").processing(update);
    }
  }

}
