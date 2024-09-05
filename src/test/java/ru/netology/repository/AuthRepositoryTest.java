package ru.netology.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.DataForTest.*;

class AuthRepositoryTest {

    private AuthRepository authRepository;

    @BeforeEach
    public void setup() {
        authRepository = new AuthRepository();
    }
    //проверка добавления токен-юзер в мапу
    @Test
    public void testPutTokenAndUsername() {
        authRepository.putTokenAndUsername(TEST_TOKEN_1, USERNAME_1);
        String retrievedUsername = authRepository.getUsernameByToken(TEST_TOKEN_1);
        assertEquals(USERNAME_1, retrievedUsername);
    }
    //проверка удаления
    @Test
    public void testRemoveTokenAndUsernameByToken() {
        authRepository.putTokenAndUsername(TEST_TOKEN_2, USERNAME_2);
        authRepository.removeTokenAndUsernameByToken(TEST_TOKEN_2);
        String retrievedUsername = authRepository.getUsernameByToken(TEST_TOKEN_2);
        assertNull(retrievedUsername);
    }
    //проверка метода получения юзера по токену (проверка по тестовым жданным 1 так как 2 добавлена и удалена (в прошлом методе)
    @Test
    public void testGetUsernameByTokenReturnsNullForNonexistentToken() {
        String retrievedUsername = authRepository.getUsernameByToken(TEST_TOKEN_1);
        assertNull(retrievedUsername);
    }
}