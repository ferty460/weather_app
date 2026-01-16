package org.example.weatherapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.exception.auth.InvalidCredentialsException;
import org.example.weatherapp.exception.auth.UserAlreadyExistsException;
import org.example.weatherapp.exception.auth.UserNotFoundException;
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
        String login = userRequest.login();
        if (userService.existsByLogin(login)) {
            throw new UserAlreadyExistsException(login);
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userService.create(user);
    }

    @Transactional
    public void login(UserRequest userRequest, HttpServletResponse response) {
        User user;

        try {
            user = userService.getByLogin(userRequest.login());
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }

        if (!BCrypt.checkpw(userRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Session session = sessionService.create(userRequest);
        WebUtil.setSessionCookie(session.getId(), response);
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        WebUtil.getSessionIdFromCookies(request.getCookies())
                .ifPresent(sessionId -> WebUtil.deleteSessionCookie(response));
    }

}
