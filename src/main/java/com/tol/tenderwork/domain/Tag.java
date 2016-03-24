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
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "hasTagss")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> belongsToProjectss = new HashSet<>();

    @ManyToMany(mappedBy = "hasTagss")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requirement> belongsToRequirementss = new HashSet<>();

    @ManyToMany(mappedBy = "hasTagss")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> belongsToTaskss = new HashSet<>();

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

    public Set<Project> getBelongsToProjectss() {
        return belongsToProjectss;
    }

    public void setBelongsToProjectss(Set<Project> projects) {
        this.belongsToProjectss = projects;
    }

    public Set<Requirement> getBelongsToRequirementss() {
        return belongsToRequirementss;
    }

    public void setBelongsToRequirementss(Set<Requirement> requirements) {
        this.belongsToRequirementss = requirements;
    }

    public Set<Task> getBelongsToTaskss() {
        return belongsToTaskss;
    }

    public void setBelongsToTaskss(Set<Task> tasks) {
        this.belongsToTaskss = tasks;
    }

    public void addProject(Project project) {
        this.belongsToProjectss.add(project);
    }

    public void removeProject(Project project) {
        this.belongsToProjectss.remove(project);
    }

    public void addRequirement(Requirement requirement) {
        this.belongsToRequirementss.add(requirement);
    }

    public void removeRequirement(Requirement requirement) {
        this.belongsToRequirementss.remove(requirement);
    }

    public void addTask(Task task) {
        this.belongsToTaskss.add(task);
    }

    public void removeTask(Task task) {
        this.belongsToTaskss.remove(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        if(tag.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
