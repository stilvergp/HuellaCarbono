package com.github.stilvergp.services;

import com.github.stilvergp.model.dao.UserDAO;
import com.github.stilvergp.model.entities.Category;
import com.github.stilvergp.model.entities.User;

import java.math.BigDecimal;
import java.util.List;

public class UserService {

    public void save(User user) {
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            userDAO.save(user);
        }
    }

    public User getUserByName(String name) {
        User user = null;
        if (!name.trim().isEmpty()) {
            UserDAO userDAO = new UserDAO();
            user = userDAO.findByName(name);
        }
        return user;
    }

    public List<User> getUsers() {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUsers();
    }

    public BigDecimal getImpact(User user, Category category) {
        BigDecimal impact = null;
        if (user != null && category != null) {
            UserDAO userDAO = new UserDAO();
            impact = userDAO.getImpact(user, category);
            if (impact == null) {
                impact = new BigDecimal(0);
            }
        }
        return impact;
    }

    public void update(User user) {
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            userDAO.update(user);
        }
    }

    public void delete(User user) {
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            User isInDatabase = userDAO.findById(user.getId());
            if (isInDatabase != null) {
                userDAO.delete(user);
            }
        }
    }
}
