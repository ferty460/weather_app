package org.example.weatherapp.repository;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Session> findById(UUID id) {
        String hql = "SELECT s FROM Session s LEFT JOIN FETCH s.user WHERE s.id = :id";

        Session session = sessionFactory.getCurrentSession().createQuery(hql, Session.class)
                .setParameter("id", id)
                .uniqueResult();

        return Optional.ofNullable(session);
    }

    @Override
    public List<Session> findAll() {
        String hql = "SELECT DISTINCT s FROM Session s LEFT JOIN FETCH s.user";

        return sessionFactory.getCurrentSession().createQuery(hql, Session.class).getResultList();
    }

    @Override
    public Session save(Session entity) {
        sessionFactory.getCurrentSession().persist(entity);

        return entity;
    }

    @Override
    public void update(Session entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public void delete(Session entity) {
        sessionFactory.getCurrentSession().remove(entity);
    }

}
