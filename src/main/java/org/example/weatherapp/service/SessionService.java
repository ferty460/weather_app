package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.exception.session.SessionExpiredException;
import org.example.weatherapp.exception.session.SessionNotFoundException;
import org.example.weatherapp.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final int SESSION_EXPIRATION_HOURS = 1;

    private final SessionRepository sessionRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Session getById(String id) {
        Session session = sessionRepository.findById(UUID.fromString(id))
                .orElseThrow(SessionNotFoundException::new);

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new SessionExpiredException();
        }

        return session;
    }

    @Transactional
    public Session create(UserRequest userRequest) {
        User user = userService.getByLogin(userRequest.login());
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(SESSION_EXPIRATION_HOURS);

        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(expiresAt);

        return sessionRepository.save(session);
    }

}
