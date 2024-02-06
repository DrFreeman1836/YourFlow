package main.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageCategoryRepo extends JpaRepository<StorageCategory, Long> {

  List<StorageCategory> findAllByUsers(Users users);

}
