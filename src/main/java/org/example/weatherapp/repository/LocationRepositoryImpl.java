package org.example.weatherapp.repository;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Location> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT l FROM Location l WHERE l.id = :id";

        Location location = session.createQuery(hql, Location.class)
                .setParameter("id", id)
                .uniqueResult();

        return Optional.ofNullable(location);
    }

    @Override
    public List<Location> findAllByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT l FROM Location l LEFT JOIN FETCH l.user u WHERE u.id = :userId";

        return session.createQuery(hql, Location.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Location> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT l FROM Location l";

        return session.createQuery(hql, Location.class).getResultList();
    }

    @Override
    public Location save(Location entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);

        return entity;
    }

    @Override
    public void update(Location entity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public void delete(Location entity) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(entity);
    }

}
