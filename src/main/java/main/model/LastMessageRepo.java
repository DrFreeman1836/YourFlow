package main.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastMessageRepo extends JpaRepository<LastMessage, Long> {

  List<LastMessage> findAllLastMessageByUsers(Users users);

  LastMessage findAllLastMessageByUsersAndIdLastMessage(Users users, Long idLastMessage);

}
