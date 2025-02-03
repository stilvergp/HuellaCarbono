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

    public void save(Habit habit) {
        try {
            session.beginTransaction();
            session.persist(habit);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public Habit findById(HabitId id) {
        return session.get(Habit.class, id);
    }

    public List<Habit> findByUser(User user) {
        Query<Habit> query = session.createQuery("FROM Habit WHERE user = :user", Habit.class)
                .setParameter("user", user);
        return query.list();
    }

    public void update(Habit habit) {
        try {
            session.beginTransaction();
            session.merge(habit);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void delete(Habit habit) {
        try {
            session.beginTransaction();
            session.remove(habit);
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
