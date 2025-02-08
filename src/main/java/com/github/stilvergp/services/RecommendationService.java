package com.github.stilvergp.services;

import com.github.stilvergp.model.dao.RecommendationDAO;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.Recommendation;

import java.util.List;

public class RecommendationService {

    public List<Recommendation> getRecommendationsByHabit(Habit habit) {
        List<Recommendation> recommendations = null;
        if (habit != null) {
            RecommendationDAO recommendationDAO = new RecommendationDAO();
            recommendations = recommendationDAO.getByHabit(habit);
        }
        return recommendations;
    }

    public List<Recommendation> getRecommendationsByFootprint(Footprint footprint) {
        List<Recommendation> recommendations = null;
        if (footprint != null) {
            RecommendationDAO recommendationDAO = new RecommendationDAO();
            recommendations = recommendationDAO.getByFootprint(footprint);
        }
        return recommendations;
    }
}
