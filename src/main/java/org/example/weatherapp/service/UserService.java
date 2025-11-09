package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User getById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }

        throw new RuntimeException("User with id " + id + " not found");
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

}
