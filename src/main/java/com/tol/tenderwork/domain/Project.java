package com.tol.tenderwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "project")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "client")
    private String client;
    
    @Column(name = "deadline")
    private LocalDate deadline;
    
    @Column(name = "last_editor")
    private String lastEditor;
    
    @Column(name = "creator")
    private String creator;
    
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    
    @Column(name = "edited_date")
    private ZonedDateTime editedDate;
    
    @Column(name = "doc_location")
    private String docLocation;
    
    @Column(name = "state")
    private String state;
    
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Estimate> estimates = new HashSet<>();

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

    public String getClient() {
        return client;
    }
    
    public void setClient(String client) {
        this.client = client;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getLastEditor() {
        return lastEditor;
    }
    
    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getEditedDate() {
        return editedDate;
    }
    
    public void setEditedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
    }

    public String getDocLocation() {
        return docLocation;
    }
    
    public void setDocLocation(String docLocation) {
        this.docLocation = docLocation;
    }

    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    public Set<Estimate> getEstimates() {
        return estimates;
    }

    public void setEstimates(Set<Estimate> estimates) {
        this.estimates = estimates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if(project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", client='" + client + "'" +
            ", deadline='" + deadline + "'" +
            ", lastEditor='" + lastEditor + "'" +
            ", creator='" + creator + "'" +
            ", createdDate='" + createdDate + "'" +
            ", editedDate='" + editedDate + "'" +
            ", docLocation='" + docLocation + "'" +
            ", state='" + state + "'" +
            '}';
    }
}
