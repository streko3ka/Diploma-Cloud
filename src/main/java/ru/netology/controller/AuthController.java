package ru.netology.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.request.AuthenticationRequest;
import ru.netology.response.AuthenticationResponse;
import ru.netology.service.AuthService;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    //принимает post запрос с путем login
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRQ) {
        return authService.login(authenticationRQ);
    }

    //принимает post запрос с путем logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        authService.logout(authToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}