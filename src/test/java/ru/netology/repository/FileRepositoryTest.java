package ru.netology.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.netology.model.StorageFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.DataForTest.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileRepositoryTest {

    @Autowired
    private FileRepository storageFileRepository;

    @Autowired
    private UserRepository userRepository;

    //подготовка к тесту
    @BeforeEach
    void setup() {
        //удаляем все записи репозиториев
        userRepository.deleteAll();
        storageFileRepository.deleteAll();
        //сохраняем тестовые данные в репозитории
        userRepository.save(USER_1);
        storageFileRepository.save(STORAGE_FILE_1);
        storageFileRepository.save(FOR_RENAME_STORAGE_FILE);
    }

    //удаление
    @Test
    void deleteByUserAndFilename() {
        //проверяем, что запись с именем файла FILENAME_1 присутствует в storageFileRepository
        Optional<StorageFile> beforeDelete = storageFileRepository.findById(FILENAME_1);
        assertTrue(beforeDelete.isPresent());
        //удаляем запись с пользователем USER_1 и именем файла
        storageFileRepository.deleteByUserAndFilename(USER_1, FILENAME_1);
        //проверяем, что запись с именем файла FILENAME_1 больше отсутствует
        Optional<StorageFile> afterDelete = storageFileRepository.findById(FILENAME_1);
        assertFalse(afterDelete.isPresent());
    }

    //поиск файла по имени пользователя и имени файла
    @Test
    void findByUserAndFilename() {
        //проверяем, что метод возвращает запись STORAGE_FILE_1
        assertEquals(STORAGE_FILE_1, storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1));
    }

    //изменение имени файла
    @Test
    void editFileNameByUser() {
        //ищем есть ли нужный файл по имени
        Optional<StorageFile> beforeEditName = storageFileRepository.findById(FOR_RENAME_FILENAME);
        assertTrue(beforeEditName.isPresent());
        assertEquals(FOR_RENAME_FILENAME, beforeEditName.get().getFilename());
        //изменяем имя файла на новое (используем имя юзера, имя файла и новое имя файла)
        storageFileRepository.editFileNameByUser(USER_1, FOR_RENAME_FILENAME, NEW_FILENAME);
        //проверяем что файл изменен используя поиск по новому имени
        Optional<StorageFile> afterEditName = storageFileRepository.findById(NEW_FILENAME);
        assertTrue(afterEditName.isPresent());
        //проверяем на совпадение измененный файл и имя, на которое производилась замена
        assertEquals(NEW_FILENAME, afterEditName.get().getFilename());
    }

    //проверяем, что метод возвращает список записей файлов
    @Test
    void findAllByUser() {
        assertEquals(List.of(STORAGE_FILE_1, FOR_RENAME_STORAGE_FILE), storageFileRepository.findAllByUser(USER_1));
    }
}