package main.command.impl;

import java.util.Comparator;
import java.util.List;
import main.api.telegram.enums.ParseMode;
import main.api.telegram.impl.TelegramRequestImpl;
import main.command.AbstractCommand;
import main.exception.UserException;
import main.menu.InlineMenu;
import main.menu.StorageMenu;
import main.model.Task;
import main.model.Users;
import main.service.SendingMessageDecorator;
import main.service.TaskService;
import main.service.UserService;
import main.state.StateService;
import main.utils.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class StartCommand extends AbstractCommand {

  public static final String NAME = "/start";

  private final TaskService taskService;

  private final TelegramRequestImpl telegramRequest;

  @Autowired
  private StartCommand(UserService userService, SendingMessageDecorator sendingMessage, StateService stateService,
      TaskService taskService, TelegramRequestImpl telegramRequest) {
    super(userService, sendingMessage, stateService, NAME);
    this.taskService = taskService;
    this.telegramRequest = telegramRequest;
  }

  @Override
  public void processing(Update update) {
    stateService.clearState(BotUtils.getUser(update));
    super.processing(update);
    User user = BotUtils.getUser(update);
    Users users = adminActions(update);
    sendingMessage.clearAllHistoryMessage(users);
    sendingMessage.sendInfoMessage(users, update.getMessage().getChatId(), "Тут будут категории задач", ParseMode.NON ,StorageMenu.getMainMenu());
    List<Task> taskList = taskService.findTasksByUser(users);
    sendingMessage.sendSimpleMessage(user, update.getMessage().getChatId(), "Задачи:", StorageMenu.getMainMenu(), true);
    taskList.stream().sorted(Comparator.comparing(Task::getPriority).reversed()).forEach(t -> {
      if (t.getIsActive())
        sendingMessage.sendSimpleMessage(user, BotUtils.getChatId(update), t.getCaption(), ParseMode.HTML,
            InlineMenu.toTask(t.getId()), false);
    });
  }

  private Users adminActions(Update update) {
    User user = BotUtils.getUser(update);
    try {
      return userService.findUserByIdTelegram(user.getId());
    } catch (UserException ex) {
      Users users = userService.saveUser(update);
      saveFirstMessage(user);
      return users;
    }
  }

  @Override
  public void postProcessing(Update update) {
    String data = update.getCallbackQuery() == null ? null : update.getCallbackQuery().getData();
    if (data == null) return;
    Long idMes = update.getCallbackQuery().getMessage().getMessageId().longValue();
    if (data.startsWith("done")) {
      taskService.taskIsDone(BotUtils.getNumberData(data));
      telegramRequest.editMessage(BotUtils.getChatId(update), idMes, "<s>" + update.getCallbackQuery().getMessage().getText() + "</s>", ParseMode.HTML, InlineMenu.nonMenu());
    }
  }

  @Override
  public void backProcessing(Update update) throws UserException {

  }

  private void saveFirstMessage(User user) {
    stateService.getFirstMessage(user).forEach(m -> {
      userService.saveLastMessage(user.getId(), m, false);
    });
    stateService.clearFirstMessage(user);
  }

}
