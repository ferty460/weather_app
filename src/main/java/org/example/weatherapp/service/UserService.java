package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.exception.auth.UserNotFoundException;
import org.example.weatherapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByLogin(String login) throws UserNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public void create(User user) {
        userRepository.save(user);
    }

}
