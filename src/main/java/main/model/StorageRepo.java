package main.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepo extends JpaRepository<Storage, Long> {

  List<Storage> findAllByStorageCategory(StorageCategory storageCategory);

}
