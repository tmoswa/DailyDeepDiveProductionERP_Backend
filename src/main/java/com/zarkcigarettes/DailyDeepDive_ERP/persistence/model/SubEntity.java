package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class SubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String full_address;
    private String country;
    private String contact_name;
    private String contact_number;
    private String contact_email;
    private String warehouse;
    private String description;
    private boolean active_status;

    @ManyToOne(optional = true)
    @JoinColumn(name="currency_id", nullable=true)
    private Currency sub_entity_currency;

    @ManyToMany(mappedBy = "sub_entities",cascade = {CascadeType.MERGE})
    @JsonIgnore
    private Collection<MainEntity> mainEntities;


    @ManyToMany
    @JoinTable(
            name = "sub_entity_types",
            joinColumns = @JoinColumn(
                    name = "sub_entity_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "entity_type_id", referencedColumnName = "id"))
    private Collection<EntityType> subEntityTypes;

    public SubEntity() {
        super();
    }

    public SubEntity(String name, String full_address, String country, String contact_name, String contact_number, String contact_email, String warehouse, String description, boolean active_status, Currency sub_entity_currency, Collection<MainEntity> mainEntities, Collection<EntityType> subEntityTypes) {
        this.name = name;
        this.full_address = full_address;
        this.country = country;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.contact_email = contact_email;
        this.warehouse = warehouse;
        this.description = description;
        this.active_status = active_status;
        this.sub_entity_currency = sub_entity_currency;
        this.mainEntities = mainEntities;
        this.subEntityTypes = subEntityTypes;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }


    public Currency getSub_entity_currency() {
        return sub_entity_currency;
    }

    public void setSub_entity_currency(Currency sub_entity_currency) {
        this.sub_entity_currency = sub_entity_currency;
    }

    public Collection<MainEntity> getMainEntities() {
        return mainEntities;
    }

    public void setMainEntities(Collection<MainEntity> mainEntities) {
        this.mainEntities = mainEntities;
    }

    public Collection<EntityType> getSubEntityTypes() {
        return subEntityTypes;
    }

    public void setSubEntityTypes(Collection<EntityType> subEntityTypes) {
        this.subEntityTypes = subEntityTypes;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getActive_status() {
        return active_status;
    }

    public void setActive_status(boolean active_status) {
        this.active_status = active_status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubEntity other = (SubEntity) obj;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{\"name\":\"").append(name).append("\"],").append("[\"id\":\"").append(id).append("\"}");
        return builder.toString();
    }
}
