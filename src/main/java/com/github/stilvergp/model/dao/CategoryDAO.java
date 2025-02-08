package com.github.stilvergp.model.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.entities.Category;
import org.hibernate.Session;

import java.util.List;

public class CategoryDAO {
    private static Session session;

    public CategoryDAO() {
        session = Connection.getInstance().getSession();
    }

    private Session getSession() {
        if (session == null || !session.isOpen()) {
            session = Connection.getInstance().getSession();
        }
        return session;
    }

    public List<Category> getCategories() {
        try (Session session = getSession()) {
            return session.createQuery("from Category", Category.class).list();
        }
    }
}
