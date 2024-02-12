package main.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import main.command.AbstractCommand;
import main.command.impl.CategoryStorageCommand;
import main.command.impl.StorageCommand;
import main.model.Storage;
import main.model.StorageCategory;
import main.utils.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class StateService {

  private final HashMap<User, List<StateDto>> state;

  @Autowired
  public StateService() {
    this.firstMessage = new HashMap<>();
    this.state = new HashMap<>();
  }


  /**
   * Уровни команд пользователя
   */
  public void addLevel(AbstractCommand command, Update update) {
    User user = BotUtils.getUser(update);
    StateDto stateDto = makeState(command, update);
    if (!state.containsKey(user)) {
      state.put(user, new ArrayList<>());
      state.get(user).add(stateDto);
    } else {
      if (!getCurrentLevel(user).getName().equals(command.getClass().getName())) {
        state.get(user).add(stateDto);
      }
    }
  }

  public StateDto getPreviousLevel(User user, boolean needRemove) {
    List<StateDto> listState = state.get(user);
    if (needRemove) {
      listState.remove(listState.size() - 1);
      return listState.get(listState.size() - 1);
    }
    return listState.get(listState.size() - 2);
  }

  public StateDto getPreviousLevel(User user) {
    return getPreviousLevel(user, false);
  }

  public StateDto getCurrentLevel(User user) {
    List<StateDto> listState = state.get(user);
    return listState.get(listState.size() - 1);
  }

  private StateDto makeState(AbstractCommand command, Update update) {
    StateDto state = new StateDto();
    state.setUpdate(update);
    state.setCommand(command);
    state.setName(command.getClass().getName());
    return state;
  }


  /**
   * Сохранять сюда id сообщений первого входа(когда пользователя еще нет в БД)
   */
  private final HashMap<User, List<Long>> firstMessage;
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

  /**
   * По последнему сообщению определят какой объект создавать
   * @return
   */
  public Class<?> getClassFormByState(AbstractCommand currentCommand) {
    if (currentCommand instanceof CategoryStorageCommand) {
      return StorageCategory.class;
    }
    if (currentCommand instanceof StorageCommand) {
      return Storage.class;
    } else {
      return null;
    }
  }

}
