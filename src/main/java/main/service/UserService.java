package main.service;

import lombok.RequiredArgsConstructor;
import main.exception.UserException;
import main.model.MyUser;
import main.model.UserRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;

  public void saveUser(Update update) {
    MyUser newUser = new MyUser();
    newUser.setUserName(update.getMessage().getFrom().getUserName());
    newUser.setFirstName(update.getMessage().getFrom().getFirstName());
    newUser.setLastName(update.getMessage().getFrom().getLastName());
    newUser.setIdTelegram(update.getMessage().getFrom().getId());
    newUser.setChatId(update.getMessage().getChatId());
    userRepo.save(newUser);
  }

  public MyUser findUserByIdTelegram(Long idTelegram) {
    return userRepo.findByIdTelegram(idTelegram).orElseThrow(() -> new UserException("Пользователь не найден, выполните команду \"/start\""));
  }

}
