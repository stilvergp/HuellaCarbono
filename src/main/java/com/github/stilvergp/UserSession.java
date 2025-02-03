package com.github.stilvergp;

import com.github.stilvergp.model.entities.User;

public class UserSession {
    private static UserSession _instance;
    private User loggedInUser;

    public UserSession() {
        loggedInUser = null;
    }

    public static UserSession getInstance() {
        if (_instance == null) {
            _instance = new UserSession();
        }
        return _instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login(User user) {
        loggedInUser = user;
    }

    public void logout() {
        loggedInUser = null;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }
}
