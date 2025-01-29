package com.github.stilvergp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class HabitId implements java.io.Serializable {
    private static final long serialVersionUID = 4075999080189554857L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "activity_id", nullable = false)
    private Integer activityId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HabitId entity = (HabitId) o;
        return Objects.equals(this.activityId, entity.activityId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, userId);
    }

}