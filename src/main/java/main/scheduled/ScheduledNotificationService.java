package main.scheduled;

import java.util.List;
import lombok.RequiredArgsConstructor;
import main.model.Users;
import main.service.SendingMessageDecorator;
import main.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledNotificationService {

  private final UserService userService;
  private final SendingMessageDecorator sendingMessage;

  @Scheduled(cron = "0 58 23 * * ?")
  public void loopDeleteMessage() {
    List<Users> listSeller = userService.findAllUsers();
    listSeller.forEach(sendingMessage::clearAllHistoryMessage);
  }

}
