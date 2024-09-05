package ru.netology.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.netology.exception.UnauthorizedException;
import ru.netology.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.DataForTest.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
    //мок класс позволяющий использовать зависимости класса
    @InjectMocks
    private UserService userService;
    //мок объект репозитория
    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        //проверяем наличие юзера используя функции мокито
        Mockito.when(userRepository.findByUsername(USERNAME_1)).thenReturn(USER_1);
        //делаем непосредственно сравнение юзера и результата поиска по исходным данным
        assertEquals(USER_1, userService.loadUserByUsername(USERNAME_1));
    }

    //проверка входа
    @Test
    void loadUserByUsernameUnauthorized() {
        //сравниваем исключение и будет ли такое же исключение при поиске по юзеру которого поиск не найдет
        assertThrows(UnauthorizedException.class, () -> userService.loadUserByUsername(USERNAME_2));
    }
}