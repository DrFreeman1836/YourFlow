package main.form;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import main.exception.UserException;
import main.model.Storage;
import main.model.StorageCategory;
import main.model.StorageCategoryRepo;
import main.model.StorageRepo;
import main.model.Task;
import main.model.TaskRepo;
import main.model.Users;
import main.service.UserService;
import main.state.StateService;
import main.utils.BotUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class FormProcessing {

  private final StorageCategoryRepo storageCategoryRepo;

  private final StorageRepo storageRepo;

  private final TaskRepo taskRepo;

  private final UserService userService;

  private final StateService stateService;

  private final HashMap<User, Object> formMap = new HashMap<>();

  private final HashMap<User, String> fieldMap = new HashMap<>();

  public synchronized void registerForm(User user, Class<?> clazz) {//todo: все синхронизировать
    if (clazz.isAssignableFrom(Task.class)) {
      Task task = new Task();
      task.setUsers(userService.findUserByIdTelegram(user.getId()));
      task.setIsActive(true);
      task.setCreatingDate(new Date());
      addedFormMap(user, task);
    }
    if (clazz.isAssignableFrom(StorageCategory.class)) {
      StorageCategory storageCategory = new StorageCategory();
      storageCategory.setUsers(userService.findUserByIdTelegram(user.getId()));
      addedFormMap(user, storageCategory);
    }
    if (clazz.isAssignableFrom(Storage.class)) {
      Storage storage = new Storage();
      StorageCategory category = storageCategoryRepo
          .findById(BotUtils.getNumberData(stateService.getPreviousLevel(user).getUpdate().getCallbackQuery().getData()))
          .orElseThrow(() -> new UserException("Ошибка, выполните команду /start"));
      storage.setStorageCategory(category);
      addedFormMap(user, storage);
    }
  }

  private void addedFormMap(User user, Object object) {
    if (!formMap.containsKey(user)) {
      formMap.put(user, object);
    } else {
      formMap.remove(user);
      formMap.put(user, object);
    }
  }

  private void addedFieldMap(User user, String field) {
    if (!fieldMap.containsKey(user)) {
      fieldMap.put(user, field);
    } else {
      fieldMap.remove(user);
      fieldMap.put(user, field);
    }
  }

  public synchronized String getRequestToUser(Update update) throws UserException {
    Object creatingObject = formMap.get(BotUtils.getUser(update));
    List<Field> sortedField = sortingFields(creatingObject.getClass().getDeclaredFields());
    for (Field f : sortedField) {
      try {
        f.setAccessible(true);
        Object value = f.get(creatingObject);
        if (value == null && f.getAnnotation(MappingForm.class) != null) {
          if (fieldMap.containsKey(BotUtils.getUser(update))) {
            Class<?> fieldType = f.getType();
            if (fieldType.isEnum()) {
              Class<? extends Enum> enumClass = fieldType.asSubclass(Enum.class);
              Enum<?> enumValue = Enum.valueOf(enumClass, update.getMessage().getText());
              f.set(creatingObject, enumValue);
            } else {
              f.set(creatingObject, update.getMessage().getText());
            }
            fieldMap.remove(BotUtils.getUser(update));
          } else {
            addedFieldMap(BotUtils.getUser(update), f.getName());
            return f.getAnnotation(MappingForm.class).caption();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        clearState(BotUtils.getUser(update));
        throw  new UserException("Ошибка, выполните команду /start");
      }
    }

    if (formMap.get(BotUtils.getUser(update)) instanceof StorageCategory) {
      storageCategoryRepo.save((StorageCategory) formMap.get(BotUtils.getUser(update)));
    }
    if (formMap.get(BotUtils.getUser(update)) instanceof Storage) {
      storageRepo.save((Storage) formMap.get(BotUtils.getUser(update)));
    }
    if (formMap.get(BotUtils.getUser(update)) instanceof Task) {
      taskRepo.save((Task) formMap.get(BotUtils.getUser(update)));
    }
    return "Сохранено!";
  }

  public void clearState(User user) {
    formMap.remove(user);
    fieldMap.remove(user);
  }

  private synchronized List<Field> sortingFields(Field[] fields) {
    return Arrays.stream(fields)
        .sorted(Comparator.comparing(Field::getName))
        .sorted(Comparator.comparingInt(f1 -> {
          if (f1.getAnnotation(MappingForm.class) == null) {
            return 0;
          } else {
            return f1.getAnnotation(MappingForm.class).position();
          }
        }))
        .toList();
  }

}
