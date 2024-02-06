package main.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import main.form.MappingForm;

@Data
@Entity
@Table(name = "STORAGE_CATEGORY")
public class StorageCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @MappingForm(needMapping = true, caption = "Введите название категории хранилища...")
  @Column(name = "NAME")
  private String name;

  @OneToMany(mappedBy = "storageCategory", fetch = FetchType.LAZY)
  private List<Storage> storageList;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id")
  private Users users;

  //

}
