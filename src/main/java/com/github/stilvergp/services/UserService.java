package com.github.stilvergp.services;

import com.github.stilvergp.dao.UserDAO;
import com.github.stilvergp.model.User;

public class UserService {

    public void save(User user) {
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            User isInDatabase = userDAO.findById(user.getId());
            if (isInDatabase == null) {
                userDAO.save(user);
            }
            userDAO.close();
        }
    }

    public User getUserByName(String name) {
        User user = null;
        if (!name.trim().isEmpty()) {
            UserDAO userDAO = new UserDAO();
            user = userDAO.findByName(name);
            userDAO.close();
        }
        return user;
    }

    public void delete(User user) {
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            User isInDatabase = userDAO.findById(user.getId());
            if (isInDatabase != null) {
                userDAO.delete(user);
            }
            userDAO.close();
        }
    }
}
