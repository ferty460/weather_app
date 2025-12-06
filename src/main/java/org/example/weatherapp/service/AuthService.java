package org.example.weatherapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.UserDto;
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
