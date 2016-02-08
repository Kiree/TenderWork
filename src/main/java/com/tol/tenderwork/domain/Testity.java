package com.tol.tenderwork.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Testity.
 */
@Entity
@Table(name = "testity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "testity")
public class Testity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 0, max = 20)
    @Column(name = "nimi", length = 20, nullable = false)
    private String nimi;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }
    
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Testity testity = (Testity) o;
        if(testity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, testity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Testity{" +
            "id=" + id +
            ", nimi='" + nimi + "'" +
            '}';
    }
}
