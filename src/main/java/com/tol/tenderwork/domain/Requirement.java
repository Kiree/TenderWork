package com.tol.tenderwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Requirement.
 */
@Entity
@Table(name = "requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "requirement")
public class Requirement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Min(value = 0)
    @Column(name = "total_duration")
    private Integer totalDuration;

    @Min(value = 0)
    @Column(name = "duration_specification")
    private Integer durationSpecification;

    @Min(value = 0)
    @Column(name = "duration_implementation")
    private Integer durationImplementation;

    @Min(value = 0)
    @Column(name = "duration_testing")
    private Integer durationTesting;

    @Min(value = 0)
    @Column(name = "synergy_benefit")
    private Integer synergyBenefit;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "owner_estimate_id")
    private Estimate ownerEstimate;

    @OneToMany(mappedBy = "ownerRequirement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> hasTaskss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getDurationSpecification() {
        return durationSpecification;
    }

    public void setDurationSpecification(Integer durationSpecification) {
        this.durationSpecification = durationSpecification;
    }

    public Integer getDurationImplementation() {
        return durationImplementation;
    }

    public void setDurationImplementation(Integer durationImplementation) {
        this.durationImplementation = durationImplementation;
    }

    public Integer getDurationTesting() {
        return durationTesting;
    }

    public void setDurationTesting(Integer durationTesting) {
        this.durationTesting = durationTesting;
    }

    public Integer getSynergyBenefit() {
        return synergyBenefit;
    }

    public void setSynergyBenefit(Integer synergyBenefit) {
        this.synergyBenefit = synergyBenefit;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Estimate getOwnerEstimate() {
        return ownerEstimate;
    }

    public void setOwnerEstimate(Estimate estimate) {
        this.ownerEstimate = estimate;
    }

    public Set<Task> getHasTaskss() {
        return hasTaskss;
    }

    public void setHasTaskss(Set<Task> tasks) {
        this.hasTaskss = tasks;
    }

    public void addTask(Task task) {
        this.hasTaskss.add(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Requirement requirement = (Requirement) o;
        if(requirement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, requirement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Requirement{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", totalDuration='" + totalDuration + "'" +
            ", durationSpecification='" + durationSpecification + "'" +
            ", durationImplementation='" + durationImplementation + "'" +
            ", durationTesting='" + durationTesting + "'" +
            ", synergyBenefit='" + synergyBenefit + "'" +
            '}';
    }
}
