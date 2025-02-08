package com.github.stilvergp.services;

import com.github.stilvergp.model.dao.FootprintDAO;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.User;

import java.util.List;

public class FootprintService {

    public void save(Footprint footprint) {
        if (footprint != null) {
            FootprintDAO footprintDAO = new FootprintDAO();
            footprintDAO.save(footprint);
        }
    }

    public Footprint findById(int id) {
        Footprint footprint = null;
        if (id > 0) {
            FootprintDAO footprintDAO = new FootprintDAO();
            footprint = footprintDAO.findById(id);
        }
        return footprint;
    }

    public List<Footprint> findByUser(User user) {
        List<Footprint> footprints = null;
        if (user != null) {
            FootprintDAO footprintDAO = new FootprintDAO();
            footprints = footprintDAO.findByUser(user);
        }
        return footprints;
    }

    public void update(Footprint footprint) {
        if (footprint != null) {
            FootprintDAO footprintDAO = new FootprintDAO();
            Footprint isInDatabase = footprintDAO.findById(footprint.getId());
            if (isInDatabase != null) {
                footprintDAO.update(footprint);
            }
        }
    }

    public void delete(Footprint footprint) {
        if (footprint != null) {
            FootprintDAO footprintDAO = new FootprintDAO();
            Footprint isInDatabase = footprintDAO.findById(footprint.getId());
            if (isInDatabase != null) {
                footprintDAO.delete(footprint);
            }
        }
    }
}
