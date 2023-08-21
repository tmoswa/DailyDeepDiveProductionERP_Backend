package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MaterialCount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="ntms_id", nullable=true)
    private NTMs ntMs_usage;

    @ManyToOne(optional = true)
    @JoinColumn(name="material_stock_count_id", nullable=true)
    private MaterialStockCount materialStockCount;


    @ManyToOne(optional = true)
    @JoinColumn(name="product_id", nullable=true)
    private Product product_usage;
    private String description;
    private double quantity;


    public MaterialCount() {
        super();
    }

    public MaterialCount(NTMs ntMs_usage, MaterialStockCount materialStockCount, Product product_usage, String description, double quantity) {
        this.ntMs_usage = ntMs_usage;
        this.materialStockCount = materialStockCount;
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

    public NTMs getNtMs_usage() {
        return ntMs_usage;
    }

    public void setNtMs_usage(NTMs ntMs_usage) {
        this.ntMs_usage = ntMs_usage;
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

    public MaterialStockCount getMaterialStockCount() {
        return materialStockCount;
    }

    public void setMaterialStockCount(MaterialStockCount materialStockCount) {
        this.materialStockCount = materialStockCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialCount that = (MaterialCount) o;
        return Double.compare(that.quantity, quantity) == 0 && Objects.equals(id, that.id) && Objects.equals(ntMs_usage, that.ntMs_usage) && Objects.equals(product_usage, that.product_usage) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ntMs_usage, product_usage, description, quantity);
    }

    @Override
    public String toString() {
        return "MaterialCount{" +
                "id=" + id +
                ", ntMs_usage=" + ntMs_usage +
                ", product_usage=" + product_usage +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}