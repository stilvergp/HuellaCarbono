package com.github.stilvergp.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.Footprint;
import com.github.stilvergp.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class FootprintDAO {
    private static Session session;

    public FootprintDAO() {
        session = Connection.getInstance().getSession();
    }

    public void save(Footprint footprint) {
        session.beginTransaction();
        session.persist(footprint);
        session.getTransaction().commit();
    }

    public Footprint findById(int id) {
        return session.get(Footprint.class, id);
    }

    public List<Footprint> findByUser(User user) {
        Query<Footprint> query = session.createQuery("from Footprint where user = :user", Footprint.class)
                .setParameter("user", user);
        return query.list();
    }

    public void update(Footprint footprint) {
        session.beginTransaction();
        session.merge(footprint);
        session.getTransaction().commit();
    }

    public void delete(Footprint footprint) {
        session.beginTransaction();
        session.remove(footprint);
        session.getTransaction().commit();
    }

    public void close() {
        session.close();
    }
}
