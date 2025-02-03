package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDAO {
    private static Session session;

    public UserDAO() {
        session = Connection.getInstance().getSession();
    }
    public void save(User user) {
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public User findById(int id) {
        return session.get(User.class, id);
    }

    public User findByName(String name) {
        User user;
        Query<User> query = session.createQuery("from User where name=:name", User.class)
                .setParameter("name", name);
        user = query.getSingleResult();
        return user;
    }

    public void delete(User user) {
        try {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void close() {
        session.close();
    }
}
