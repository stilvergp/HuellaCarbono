package com.github.stilvergp.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.Habit;
import com.github.stilvergp.model.HabitId;
import org.hibernate.Session;

public class HabitDAO {
    private static Session session;

    public HabitDAO() {
        session = Connection.getInstance().getSession();
    }

    public void save(Habit habit) {
        session.beginTransaction();
        session.persist(habit);
        session.getTransaction().commit();
    }

    public Habit findById(HabitId id) {
        return session.get(Habit.class, id);
    }

    public void update(Habit habit) {
        session.beginTransaction();
        session.merge(habit);
        session.getTransaction().commit();
    }

    public void delete(Habit habit) {
        session.beginTransaction();
        session.remove(habit);
        session.getTransaction().commit();
    }

    public void close() {
        session.close();
    }
}
