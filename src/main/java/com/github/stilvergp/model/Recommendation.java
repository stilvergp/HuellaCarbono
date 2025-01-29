package com.github.stilvergp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "estimated_impact", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedImpact;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEstimatedImpact() {
        return estimatedImpact;
    }

    public void setEstimatedImpact(BigDecimal estimatedImpact) {
        this.estimatedImpact = estimatedImpact;
    }

}