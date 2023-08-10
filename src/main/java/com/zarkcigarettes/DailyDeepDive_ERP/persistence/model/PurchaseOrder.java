package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="ntms_id", nullable=true)
    private NTMs ntMs;

    @ManyToOne(optional = true)
    @JoinColumn(name="main_entity_id", nullable=true)
    private MainEntity main_entity_po;

    @ManyToOne(optional = true)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id",nullable=true)
    private MainEntity supplier;

    @OneToMany(mappedBy = "purchaseOrder")
    @JsonIgnore
    private Collection<Invoice> invoices;

    private String description;
    private double quantity;
    private String status;

    private String name;
    private String url;

    private int order_number;

    @Nullable
    private LocalDate order_date;
    @Nullable
    private LocalDate delivery_date;
    private double delivered_quantity;

    public PurchaseOrder() {
        super();
    }

    public PurchaseOrder(NTMs ntMs, MainEntity main_entity_po, MainEntity supplier, String description, double quantity, String status, String name, String url,int order_number) {
        this.ntMs = ntMs;
        this.main_entity_po = main_entity_po;
        this.supplier = supplier;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.name = name;
        this.url = url;
        this.order_number=order_number;
    }

    public PurchaseOrder(NTMs ntMs, MainEntity main_entity_po, MainEntity supplier, Collection<Invoice> invoices, String description, double quantity, String status, String name, String url, int order_number, LocalDate order_date, LocalDate delivery_date, double delivered_quantity) {
        this.ntMs = ntMs;
        this.main_entity_po = main_entity_po;
        this.supplier = supplier;
        this.invoices = invoices;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.name = name;
        this.url = url;
        this.order_number = order_number;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
        this.delivered_quantity = delivered_quantity;
    }

//

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NTMs getNtMs() {
        return ntMs;
    }

    public void setNtMs(NTMs ntMs) {
        this.ntMs = ntMs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MainEntity getMain_entity_po() {
        return main_entity_po;
    }

    public void setMain_entity_po(MainEntity main_entity_po) {
        this.main_entity_po = main_entity_po;
    }

    public MainEntity getSupplier() {
        return supplier;
    }

    public void setSupplier(MainEntity supplier) {
        this.supplier = supplier;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        this.invoices = invoices;
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }

    public LocalDate getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(LocalDate delivery_date) {
        this.delivery_date = delivery_date;
    }

    public double getDelivered_quantity() {
        return delivered_quantity;
    }

    public void setDelivered_quantity(double delivered_quantity) {
        this.delivered_quantity = delivered_quantity;
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
        final PurchaseOrder role = (PurchaseOrder) obj;
        if (!getName().equals(role.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "id=" + id +
                ", ntMs=" + ntMs +
                ", main_entity_po=" + main_entity_po +
                ", supplier=" + supplier +
                ", invoices=" + invoices +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", order_number=" + order_number +
                ", order_date=" + order_date +
                ", delivery_date=" + delivery_date +
                ", delivered_quantity=" + delivered_quantity +
                '}';
    }
}