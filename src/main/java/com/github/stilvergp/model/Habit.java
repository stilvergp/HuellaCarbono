package com.github.stilvergp.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "habit")
public class Habit {
    @EmbeddedId
    private HabitId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private com.github.stilvergp.model.User user;

    @MapsId("activityId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "frequency", nullable = false)
    private Integer frequency;

    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "last_date")
    private Instant lastDate;

    public HabitId getId() {
        return id;
    }

    public void setId(HabitId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getLastDate() {
        return lastDate;
    }

    public void setLastDate(Instant lastDate) {
        this.lastDate = lastDate;
    }

}