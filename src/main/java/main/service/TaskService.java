package main.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import main.exception.UserException;
import main.model.Task;
import main.model.TaskRepo;
import main.model.Users;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepo taskRepo;

  public List<Task> findTasksByUser(Users users) {
    return taskRepo.findAllByUsers(users);
  }

  public void taskIsDone(Long id) throws UserException {
    Task task =  taskRepo.findById(id).orElseThrow(() -> new UserException("Задача не найдена, выполните команду /start"));
    task.setIsActive(false);
    taskRepo.save(task);
  }

}
