package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class NTMs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String code;
    private String size;
    private double quantity;
    private int lead_time;
    private String description;
    private String unit_of_measure;

    @OneToMany(mappedBy = "ntMs_usage")
    @JsonIgnore
    private Collection<MaterialUsage> materialUsages;

    @OneToMany(mappedBy = "ntMs")
    @JsonIgnore
    private Collection<PurchaseOrder> purchaseOrders;

    @OneToMany(mappedBy = "main_entity_po")
    @JsonIgnore
    private Collection<PurchaseOrder> purchase_orders;

    @ManyToOne(optional = true)
    @JoinColumn(name="main_entity_id", nullable=true)
    private MainEntity main_entity_material;


    public NTMs(String name, String code, String size, String description,double quantity,String unit_of_measure,int lead_time) {
        this.name = name;
        this.code = code;
        this.size = size;
        this.description = description;
        this.quantity=quantity;
        this.unit_of_measure=unit_of_measure;
        this.lead_time=lead_time;
    }

    public NTMs() {
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

    public Collection<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Collection<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Collection<PurchaseOrder> getPurchase_orders() {
        return purchase_orders;
    }

    public void setPurchase_orders(Collection<PurchaseOrder> purchase_orders) {
        this.purchase_orders = purchase_orders;
    }

    public MainEntity getMain_entity_material() {
        return main_entity_material;
    }

    public void setMain_entity_material(MainEntity main_entity_material) {
        this.main_entity_material = main_entity_material;
    }


    public Collection<MaterialUsage> getMaterialUsages() {
        return materialUsages;
    }

    public void setMaterialUsages(Collection<MaterialUsage> materialUsages) {
        this.materialUsages = materialUsages;
    }

    public int getLead_time() {
        return lead_time;
    }

    public void setLead_time(int lead_time) {
        this.lead_time = lead_time;
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
        final NTMs subsidiary = (NTMs) obj;
        if (!getName().equals(subsidiary.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("NTMs [name=").append(name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}
