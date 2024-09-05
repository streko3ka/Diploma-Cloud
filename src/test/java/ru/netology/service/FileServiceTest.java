package ru.netology.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.netology.exception.InputDataException;
import ru.netology.exception.UnauthorizedException;
import ru.netology.repository.AuthRepository;
import ru.netology.repository.FileRepository;
import ru.netology.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.netology.DataForTest.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileServiceTest {
    //тестирование методов с успешной работой и на проброс исключений
    @InjectMocks
    private FileService storageFileService;

    @Mock
    private FileRepository storageFileRepository;

    @Mock
    private AuthRepository authenticationRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(authenticationRepository.getUsernameByToken(BEARER_TOKEN_SPLIT)).thenReturn(USERNAME_1);
        Mockito.when(userRepository.findByUsername(USERNAME_1)).thenReturn(USER_1);
    }

    @Test
    void uploadFile() {
        assertTrue(storageFileService.uploadFile(TEST_TOKEN, FILENAME_1, MULTIPART_FILE));
    }

    @Test
    void uploadFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> storageFileService.uploadFile(TEST_TOKEN_1, FILENAME_1, MULTIPART_FILE));
    }

    @Test
    void deleteFile() {
        storageFileService.deleteFile(TEST_TOKEN, FILENAME_1);
        Mockito.verify(storageFileRepository, Mockito.times(1)).deleteByUserAndFilename(USER_1, FILENAME_1);
    }

    @Test
    void deleteFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> storageFileService.deleteFile(TEST_TOKEN_1, FILENAME_1));
    }

    @Test
    void deleteFileInputDataException() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(InputDataException.class, () -> storageFileService.deleteFile(TEST_TOKEN, FILENAME_1));
    }

    @Test
    void downloadFile() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertEquals(FILE_CONTENT_1, storageFileService.downloadFile(TEST_TOKEN, FILENAME_1));
    }

    @Test
    void downloadFileUnauthorized() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(UnauthorizedException.class, () -> storageFileService.downloadFile(TEST_TOKEN_1, FILENAME_1));
    }

    @Test
    void downloadFileInputDataException() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(InputDataException.class, () -> storageFileService.downloadFile(TEST_TOKEN, FILENAME_2));
    }

    @Test
    void editFileName() {
        storageFileService.editFileName(TEST_TOKEN, FILENAME_1, EDIT_FILE_NAME_RQ);
        Mockito.verify(storageFileRepository, Mockito.times(1)).editFileNameByUser(USER_1, FILENAME_1, NEW_FILENAME);
    }

    @Test
    void editFileNameUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> storageFileService.editFileName(TEST_TOKEN_1, FILENAME_1, EDIT_FILE_NAME_RQ));
    }

    @Test
    void editFileNameInputDataException() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(InputDataException.class, () -> storageFileService.deleteFile(TEST_TOKEN, FILENAME_1));
    }

    @Test
    void getAllFiles() {
        Mockito.when(storageFileRepository.findAllByUser(USER_1)).thenReturn(STORAGE_FILE_LIST);
        assertEquals(FILE_RS_LIST, storageFileService.getAllFiles(TEST_TOKEN, LIMIT));
    }

    @Test
    void getAllFilesUnauthorized() {
        Mockito.when(storageFileRepository.findAllByUser(USER_1)).thenReturn(STORAGE_FILE_LIST);
        assertThrows(UnauthorizedException.class, () -> storageFileService.getAllFiles(TEST_TOKEN_1, LIMIT));
    }
}