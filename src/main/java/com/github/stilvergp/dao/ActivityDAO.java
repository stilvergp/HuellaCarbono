package com.github.stilvergp.dao;

import com.github.stilvergp.connection.Connection;
import com.github.stilvergp.model.Activity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;


public class ActivityDAO {
    private static Session session;

    public ActivityDAO() {
        session = Connection.getInstance().getSession();
    }

    public BigDecimal getEmissionFromActivity(Activity activity) {
        Query<BigDecimal> hql = session.createQuery("SELECT a.category.emissionFactor FROM Activity a WHERE a.id = :id", BigDecimal.class)
                .setParameter("id", activity.getId());
        return hql.uniqueResult();
    }

    public String getUnitFromActivity(Activity activity) {
        Query<String> hql = session.createQuery("SELECT a.category.unit FROM Activity a WHERE a.id = :id", String.class)
                .setParameter("id", activity.getId());
        return hql.uniqueResult();
    }



    public Activity findById(int id) {
        return session.get(Activity.class, id);
    }

    public List<Activity> findAll() {
        return session.createQuery("from Activity", Activity.class).list();
    }
}
