package com.tol.tenderwork.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotNull
    @Size(max = 60)
    @Column(name = "name", length = 60, nullable = false)
    private String name;
    
    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;
    
    @Size(max = 60)
    @Column(name = "client", length = 60)
    private String client;
    
    @Column(name = "deadline")
    private LocalDate deadline;
    
    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;
    
    @NotNull
    @Column(name = "edited_date", nullable = false)
    private ZonedDateTime editedDate;
    
    @Size(max = 250)
    @Column(name = "doc_location", length = 250)
    private String docLocation;
    
    @NotNull
    @Column(name = "state", nullable = false)
    private String state;
    
    @Size(max = 1000)
    @Column(name = "state_description", length = 1000)
    private String stateDescription;
    
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "edited_by_id")
    private User editedBy;

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

    public String getStateDescription() {
        return stateDescription;
    }
    
    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(User user) {
        this.editedBy = user;
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
            ", createdDate='" + createdDate + "'" +
            ", editedDate='" + editedDate + "'" +
            ", docLocation='" + docLocation + "'" +
            ", state='" + state + "'" +
            ", stateDescription='" + stateDescription + "'" +
            '}';
    }
}
