package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String code;
    private String size;
    private double quantity;
    private String description;
    private String unit_of_measure;

    @ManyToOne(optional = true)
    @JoinColumn(name="main_entity_id", nullable=true)
    private MainEntity main_entity_product;

    @OneToMany(mappedBy = "product_usage")
    @JsonIgnore
    private Collection<MaterialUsage> materialUsages;

    public Product(String name, String code, String size, String description, double quantity, String unit_of_measure) {
        this.name = name;
        this.code = code;
        this.size = size;
        this.description = description;
        this.quantity=quantity;
        this.unit_of_measure=unit_of_measure;
    }

    public Product() {
        super();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit_of_measure() {
        return unit_of_measure;
    }

    public void setUnit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public MainEntity getMain_entity_product() {
        return main_entity_product;
    }

    public void setMain_entity_product(MainEntity main_entity_product) {
        this.main_entity_product = main_entity_product;
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
        final Product subsidiary = (Product) obj;
        if (!getName().equals(subsidiary.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Produ [name=").append(name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}
