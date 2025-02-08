package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.Recommendation;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class RecommendationDAO {
    private static Session session;

    public RecommendationDAO() {
        session = Connection.getInstance().getSession();
    }

    private Session getSession() {
        if (session == null || !session.isOpen()) {
            session = Connection.getInstance().getSession();
        }
        return session;
    }

    public List<Recommendation> getByFootprint(Footprint footprint) {
        try (Session session = getSession()) {
            Query<Recommendation> hql = session.createQuery("SELECT r FROM Footprint f " +
                            "JOIN f.activity a " +
                            "JOIN a.category c " +
                            "JOIN c.recommendations r " +
                            "WHERE f.id = :id", Recommendation.class)
                    .setParameter("id", footprint.getId());
            return hql.list();
        }
    }

    public List<Recommendation> getByHabit(Habit habit) {
        try (Session session = getSession()) {
            Query<Recommendation> hql = session.createQuery("SELECT r from Habit h " +
                            "JOIN h.activity a " +
                            "JOIN a.category c " +
                            "JOIN c.recommendations r " +
                            "WHERE h.id = :id", Recommendation.class)
                    .setParameter("id", habit.getId());
            return hql.list();
        }
    }
}
