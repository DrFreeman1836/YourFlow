package main.state;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class StateService {
  private final HashMap<User, String> lastMessageMap;

  @Autowired
  public StateService() {
    this.lastMessageMap = new HashMap<>();
  }

  public void setLastMessageMap(String lastMessage, User user) {
    lastMessageMap.put(user, lastMessage);
  }

  public String getLastMessage(User user) {
    return lastMessageMap.get(user);
  }

  public void clearLastMessage(User user) {
    lastMessageMap.remove(user);
  }

}
