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

    @ManyToMany(mappedBy = "tags", targetEntity = Project.class)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projectTags = new HashSet<>();

    @ManyToMany(mappedBy = "tags", targetEntity = Requirement.class)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requirement> requirementTags = new HashSet<>();

    @ManyToMany(mappedBy = "tags", targetEntity = Task.class)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> taskTags = new HashSet<>();

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


    public void addProject(Project project) { this.getProjectTags().add(project); }

    public void removeProject(Project project) { this.getProjectTags().remove(project); }

    public void addRequirement(Requirement requirement) {
        this.requirementTags.add(requirement);
    }

    public void removeRequirement(Requirement requirement) {this.requirementTags.remove(requirement); }

    public void addTask(Task task) {this.taskTags.add(task); }

    public void removeTask(Task task) {this.taskTags.remove(task); }

    public Set<Project> getProjectTags() {
        return projectTags;
    }

    public void setProjectTags(Set<Project> projectTags) {
        this.projectTags = projectTags;
    }

    public Set<Requirement> getRequirementTags() {
        return requirementTags;
    }

    public void setRequirementTags(Set<Requirement> requirementTags) {
        this.requirementTags = requirementTags;
    }

    public Set<Task> getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(Set<Task> taskTags) {
        this.taskTags = taskTags;
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
