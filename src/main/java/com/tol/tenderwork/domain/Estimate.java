package com.tol.tenderwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Estimate.
 */
@Entity
@Table(name = "estimate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "estimate")
public class Estimate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 31)
    @Column(name = "workdays_in_month", nullable = false)
    private Integer workdaysInMonth;

    @NotNull
    @Min(value = 1)
    @Column(name = "desired_project_duration", nullable = false)
    private Integer desiredProjectDuration;

    @NotNull
    @Min(value = 1)
    @Column(name = "daily_price", nullable = false)
    private Long dailyPrice;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "specification_factor", nullable = false)
    private Float specificationFactor;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "testing_factor", nullable = false)
    private Float testingFactor;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "implementation_factor", nullable = false)
    private Float implementationFactor;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "synergy_benefit", nullable = false)
    private Float synergyBenefit;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "total_duration")
    private Float totalDuration;

    @Column(name = "resourcing")
    private Float resourcing;

    @Column(name = "total_synergy_benefit")
    private Float totalSynergyBenefit;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "owner_project_id")
    private Project ownerProject;

    @OneToMany(mappedBy = "ownerEstimate", cascade = CascadeType.REFRESH)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requirement> hasRequirementss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWorkdaysInMonth() {
        return workdaysInMonth;
    }

    public void setWorkdaysInMonth(Integer workdaysInMonth) {
        this.workdaysInMonth = workdaysInMonth;
    }

    public Integer getDesiredProjectDuration() {
        return desiredProjectDuration;
    }

    public void setDesiredProjectDuration(Integer desiredProjectDuration) {
        this.desiredProjectDuration = desiredProjectDuration;
    }

    public Long getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(Long dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Float getSpecificationFactor() {
        return specificationFactor;
    }

    public void setSpecificationFactor(Float specificationFactor) {
        this.specificationFactor = specificationFactor;
    }

    public Float getTestingFactor() {
        return testingFactor;
    }

    public void setTestingFactor(Float testingFactor) {
        this.testingFactor = testingFactor;
    }

    public Float getImplementationFactor() {
        return implementationFactor;
    }

    public void setImplementationFactor(Float implementationFactor) {
        this.implementationFactor = implementationFactor;
    }

    public Float getSynergyBenefit() {
        return synergyBenefit;
    }

    public void setSynergyBenefit(Float synergyBenefit) {
        this.synergyBenefit = synergyBenefit;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Float totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Float getResourcing() {
        return resourcing;
    }

    public void setResourcing(Float resourcing) {
        this.resourcing = resourcing;
    }

    public Float getTotalSynergyBenefit() {
        return totalSynergyBenefit;
    }

    public void setTotalSynergyBenefit(Float totalSynergyBenefit) {
        this.totalSynergyBenefit = totalSynergyBenefit;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Project getOwnerProject() {
        return ownerProject;
    }

    public void setOwnerProject(Project project) {
        this.ownerProject = project;
    }

    public Set<Requirement> getHasRequirementss() {
        return hasRequirementss;
    }

    public void setHasRequirementss(Set<Requirement> requirements) {
        this.hasRequirementss = requirements;
    }

    public void addRequirement(Requirement requirement) {
        removeRequirement(requirement);
        this.hasRequirementss.add(requirement);
    }

    public void removeRequirement(Requirement requirement) {
        if(this.hasRequirementss.contains(requirement)) {
            this.hasRequirementss.remove(requirement);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estimate estimate = (Estimate) o;
        if(estimate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, estimate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Estimate{" +
            "id=" + id +
            ", workdaysInMonth='" + workdaysInMonth + "'" +
            ", desiredProjectDuration='" + desiredProjectDuration + "'" +
            ", dailyPrice='" + dailyPrice + "'" +
            ", specificationFactor='" + specificationFactor + "'" +
            ", testingFactor='" + testingFactor + "'" +
            ", implementationFactor='" + implementationFactor + "'" +
            ", synergyBenefit='" + synergyBenefit + "'" +
            ", totalPrice='" + totalPrice + "'" +
            ", totalDuration='" + totalDuration + "'" +
            ", resourcing='" + resourcing + "'" +
            ", totalSynergyBenefit='" + totalSynergyBenefit + "'" +
            '}';
    }
}
