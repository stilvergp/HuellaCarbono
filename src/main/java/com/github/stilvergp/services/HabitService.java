package com.github.stilvergp.services;

import com.github.stilvergp.model.dao.HabitDAO;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.HabitId;
import com.github.stilvergp.model.entities.User;

import java.util.List;

public class HabitService {
    public void save(Habit habit) {
        if (habit != null) {
            HabitDAO habitDAO = new HabitDAO();
            habitDAO.save(habit);
        }
    }

    public Habit findById(int userId, int activityId) {
        Habit habit = null;
        if (userId > 0 && activityId > 0) {
            HabitDAO habitDAO = new HabitDAO();
            HabitId id = new HabitId();
            id.setUserId(userId);
            id.setActivityId(activityId);
            habit = habitDAO.findById(id);
            habitDAO.close();
        }
        return habit;
    }

    public List<Habit> findByUser(User user) {
        List<Habit> habits = null;
        if (user != null) {
            HabitDAO habitDAO = new HabitDAO();
            habits = habitDAO.findByUser(user);
        }
        return habits;
    }

    public void update(Habit habit) {
        if (habit != null) {
            HabitDAO habitDAO = new HabitDAO();
            Habit isInDatabase = habitDAO.findById(habit.getId());
            if (isInDatabase != null) {
                habitDAO.update(habit);
            }
        }
    }

    public void delete(Habit footprint) {
        if (footprint != null) {
            HabitDAO habitDAO = new HabitDAO();
            Habit isInDatabase = habitDAO.findById(footprint.getId());
            if (isInDatabase != null) {
                habitDAO.delete(footprint);
            }
            habitDAO.close();
        }
    }


}
