package org.example.weatherapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.UserMapper;
import org.example.weatherapp.util.WebUtil;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRequest userRequest) {
        if (userService.existsByLogin(userRequest.login())) {
            throw new IllegalArgumentException("User with login " + userRequest.login() + " already exists");
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userService.create(user);
    }

    @Transactional
    public void login(UserRequest userRequest, HttpServletResponse response) {
        User user = userService.getByLogin(userRequest.login());

        if (!BCrypt.checkpw(userRequest.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        Session session = sessionService.create(userRequest);
        WebUtil.setSessionCookie(session.getId(), response);
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        String sessionId = WebUtil.getSessionIdFromCookies(cookies);
        if (sessionId == null) {
            return;
        }

        WebUtil.deleteSessionCookie(response);
    }

}
