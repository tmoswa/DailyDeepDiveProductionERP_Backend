package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class FinishedProductMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="product_id", nullable=true)
    private Product product_usage;
    private String description;
    private String manual_reference;
    private double quantity;


    public FinishedProductMovement() {
        super();
    }

    public FinishedProductMovement(String manual_reference, Product product_usage, String description, double quantity) {
        this.manual_reference = manual_reference;
        this.product_usage = product_usage;
        this.description = description;
        this.quantity = quantity;
    }

//


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManual_reference() {
        return manual_reference;
    }

    public void setManual_reference(String manual_reference) {
        this.manual_reference = manual_reference;
    }

    public Product getProduct_usage() {
        return product_usage;
    }

    public void setProduct_usage(Product product_usage) {
        this.product_usage = product_usage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinishedProductMovement that = (FinishedProductMovement) o;
        return Double.compare(that.quantity, quantity) == 0 && Objects.equals(id, that.id) && Objects.equals(manual_reference, that.manual_reference) && Objects.equals(product_usage, that.product_usage) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manual_reference, product_usage, description, quantity);
    }

    @Override
    public String toString() {
        return "MaterialUsage{" +
                "id=" + id +
                ", manual_reference=" + manual_reference +
                ", product_usage=" + product_usage +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}