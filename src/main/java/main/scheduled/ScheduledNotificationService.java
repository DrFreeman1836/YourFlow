package main.scheduled;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import main.api.telegram.enums.ParseMode;
import main.menu.InlineMenu;
import main.menu.StorageMenu;
import main.model.Task;
import main.model.Users;
import main.service.SendingMessageDecorator;
import main.service.TaskService;
import main.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class ScheduledNotificationService {

  private final UserService userService;
  private final SendingMessageDecorator sendingMessage;
  private final TaskService taskService;

  @Scheduled(cron = "0 58 23 * * ?")
  public void loopDeleteMessage() {
    List<Users> allUsers = userService.findAllUsers();
    allUsers.forEach(sendingMessage::clearAllHistoryMessage);
  }

  @Scheduled(cron = "0 01 7 * * ?")
  public void loopUpdateMessage() {
    List<Users> allUsers = userService.findAllUsers();
    allUsers.forEach(u -> {
      User user = new User();
      user.setId(u.getIdTelegram());
      sendingMessage.sendInfoMessage(u, u.getChatId(), "Тут будут категории задач", ParseMode.NON , StorageMenu.getMainMenu());
      List<Task> taskList = taskService.findTasksByUser(u);
      sendingMessage.sendSimpleMessage(user, u.getChatId(), "Задачи:", StorageMenu.getMainMenu(), true);
      taskList.stream().sorted(Comparator.comparing(Task::getPriority).reversed()).forEach(t -> {
        if (t.getIsActive())
          sendingMessage.sendSimpleMessage(user, u.getChatId(), t.getCaption(), ParseMode.HTML,
              InlineMenu.toTask(t.getId()), false);
      });
    });
  }

}
