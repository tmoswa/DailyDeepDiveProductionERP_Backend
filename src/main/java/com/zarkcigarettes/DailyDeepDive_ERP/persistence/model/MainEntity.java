package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class MainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="currency_id", nullable=true)
    private Currency main_entity_currency;
    @ManyToMany
    @JoinTable(
            name = "main_entity_sub_entity",
            joinColumns = @JoinColumn(
                    name = "entity_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "sub_entity_id", referencedColumnName = "id"))
    private Collection<SubEntity> sub_entities;

    @OneToMany(mappedBy = "main_entity_po")
    @JsonIgnore
    private Collection<PurchaseOrder> purchase_orders;

    @OneToMany(mappedBy = "main_entity_product")
    @JsonIgnore
    private Collection<Product> products;

    @OneToMany(mappedBy = "main_entity_material")
    @JsonIgnore
    private Collection<NTMs> ntMs;


    @ManyToMany
    @JoinTable(
            name = "main_entity_types",
            joinColumns = @JoinColumn(
                    name = "main_entity_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "entity_type_id", referencedColumnName = "id"))
    private Collection<EntityType> main_entity_type;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private Collection<PurchaseOrder> order_supplier;

    private String legal_name;
    private String full_address;
    private String country;
    private String contact_name;
    private String contact_number;
    private String contact_email;
    private String warehouse;
    private String description;
    private boolean active_status;


    public MainEntity() {
        super();
    }

    public MainEntity(final String legal_name) {
        super();
        this.legal_name = legal_name;
    }

    public MainEntity(Currency main_entity_currency, Collection<SubEntity> sub_entities, Collection<PurchaseOrder> purchase_orders, Collection<EntityType> main_entity_type, Collection<PurchaseOrder> order_supplier, String legal_name, String full_address, String country, String contact_name, String contact_number, String contact_email, String warehouse, String description, boolean active_status) {
        this.main_entity_currency = main_entity_currency;
        this.sub_entities = sub_entities;
        this.purchase_orders = purchase_orders;
        this.main_entity_type = main_entity_type;
        this.order_supplier = order_supplier;
        this.legal_name = legal_name;
        this.full_address = full_address;
        this.country = country;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.contact_email = contact_email;
        this.warehouse = warehouse;
        this.description = description;
        this.active_status = active_status;
    }

//

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Currency getMain_entity_currency() {
        return main_entity_currency;
    }

    public void setMain_entity_currency(Currency main_entity_currency) {
        this.main_entity_currency = main_entity_currency;
    }

    public Collection<SubEntity> getSub_entities() {
        return sub_entities;
    }

    public void setSub_entities(Collection<SubEntity> sub_entities) {
        this.sub_entities = sub_entities;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public boolean isActive_status() {
        return active_status;
    }

    public Collection<EntityType> getMain_entity_type() {
        return main_entity_type;
    }

    public void setMain_entity_type(Collection<EntityType> main_entity_type) {
        this.main_entity_type = main_entity_type;
    }

    public String getLegal_name() {
        return legal_name;
    }

    public void setLegal_name(String legal_name) {
        this.legal_name = legal_name;
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

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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

    public Collection<PurchaseOrder> getPurchase_orders() {
        return purchase_orders;
    }

    public void setPurchase_orders(Collection<PurchaseOrder> purchase_orders) {
        this.purchase_orders = purchase_orders;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public Collection<PurchaseOrder> getOrder_supplier() {
        return order_supplier;
    }

    public void setOrder_supplier(Collection<PurchaseOrder> order_supplier) {
        this.order_supplier = order_supplier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLegal_name() == null) ? 0 : getLegal_name().hashCode());
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
        final MainEntity mainEntity = (MainEntity) obj;
        if (!getLegal_name().equals(mainEntity.getLegal_name())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Main Entity [name=").append(legal_name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}