package com.softpala.SalesDeepDive_ERP.persistence.model;

import javax.persistence.*;

@Entity
@Table(name = "waste_thresholds")
public class WasteThreshold {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "warn_pct", nullable = false)
    private double warnPct;

    @Column(name = "critical_pct", nullable = false)
    private double criticalPct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getWarnPct() {
        return warnPct;
    }

    public void setWarnPct(double warnPct) {
        this.warnPct = warnPct;
    }

    public double getCriticalPct() {
        return criticalPct;
    }

    public void setCriticalPct(double criticalPct) {
        this.criticalPct = criticalPct;
    }
}

