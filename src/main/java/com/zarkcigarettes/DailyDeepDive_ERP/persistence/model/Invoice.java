package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="currency_id", nullable=true)
    private Currency invoice_currency;

    @ManyToOne(optional = true)
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id",nullable=true)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(optional = true)
    @JoinColumn(name="main_entity_id", nullable=true)
    private MainEntity main_entity_inv;

    private String description;
    private String manual_invoice_number;
    private String status;
    double amount;
    private String name;
    private String url;

    private int invoice_number;

    public Invoice() {
        super();
    }

    public Invoice(PurchaseOrder purchaseOrder, String description, String manual_invoice_number, String status, String name, String url, int invoice_number,double amount,MainEntity main_entity_inv) {
        this.purchaseOrder = purchaseOrder;
        this.description = description;
        this.manual_invoice_number = manual_invoice_number;
        this.status = status;
        this.name = name;
        this.url = url;
        this.invoice_number = invoice_number;
        this.amount=amount;
        this.main_entity_inv=main_entity_inv;
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

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getManual_invoice_number() {
        return manual_invoice_number;
    }

    public void setManual_invoice_number(String manual_invoice_number) {
        this.manual_invoice_number = manual_invoice_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(int invoice_number) {
        this.invoice_number = invoice_number;
    }

    public MainEntity getMain_entity_inv() {
        return main_entity_inv;
    }

    public void setMain_entity_inv(MainEntity main_entity_inv) {
        this.main_entity_inv = main_entity_inv;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getInvoice_currency() {
        return invoice_currency;
    }

    public void setInvoice_currency(Currency invoice_currency) {
        this.invoice_currency = invoice_currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Double.compare(invoice.amount, amount) == 0 && invoice_number == invoice.invoice_number && Objects.equals(id, invoice.id) && Objects.equals(purchaseOrder, invoice.purchaseOrder) && Objects.equals(description, invoice.description) && Objects.equals(manual_invoice_number, invoice.manual_invoice_number) && Objects.equals(status, invoice.status) && Objects.equals(name, invoice.name) && Objects.equals(url, invoice.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, purchaseOrder, description, manual_invoice_number, status, amount, name, url, invoice_number);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", purchaseOrder=" + purchaseOrder +
                ", description='" + description + '\'' +
                ", manual_invoice_number='" + manual_invoice_number + '\'' +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", invoice_number=" + invoice_number +
                ", amount=" + amount +
                '}';
    }
}