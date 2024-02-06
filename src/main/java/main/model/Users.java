package main.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "USERS")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Уникальное имя
   */
  @Column(name = "USER_NAME")
  private String userName;

  /**
   * Уникальный id чата с ботом
   */
  @Column(name = "CHAT_ID", unique = true)
  private Long chatId;

  /**
   * Уникальный id в БД телеграмма
   */
  @Column(name = "ID_TELEGRAM", unique = true)
  private Long idTelegram;

  /**
   * Имя
   */
  @Column(name = "FIRST_NAME")
  private String firstName;

  /**
   * Фамилия
   */
  @Column(name = "LAST_NAME")
  private String lastName;

  /**
   * id сообщения информационной панели
   */
  @Column(name = "ID_INFO_MESSAGE")
  private Long idInfoMessage;

  @OneToMany(mappedBy = "users", fetch = FetchType.EAGER)
  private List<LastMessage> lastMessageList;

  @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
  private List<StorageCategory> storageList;

  @Override
  public String toString() {
    return "Users{" +
        "id=" + id +
        ", userName='" + userName + '\'' +
        ", chatId=" + chatId +
        ", idTelegram=" + idTelegram +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", idInfoMessage=" + idInfoMessage +
        '}';
  }
}
