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
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "task")
public class Task implements Serializable {

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
    
    @NotNull
    @Min(value = 0)
    @Column(name = "estimate_specification", nullable = false)
    private Integer estimateSpecification;
    
    @NotNull
    @Min(value = 0)
    @Column(name = "estimate_implementation", nullable = false)
    private Integer estimateImplementation;
    
    @NotNull
    @Min(value = 0)
    @Column(name = "estimate_testing", nullable = false)
    private Integer estimateTesting;
    
    @Min(value = 0)
    @Column(name = "estimate_synergy")
    private Integer estimateSynergy;
    
    @NotNull
    @Column(name = "synergy_check", nullable = false)
    private Boolean synergyCheck;
    
    @Min(value = 0)
    @Column(name = "synergy_total")
    private Integer synergyTotal;
    
    @Min(value = 0)
    @Column(name = "estimate_total")
    private Integer estimateTotal;
    
    @ManyToOne
    @JoinColumn(name = "owned_by_id")
    private User ownedBy;

    @ManyToOne
    @JoinColumn(name = "specification_factor_id")
    private Estimate specificationFactor;

    @ManyToOne
    @JoinColumn(name = "implementation_factor_id")
    private Estimate implementationFactor;

    @ManyToOne
    @JoinColumn(name = "testing_factor_id")
    private Estimate testingFactor;

    @ManyToOne
    @JoinColumn(name = "synergy_benefit_id")
    private Estimate synergyBenefit;

    @ManyToOne
    @JoinColumn(name = "owner_requirement_id")
    private Requirement ownerRequirement;

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

    public Integer getEstimateSpecification() {
        return estimateSpecification;
    }
    
    public void setEstimateSpecification(Integer estimateSpecification) {
        this.estimateSpecification = estimateSpecification;
    }

    public Integer getEstimateImplementation() {
        return estimateImplementation;
    }
    
    public void setEstimateImplementation(Integer estimateImplementation) {
        this.estimateImplementation = estimateImplementation;
    }

    public Integer getEstimateTesting() {
        return estimateTesting;
    }
    
    public void setEstimateTesting(Integer estimateTesting) {
        this.estimateTesting = estimateTesting;
    }

    public Integer getEstimateSynergy() {
        return estimateSynergy;
    }
    
    public void setEstimateSynergy(Integer estimateSynergy) {
        this.estimateSynergy = estimateSynergy;
    }

    public Boolean getSynergyCheck() {
        return synergyCheck;
    }
    
    public void setSynergyCheck(Boolean synergyCheck) {
        this.synergyCheck = synergyCheck;
    }

    public Integer getSynergyTotal() {
        return synergyTotal;
    }
    
    public void setSynergyTotal(Integer synergyTotal) {
        this.synergyTotal = synergyTotal;
    }

    public Integer getEstimateTotal() {
        return estimateTotal;
    }
    
    public void setEstimateTotal(Integer estimateTotal) {
        this.estimateTotal = estimateTotal;
    }

    public User getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(User user) {
        this.ownedBy = user;
    }

    public Estimate getSpecificationFactor() {
        return specificationFactor;
    }

    public void setSpecificationFactor(Estimate estimate) {
        this.specificationFactor = estimate;
    }

    public Estimate getImplementationFactor() {
        return implementationFactor;
    }

    public void setImplementationFactor(Estimate estimate) {
        this.implementationFactor = estimate;
    }

    public Estimate getTestingFactor() {
        return testingFactor;
    }

    public void setTestingFactor(Estimate estimate) {
        this.testingFactor = estimate;
    }

    public Estimate getSynergyBenefit() {
        return synergyBenefit;
    }

    public void setSynergyBenefit(Estimate estimate) {
        this.synergyBenefit = estimate;
    }

    public Requirement getOwnerRequirement() {
        return ownerRequirement;
    }

    public void setOwnerRequirement(Requirement requirement) {
        this.ownerRequirement = requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        if(task.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", estimateSpecification='" + estimateSpecification + "'" +
            ", estimateImplementation='" + estimateImplementation + "'" +
            ", estimateTesting='" + estimateTesting + "'" +
            ", estimateSynergy='" + estimateSynergy + "'" +
            ", synergyCheck='" + synergyCheck + "'" +
            ", synergyTotal='" + synergyTotal + "'" +
            ", estimateTotal='" + estimateTotal + "'" +
            '}';
    }
}
