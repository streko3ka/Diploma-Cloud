package ru.netology.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.exception.UnauthorizedException;
import ru.netology.model.User;
import ru.netology.repository.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //делаем проверку на наличие такого пользователя и если нет, то выводит ошибку в логи и пробрасываем исключение
        User user = userRepository.findByUsername(username);
        if (user == null) {
            //записываем несанкционированный вход
            log.error("User Service: Unauthorized");
            throw new UnauthorizedException("User Service: Unauthorized");
        }
        //возвращаем пользователя
        return user;
    }
}