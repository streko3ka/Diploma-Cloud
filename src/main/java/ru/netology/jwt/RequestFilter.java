package ru.netology.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class RequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final TokenUtil tokenUtil;
    private static final int  START_OF_TOKEN= 7;
    public RequestFilter(UserService userService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    //фильтрация запроса
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //исзвеление токена
        final String requestTokenHeader = request.getHeader("auth-token");
        String username = null;
        String token = null;
        //если токен есть
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(START_OF_TOKEN);
            //исзвлекаем имя пользователя из токена
            try {
                username = tokenUtil.getUsernameFromToken(token);
                //пробрасываем исключение
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        } else {
            //логируем
            log.warn("JWT Token does not begin with Bearer String");
        }
        //если все условия выполняются, то создается объект
        // UsernamePasswordAuthenticationToken с информацией о
        // пользователе и устанавливается аутентификация в контексте
        // безопасности с помощью метода setAuthentication у
        // SecurityContextHolder. Затем вызывается метод doFilter
        // у FilterChain для продолжения обработки запроса фильтрами цепочки.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);

            if (tokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}