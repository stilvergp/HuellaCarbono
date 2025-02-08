package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.Activity;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.Habit;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;


public class ActivityDAO {
    private static Session session;

    public ActivityDAO() {
        session = Connection.getInstance().getSession();
    }

    private Session getSession() {
        if (session == null || !session.isOpen()) {
            session = Connection.getInstance().getSession();
        }
        return session;
    }

    public BigDecimal getEmissionFromActivity(Activity activity) {
        try (Session session = getSession()) {
            Query<BigDecimal> hql = session.createQuery("SELECT a.category.emissionFactor FROM Activity a WHERE a.id = :id", BigDecimal.class)
                    .setParameter("id", activity.getId());
            return hql.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getUnitFromActivity(Activity activity) {
        try (Session session = getSession()) {
            Query<String> hql = session.createQuery("SELECT a.category.unit FROM Activity a WHERE a.id = :id", String.class)
                    .setParameter("id", activity.getId());
            return hql.uniqueResult();
        }
    }

    public Activity findById(int id) {
        try (Session session = getSession()) {
            return session.get(Activity.class, id);
        }
    }

    public List<Activity> findAll() {
        try (Session session = getSession()) {
            return session.createQuery("from Activity", Activity.class).list();
        }
    }

    public Activity getByFootprint(Footprint footprint) {
        try (Session session = getSession()) {
            Query<Activity> hql = session.createQuery("SELECT f.activity FROM Footprint f WHERE f.id = :id", Activity.class)
                    .setParameter("id", footprint.getId());
            return hql.uniqueResult();
        }
    }

    public Activity getByHabit(Habit habit) {
        try (Session session = getSession()) {
            Query<Activity> hql = session.createQuery("SELECT h.activity FROM Habit h WHERE h.id = :id", Activity.class)
                    .setParameter("id", habit.getId());
            return hql.uniqueResult();
        }
    }
}
