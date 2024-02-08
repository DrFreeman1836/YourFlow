package main.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import main.command.impl.CategoryStorageCommand;
import main.command.impl.StorageCommand;
import main.model.Storage;
import main.model.StorageCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class StateService {

  /**
   * Используется для сохранения состояния в командах
   */
  private final HashMap<User, String> lastMessageMap;

  /**
   * Сохранять сюда id сообщений первого входа(когда пользователя еще нет в БД)
   */
  private final HashMap<User, List<Long>> firstMessage;

  @Autowired
  public StateService() {
    this.lastMessageMap = new HashMap<>();
    this.firstMessage = new HashMap<>();
  }

  public void setLastMessageMap(String lastMessage, User user) {
    if (lastMessageMap.containsKey(user)) {
      lastMessageMap.remove(user);
      lastMessageMap.put(user, lastMessage);
    } else {
      lastMessageMap.put(user, lastMessage);
    }
  }

  /**
   * По последнему сообщению определят какой объект создавать
   * @param lastMessage
   * @return
   */
  public Class<?> getClassFormByState(String lastMessage) {
    return switch (lastMessage) {
      case CategoryStorageCommand.NAME -> StorageCategory.class;
      case StorageCommand.NAME -> Storage.class;
      default -> null;
    };
  }

  public String getLastMessage(User user) {
    return lastMessageMap.get(user);
  }

  public void clearLastMessage(User user) {
    lastMessageMap.remove(user);
  }

  public void addFirstMessage(User user, Long idMessage) {
    if (firstMessage.containsKey(user)) {
      firstMessage.get(user).add(idMessage);
    } else {
      firstMessage.put(user, Collections.singletonList(idMessage));
    }
  }

  public List<Long> getFirstMessage(User user) {
    return firstMessage.get(user);
  }

  public void clearFirstMessage(User user) {
    firstMessage.remove(user);
  }

}
