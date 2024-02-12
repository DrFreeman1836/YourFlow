package main.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import main.exception.UserException;
import main.model.Storage;
import main.model.StorageCategory;
import main.model.StorageCategoryRepo;
import main.model.StorageRepo;
import main.model.Users;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageService {

  private final StorageCategoryRepo storageCategoryRepo;

  private final StorageRepo storageRepo;

  public void saveStorageCategory(Users users, String nameCategory) {
    StorageCategory storageCategory = new StorageCategory();
    storageCategory.setUsers(users);
    storageCategory.setName(nameCategory);
    storageCategoryRepo.save(storageCategory);
  }

  public List<Storage> findAllStorageByUsersAndCategory(StorageCategory storageCategory) {
    return storageRepo.findAllByStorageCategory(storageCategory);
  }

  public StorageCategory findCategoryById(Long idCategory) {
    return storageCategoryRepo.findById(idCategory).orElseThrow(() -> new UserException("Категория не найдена, выполните команду /start"));
  }

  public void deleteCategoryById(Long id) {
    storageCategoryRepo.deleteById(id);
  }

  public void deleteStorageById(Long id) {
    storageRepo.deleteById(id);
  }

  public List<StorageCategory> findAllCategoriesByUsers(Users users) {
    return storageCategoryRepo.findAllByUsers(users);
  }

}
