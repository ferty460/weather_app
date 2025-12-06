package org.example.weatherapp.repository;

import org.example.weatherapp.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);

}
