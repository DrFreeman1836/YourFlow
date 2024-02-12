package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "STORAGE")
public class Storage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @MappingForm(position = 1, caption = "Введите заголовок...")
  @Column(name = "PREVIEW")
  private String preview;

  @MappingForm(position = 2, caption = "Пришлите контент...")
  @Column(name = "CONTENT", columnDefinition = "text")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "storage_category_id")
  private StorageCategory storageCategory;

  @Override
  public String toString() {
    return  preview + ":" + "\n\n" + content;
  }

}
