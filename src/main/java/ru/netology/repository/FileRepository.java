package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.model.StorageFile;
import ru.netology.model.User;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<StorageFile, String> {
    //метод для удаления файла из базы данных на основе пользователя и имени файла
    void deleteByUserAndFilename(User user, String filename);
    //метод для поиска файла в базе данных на основе пользователя и имени файла
    StorageFile findByUserAndFilename(User user, String filename);
    //метод для изменения имени файла в базе данных на основе пользователя, старого имени файла и нового имени
    @Modifying
    @Query("update StorageFile f set f.filename = :newName where f.filename = :filename and f.user = :user")
    void editFileNameByUser(@Param("user") User user, @Param("filename") String filename, @Param("newName") String newName);
    //возвращает список объектов
    List<StorageFile> findAllByUser(User user);
}