package org.example.weatherapp.service;

import org.example.weatherapp.config.TestHibernateConfig;
import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.exception.auth.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringJUnitConfig(TestHibernateConfig.class)
public class AuthServiceTest {

    @MockitoBean
    private LocationService locationService;

    @MockitoBean
    private OpenWeatherApiService openWeatherApiService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Test
    void whenUserRegister_thenUserPresentInDb() {
        UserRequest userRequest = new UserRequest("Dummy", "dummyPass");
        authService.register(userRequest);

        boolean isExists = userService.existsByLogin("Dummy");
        assertTrue(isExists, "User should be saved in the database");
    }

    @Test
    void whenUserRegisterWithNonUniqueLogin_thenThrowsException() {
        UserRequest user1 = new UserRequest("Dummy", "dummyPass");
        authService.register(user1);

        UserRequest user2 = new UserRequest("Dummy", "dummyPass");

        assertThatThrownBy(() -> authService.register(user2))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with username '" + user2.login() + "' already exists");
    }

    @Test
    void whenUserRegister_thenPasswordIsEncrypts() {
        UserRequest userRequest = new UserRequest("Dummy", "dummyPass");
        authService.register(userRequest);

        User user = userService.getByLogin(userRequest.login());
        assertNotEquals("dummyPass", user.getPassword(), "Password should be encoded");
    }

}
