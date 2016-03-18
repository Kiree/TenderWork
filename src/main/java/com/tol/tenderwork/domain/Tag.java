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
    @Column(name = "name", length = 30, nullable = false)
    private String name;
    
    @NotNull
    @Min(value = 0)
    @Column(name = "counter", nullable = false)
    private Integer counter;
    
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tag_belongs_to_projects",
               joinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="belongs_to_projectss_id", referencedColumnName="ID"))
    private Set<Project> belongsToProjectss = new HashSet<>();

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

    public Integer getCounter() {
        return counter;
    }
    
    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Set<Project> getBelongsToProjectss() {
        return belongsToProjectss;
    }

    public void setBelongsToProjectss(Set<Project> projects) {
        this.belongsToProjectss = projects;
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
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", counter='" + counter + "'" +
            '}';
    }
}
