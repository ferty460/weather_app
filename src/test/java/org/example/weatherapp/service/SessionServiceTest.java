package org.example.weatherapp.service;

import org.example.weatherapp.config.TestHibernateConfig;
import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.exception.session.SessionExpiredException;
import org.example.weatherapp.mapper.UserMapper;
import org.example.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringJUnitConfig(TestHibernateConfig.class)
public class SessionServiceTest {

    @MockitoBean
    private LocationService locationService;

    @MockitoBean
    private OpenWeatherApiService openWeatherApiService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void sessionExpires_correctly() throws InterruptedException {
        UserRequest userRequest = new UserRequest("Dummy", "dummyPass");
        userRepository.save(userMapper.toEntity(userRequest));

        Session session = sessionService.create(userRequest);
        session.setExpiresAt(LocalDateTime.now().plusSeconds(1));

        Thread.sleep(2000);

        String sessionId = session.getId().toString();
        assertThrows(SessionExpiredException.class, () -> sessionService.getById(sessionId));
    }

}
