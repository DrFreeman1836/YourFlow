package main.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import main.exception.UserException;
import main.model.LastMessage;
import main.model.LastMessageRepo;
import main.model.Users;
import main.model.UserRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;

  private final LastMessageRepo lastMessageRepo;

  public Users saveUser(Update update) {
    Users newUser = new Users();
    newUser.setUserName(update.getMessage().getFrom().getUserName());
    newUser.setFirstName(update.getMessage().getFrom().getFirstName());
    newUser.setLastName(update.getMessage().getFrom().getLastName());
    newUser.setIdTelegram(update.getMessage().getFrom().getId());
    newUser.setChatId(update.getMessage().getChatId());
    return userRepo.save(newUser);
  }

  public List<Users> findAllUsers() {
    return userRepo.findAll();
  }

  public Users findUserByIdTelegram(Long idTelegram) {
    return userRepo.findByIdTelegram(idTelegram).orElseThrow(() -> new UserException("Пользователь не найден, выполните команду \"/start\""));
  }

  public void clearLastMessage(Users users, boolean isInfo) {
    if (isInfo) {
      lastMessageRepo.findAllLastMessageByUsers(users)
          .forEach(lastMessageRepo::delete);
    } else {
      lastMessageRepo.findAllLastMessageByUsersAndIsInfo(users, isInfo)
          .forEach(lastMessageRepo::delete);
    }
  }

  public void clearLastMessage(Users users, Long idMes) {
    lastMessageRepo.delete(lastMessageRepo.findAllLastMessageByUsersAndIdLastMessage(users, idMes));
  }

  public void saveLastMessage(Users users, Long idLastMessage, boolean isInfo) {
    LastMessage lastMessage = new LastMessage();
    lastMessage.setIdLastMessage(idLastMessage);
    lastMessage.setUsers(users);
    lastMessage.setIsInfo(isInfo);
    lastMessageRepo.save(lastMessage);
  }

  public void saveLastMessage(Long idTelegram, Long idLastMessage, boolean isInfo) {
    saveLastMessage(findUserByIdTelegram(idTelegram), idLastMessage, isInfo);
  }

}
