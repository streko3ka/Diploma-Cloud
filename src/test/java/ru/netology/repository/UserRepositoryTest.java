package ru.netology.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.DataForTest.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    //ищем юзера
    @Test
    void findByUsername() {
        //предварительно готовим тестовые данные
        userRepository.deleteAll();
        userRepository.save(USER_1);
        //ищем юзера и сравниваем
        assertEquals(USER_1, userRepository.findByUsername(USERNAME_1));
    }
}