package org.example.weatherapp.repository;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT u FROM User u LEFT JOIN FETCH u.locations WHERE u.id = :id";

        User user = session.createQuery(hql, User.class)
                .setParameter("id", id)
                .uniqueResult();

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.locations";

        return session.createQuery(hql, User.class).getResultList();
    }

    @Override
    public User save(User entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);

        return entity;
    }

    @Override
    public void update(User entity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public void delete(User entity) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(entity);
    }

}
