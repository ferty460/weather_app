package org.example.weatherapp.repository;

import org.example.weatherapp.entity.Session;

import java.util.UUID;

public interface SessionRepository extends CrudRepository<Session, UUID> {

}
