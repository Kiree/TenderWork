package com.tol.tenderwork.domain;

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
    //Työpäiviä kuukaudessa (1 - 31) keskimäärin
    private Integer workdaysInMonth;

    @NotNull
    @Min(value = 1)
    @Column(name = "desired_project_duration", nullable = false)
    //Projektin toivekesto KUUKAUSIA
    private Integer desiredProjectDuration;

    @NotNull
    @Min(value = 1)
    @Column(name = "daily_price", nullable = false)
    //Päivähinta
    private Long dailyPrice;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "specification_factor", nullable = false)
    //Määrityskerroin 0-1
    private Float specificationFactor;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "testing_factor", nullable = false)
    //Testauskerroin 0-1
    private Float testingFactor;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "implementation_factor", nullable = false)
    //Toteutuskerroin 0-1
    private Float implementationFactor;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "synergy_benefit", nullable = false)
    //Synergiaetukerroin 0-1
    private Float synergyBenefit;

    @Column(name = "total_price")
    //Itsenäinen tuote: kesto(htp) x päivähinta
    //Synergialla: (kesto - hyöty) x päivähinta [jos synergyBenefit > 0]
    private Long totalPrice;

    @Column(name = "total_duration")
    //Itsenäinen tuote: (htp), vaatimuksien työmääräarvio yhteensä
    //Synergialla: Työmääräarvio - hyöty [jos synergyBenefit > 0]
    private Integer totalDuration;

    @Column(name = "resourcing")
    //Itsenäinen tuote: Kesto / (työpäiviä kuukaudessa * toivekesto kuukausissa)
    //Synergialla: Sama [jos synergyBenefit > 0]
    private Integer resourcing;

    @Column(name = "total_synergy_benefit")
    //Synergiahyöty: Hyöty yhteensä (htp) vaatimuksista [jos synergyBenefit > 0]
    private Integer totalSynergyBenefit;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "owner_project_id")
    private Project ownerProject;

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

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getResourcing() {
        return resourcing;
    }

    public void setResourcing(Integer resourcing) {
        this.resourcing = resourcing;
    }

    public Integer getTotalSynergyBenefit() {
        return totalSynergyBenefit;
    }

    public void setTotalSynergyBenefit(Integer totalSynergyBenefit) {
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
