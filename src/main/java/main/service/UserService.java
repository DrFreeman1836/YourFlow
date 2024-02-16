package main.service;

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

  public Users saveUser(Update update, Long idInfoMessage) {
    Users newUser = new Users();
    newUser.setIdInfoMessage(idInfoMessage);
    newUser.setUserName(update.getMessage().getFrom().getUserName());
    newUser.setFirstName(update.getMessage().getFrom().getFirstName());
    newUser.setLastName(update.getMessage().getFrom().getLastName());
    newUser.setIdTelegram(update.getMessage().getFrom().getId());
    newUser.setChatId(update.getMessage().getChatId());
    return userRepo.save(newUser);
  }

  public Users findUserByIdTelegram(Long idTelegram) {
    return userRepo.findByIdTelegram(idTelegram).orElseThrow(() -> new UserException("Пользователь не найден, выполните команду \"/start\""));
  }

  public void clearLastMessage(Users users) {
    lastMessageRepo.findAllLastMessageByUsers(users).forEach(lastMessageRepo::delete);
  }

  public void clearLastMessage(Users users, Long idMes) {
    lastMessageRepo.delete(lastMessageRepo.findAllLastMessageByUsersAndIdLastMessage(users, idMes));
  }

  public void saveLastMessage(Users users, Long idLastMessage) {
    LastMessage lastMessage = new LastMessage();
    lastMessage.setIdLastMessage(idLastMessage);
    lastMessage.setUsers(users);
    lastMessageRepo.save(lastMessage);
  }

  public void saveLastMessage(Long idTelegram, Long idLastMessage) {
    saveLastMessage(findUserByIdTelegram(idTelegram), idLastMessage);
  }

}
