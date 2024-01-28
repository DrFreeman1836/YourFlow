package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "USERS")
public class MyUser {

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

}
