package com.tol.tenderwork.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @Column(name = "creator")
    private String creator;
    
    @Column(name = "work_days")
    private Integer workDays;
    
    @Column(name = "duration")
    private Integer duration;
    
    @Column(name = "cost", precision=10, scale=2)
    private BigDecimal cost;
    
    @Column(name = "multi_spec", precision=10, scale=2)
    private BigDecimal multiSpec;
    
    @Column(name = "multi_imp", precision=10, scale=2)
    private BigDecimal multiImp;
    
    @Column(name = "multi_test", precision=10, scale=2)
    private BigDecimal multiTest;
    
    @Column(name = "multi_syn", precision=10, scale=2)
    private BigDecimal multiSyn;
    
    @Column(name = "overall_cost", precision=10, scale=2)
    private BigDecimal overallCost;
    
    @Column(name = "overall_duration", precision=10, scale=2)
    private BigDecimal overallDuration;
    
    @Column(name = "overall_resources", precision=10, scale=2)
    private BigDecimal overallResources;
    
    @Column(name = "overall_gain", precision=10, scale=2)
    private BigDecimal overallGain;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getWorkDays() {
        return workDays;
    }
    
    public void setWorkDays(Integer workDays) {
        this.workDays = workDays;
    }

    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getCost() {
        return cost;
    }
    
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getMultiSpec() {
        return multiSpec;
    }
    
    public void setMultiSpec(BigDecimal multiSpec) {
        this.multiSpec = multiSpec;
    }

    public BigDecimal getMultiImp() {
        return multiImp;
    }
    
    public void setMultiImp(BigDecimal multiImp) {
        this.multiImp = multiImp;
    }

    public BigDecimal getMultiTest() {
        return multiTest;
    }
    
    public void setMultiTest(BigDecimal multiTest) {
        this.multiTest = multiTest;
    }

    public BigDecimal getMultiSyn() {
        return multiSyn;
    }
    
    public void setMultiSyn(BigDecimal multiSyn) {
        this.multiSyn = multiSyn;
    }

    public BigDecimal getOverallCost() {
        return overallCost;
    }
    
    public void setOverallCost(BigDecimal overallCost) {
        this.overallCost = overallCost;
    }

    public BigDecimal getOverallDuration() {
        return overallDuration;
    }
    
    public void setOverallDuration(BigDecimal overallDuration) {
        this.overallDuration = overallDuration;
    }

    public BigDecimal getOverallResources() {
        return overallResources;
    }
    
    public void setOverallResources(BigDecimal overallResources) {
        this.overallResources = overallResources;
    }

    public BigDecimal getOverallGain() {
        return overallGain;
    }
    
    public void setOverallGain(BigDecimal overallGain) {
        this.overallGain = overallGain;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
            ", creator='" + creator + "'" +
            ", workDays='" + workDays + "'" +
            ", duration='" + duration + "'" +
            ", cost='" + cost + "'" +
            ", multiSpec='" + multiSpec + "'" +
            ", multiImp='" + multiImp + "'" +
            ", multiTest='" + multiTest + "'" +
            ", multiSyn='" + multiSyn + "'" +
            ", overallCost='" + overallCost + "'" +
            ", overallDuration='" + overallDuration + "'" +
            ", overallResources='" + overallResources + "'" +
            ", overallGain='" + overallGain + "'" +
            '}';
    }
}
