package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.HabitId;
import com.github.stilvergp.model.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HabitDAO {
    private static Session session;

    public HabitDAO() {
        session = Connection.getInstance().getSession();
    }

    private Session getSession() {
        if (session == null || !session.isOpen()) {
            session = Connection.getInstance().getSession();
        }
        return session;
    }

    public void save(Habit habit) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.merge(habit);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public Habit findById(HabitId id) {
        try (Session session = getSession()) {
            return session.get(Habit.class, id);
        }
    }

    public List<Habit> findByUser(User user) {
        try (Session session = getSession()) {
            Query<Habit> query = session.createQuery("FROM Habit WHERE user = :user", Habit.class)
                    .setParameter("user", user);
            return query.list();
        }
    }

    public void update(Habit habit) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.merge(habit);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void delete(Habit habit) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.remove(habit);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
