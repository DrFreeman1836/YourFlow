package main.command.impl;

import main.command.AbstractCommand;
import main.command.CommandStorage;
import main.exception.UserException;
import main.form.FormProcessing;
import main.menu.StorageMenu;
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
public class AddingCategoryStorageCommand extends AbstractCommand {

  public static final String NAME = "Добавить категорию";

  private final StorageService storageService;

  private final FormProcessing formProcessing;

  @Autowired
  public AddingCategoryStorageCommand(UserService userService,
      SendingMessageDecorator sendingMessage, StateService stateService,
      StorageService storageService, FormProcessing formProcessing) {
    super(userService, sendingMessage, stateService);
    this.storageService = storageService;
    this.formProcessing = formProcessing;
    commandLevels.add(CategoryStorageCommand.NAME);
    commandLevels.add(NAME);
  }

  @Override
  public void processing(Update update) throws UserException {
    User user = update.getMessage().getFrom();
    saveState("Добавить категорию", user);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(),
        "Введите название категории хранилища...", StorageMenu.backMenu(), true);
  }

  public void processing(Update update, Object form) throws UserException {
    User user = update.getMessage().getFrom();
    saveState("Добавить категорию", user);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(),
        "Введите название категории хранилища...", StorageMenu.backMenu(), true);
  }

  @Override
  public void postProcessing(Update update, String lastMessage) throws UserException {
    User user = update.getMessage().getFrom();
    Users users = userService.findUserByIdTelegram(user.getId());
    storageService.saveStorageCategory(users, update.getMessage().getText());
//    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(),
//        "Сохранена категория хранилища \"" + update.getMessage().getText() + "\"",
//        StorageMenu.backMenu(), true);
    clearState(update.getMessage().getFrom());
    String previousLevel = previousCommand();
    CommandStorage.getMapCommand().get(previousLevel).processing(update);
  }

}
