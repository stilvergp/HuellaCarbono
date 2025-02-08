package com.github.stilvergp.services;

import com.github.stilvergp.model.dao.ActivityDAO;
import com.github.stilvergp.model.entities.Activity;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.Habit;

import java.math.BigDecimal;
import java.util.List;

public class ActivityService {

    public List<Activity> getActivities() {
        ActivityDAO activityDAO = new ActivityDAO();
        return activityDAO.findAll();
    }

    public BigDecimal getEmissionFactor(Activity activity) {
        BigDecimal bigDecimal = null;
        if (activity != null) {
            ActivityDAO activityDAO = new ActivityDAO();
            bigDecimal = activityDAO.getEmissionFromActivity(activity);
        }
        return bigDecimal;
    }

    public String getUnit(Activity activity) {
        String unit = null;
        if (activity != null) {
            ActivityDAO activityDAO = new ActivityDAO();
            unit = activityDAO.getUnitFromActivity(activity);
        }
        return unit;
    }

    public Activity getActivityByHabit(Habit habit) {
        Activity activity = null;
        if (habit != null) {
            ActivityDAO activityDAO = new ActivityDAO();
            activity = activityDAO.getByHabit(habit);
        }
        return activity;
    }

    public Activity getActivityByFootprint(Footprint footprint) {
        Activity activity = null;
        if (footprint != null) {
            ActivityDAO activityDAO = new ActivityDAO();
            activity = activityDAO.getByFootprint(footprint);
        }
        return activity;
    }
}
