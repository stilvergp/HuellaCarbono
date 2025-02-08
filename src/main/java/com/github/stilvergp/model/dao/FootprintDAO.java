package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class FootprintDAO {
    private static Session session;

    public FootprintDAO() {
        session = Connection.getInstance().getSession();
    }

    private Session getSession() {
        if (session == null || !session.isOpen()) {
            session = Connection.getInstance().getSession();
        }
        return session;
    }

    public void save(Footprint footprint) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.persist(footprint);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public Footprint findById(int id) {
        try (Session session = getSession()) {
            return session.get(Footprint.class, id);
        }
    }

    public List<Footprint> findByUser(User user) {
        try (Session session = getSession()) {
            Query<Footprint> query = session.createQuery("from Footprint where user = :user", Footprint.class)
                    .setParameter("user", user);
            return query.list();
        }
    }

    public void update(Footprint footprint) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.merge(footprint);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void delete(Footprint footprint) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.remove(footprint);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
