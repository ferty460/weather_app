package org.example.weatherapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.UserDto;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int ONE_HOUR_IN_SECONDS = 60 * 60;
    public static final String SESSION_COOKIE_NAME = "SESSION_ID";

    private final UserService userService;
    private final UserMapper userMapper;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;

    // todo: validation
    @Transactional
    public UserDto register(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));

        return userService.create(user);
    }

    @Transactional
    public void login(UserDto userDto, HttpServletResponse response) {
        User user = userService.getByLogin(userDto.login());

        if (!BCrypt.checkpw(userDto.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        Session session = sessionService.create(userDto);
        setSessionCookie(session.getId(), response);
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        String sessionId = null;
        for (Cookie cookie : cookies) {
            if ("SESSION_ID".equals(cookie.getName())) {
                sessionId = cookie.getValue();
            }
        }

        if (sessionId == null) {
            return;
        }

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }


    private void setSessionCookie(UUID sessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(ONE_HOUR_IN_SECONDS);

        response.addCookie(cookie);
    }

}
