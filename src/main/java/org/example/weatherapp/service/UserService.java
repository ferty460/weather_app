package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User with login " + login + " not found"));
    }

    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public void create(User user) {
        userRepository.save(user);
    }

}
