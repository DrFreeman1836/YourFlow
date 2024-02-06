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
    super(userService, sendingMessage, stateService);
    this.storageService = storageService;
    commandLevels.add("Главное меню");
    commandLevels.add(NAME);
  }

  @Override
  public void processing(Update update) throws UserException {
    User user = update.getMessage().getFrom();
    saveState(currentCommand(), user);
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
  public void postProcessing(Update update, String lastMessage) throws UserException {
    User user = update.getCallbackQuery() != null ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
    String data = update.getCallbackQuery() == null ? null : update.getCallbackQuery().getData();
    if (data == null) return;
    Long idMes = update.getCallbackQuery().getMessage().getMessageId().longValue();
    if (data.startsWith("d")) {
      storageService.deleteCategoryById(getNumberData(data));
      sendingMessage.deleteMessageById(idMes, user.getId());
    } else if (data.startsWith("s")) {
      CommandStorage.getMapCommand().get("Контент").processing(update);
    }
  }

  @Override
  public void backProcessing(Update update, String lastMessage) throws UserException {
    clearState(update.getMessage().getFrom());
    sendingMessage.sendSimpleMessage(update.getMessage().getFrom(), update.getMessage().getChatId(), previousCommand(), StorageMenu.getMainMenu(), true);
  }

}
