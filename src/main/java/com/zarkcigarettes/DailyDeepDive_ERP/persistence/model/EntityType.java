package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class EntityType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "main_entity_type",cascade = CascadeType.MERGE)
    @JsonIgnore
    private Collection<MainEntity> main_entities;

    @OneToMany(mappedBy = "subEntityTypes",cascade = CascadeType.MERGE)
    @JsonIgnore
    private Collection<SubEntity> sub_entities;


    private String name;

    public EntityType() {
        super();
    }
    public EntityType(final String name) {
        super();
        this.name = name;
    }

    public EntityType(Collection<MainEntity> main_entities, Collection<SubEntity> sub_entities, String name) {
        this.main_entities = main_entities;
        this.sub_entities = sub_entities;
        this.name = name;
    }
    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Main Module [name=").append(name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}
