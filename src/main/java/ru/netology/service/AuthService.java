package ru.netology.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.netology.request.AuthenticationRequest;
import ru.netology.response.AuthenticationResponse;
import ru.netology.jwt.TokenUtil;
import ru.netology.repository.AuthRepository;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authenticationRepository;
    private final AuthenticationManager authenticationManager;
    private static final int  START_OF_TOKEN= 7;
    private TokenUtil tokenUtil;
    private UserService userService;
    //метод авторизации
    public AuthenticationResponse login(AuthenticationRequest authenticationRQ) {
        // получение данных
        final String username = authenticationRQ.getLogin();
        final String password = authenticationRQ.getPassword();
        //проверка пользователя
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userService.loadUserByUsername(username);
        //создание токена
        final String token = tokenUtil.generateToken(userDetails);
        //сохранение токена и иени юзера в мапу репозитория
        authenticationRepository.putTokenAndUsername(token, username);
        //логируем действие
        log.info("User {} authentication. JWT: {}", username, token);
        return new AuthenticationResponse(token);
    }
    //метод выхода итз учетки
    public void logout(String authToken) {
        //вырезаем только токен
        final String token = authToken.substring(START_OF_TOKEN);
        //получаю по токену имя
        final String username = authenticationRepository.getUsernameByToken(token);
        //логирую что пользователь вышел
        log.info("User {} logout. JWT is disabled.", username);
        //удаляю запись
        authenticationRepository.removeTokenAndUsernameByToken(token);
    }
}