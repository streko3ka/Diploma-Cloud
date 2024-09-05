package ru.netology.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import ru.netology.jwt.TokenUtil;
import ru.netology.repository.AuthRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.DataForTest.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {
    //мок аннотация позволяющая использовать зависимости класса сервиса (для проверки этого класса)
    @InjectMocks
    private AuthService authService;
    //мок объект репозитория
    @Mock
    private AuthRepository authRepository;

    @Mock
    private AuthenticationManager authManager;
    //мок объект класса работающего с токеном и информацией о пользователе
    @Mock
    private TokenUtil jwtTokenUtil;
    //мок сервиса юзер
    @Mock
    private UserService userService;
    //тест авторизации
    @Test
    void login() {
        //проверка наличия юзера по которому делается проверка
        Mockito.when(userService.loadUserByUsername(USERNAME_1)).thenReturn(USER_1);
        //проверка генерации токена
        Mockito.when(jwtTokenUtil.generateToken(USER_1)).thenReturn(TEST_TOKEN_1);
        //проверка текстового значения ответа
        assertEquals(AUTHENTICATION_RS, authService.login(AUTHENTICATION_RQ));
    }
    //тест выхода из аккаунта
    @Test
    void logout() {
        //проверка получения юзера из токена
        Mockito.when(authRepository.getUsernameByToken(BEARER_TOKEN_SUBSTRING_7)).thenReturn(USERNAME_1);
        //выходит по токену
        authService.logout(TEST_TOKEN);
        //так как метод void, проверяем количествол вызовов
        Mockito.verify(authRepository, Mockito.times(1)).getUsernameByToken(BEARER_TOKEN_SUBSTRING_7);
        Mockito.verify(authRepository, Mockito.times(1)).removeTokenAndUsernameByToken(BEARER_TOKEN_SUBSTRING_7);
    }
}