package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.Category;
import com.github.stilvergp.model.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;

public class UserDAO {
    private static Session session;

    public UserDAO() {
        session = Connection.getInstance().getSession();
    }

    private Session getSession() {
        if (session == null || !session.isOpen()) {
            session = Connection.getInstance().getSession();
        }
        return session;
    }

    public void save(User user) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public User findById(int id) {
        try (Session session = getSession()) {
            return session.get(User.class, id);
        }
    }

    public User findByName(String name) {
        try (Session session = getSession()) {
            Query<User> query = session.createQuery("from User where name=:name", User.class)
                    .setParameter("name", name);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getUsers() {
        try (Session session = getSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    public BigDecimal getImpact(User user, Category category) {
        try (Session session = getSession()) {
            Query<BigDecimal> hql = session.createQuery("SELECT SUM(f.value * c.emissionFactor) " +
                            "FROM Footprint f " +
                            "JOIN f.activity a " +
                            "JOIN a.category c " +
                            "WHERE f.user = :user " +
                            "AND c = :category", BigDecimal.class)
                    .setParameter("user", user)
                    .setParameter("category", category);
            return hql.uniqueResult();
        }
    }

    public void update(User user) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void delete(User user) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
