package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class Increaments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Value("1")
    private Long id;
@Value("0")
    private int purchase_order_inc;
    @Value("0")
    private int invoice_inc;
    @Value("0")
    private int production_inc;

    public Increaments() {
        super();
    }

    public Increaments(final String name) {
        super();
    }

    public Increaments(int purchase_order_inc) {
        this.purchase_order_inc = purchase_order_inc;
    }

    public Increaments(int purchase_order_inc, int invoice_inc, int production_inc) {
        this.purchase_order_inc = purchase_order_inc;
        this.invoice_inc = invoice_inc;
        this.production_inc = production_inc;
    }
    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }


    public int getPurchase_order_inc() {
        return purchase_order_inc;
    }

    public void setPurchase_order_inc(int purchase_order_inc) {
        this.purchase_order_inc = purchase_order_inc;
    }

    public int getInvoice_inc() {
        return invoice_inc;
    }

    public void setInvoice_inc(int invoice_inc) {
        this.invoice_inc = invoice_inc;
    }

    public int getProduction_inc() {
        return production_inc;
    }

    public void setProduction_inc(int production_inc) {
        this.production_inc = production_inc;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "IncreamentsData{" +
                "id=" + id +
                ", purchase_order_inc=" + purchase_order_inc +
                ", invoice_inc=" + invoice_inc +
                ", production_inc=" + production_inc +
                '}';
    }


}
