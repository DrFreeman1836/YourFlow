package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import main.form.MappingForm;

@Data
@Entity
@Table(name = "TASK")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @MappingForm(position = 1, caption = "Введите задачу...")
  @Column(name = "CAPTION")
  private String caption;

  @MappingForm(position = 2, caption = "Выберите приоритет...")
  @Enumerated(EnumType.STRING)
  private Priority priority;

  @Column(name = "CREATING_DATE")
  private Date creatingDate;

  @Column(name = "IS_ACTIVE")
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id")
  private Users users;

  public enum Priority {
    ВЫСОКИЙ, НОРМАЛЬНЫЙ, НИЗКИЙ
  }

}
