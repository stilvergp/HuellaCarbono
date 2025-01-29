package com.github.stilvergp.services;

import com.github.stilvergp.dao.ActivityDAO;
import com.github.stilvergp.model.Activity;

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
}
