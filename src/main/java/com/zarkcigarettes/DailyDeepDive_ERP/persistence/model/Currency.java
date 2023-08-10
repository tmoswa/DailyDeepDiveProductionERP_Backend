package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy="main_entity_currency")
    @JsonIgnore
    private Collection<MainEntity> mainEntities;

    @OneToMany(mappedBy="invoice_currency")
    @JsonIgnore
    private Collection<Invoice> invoices;
    @OneToMany(mappedBy="sub_entity_currency")
    @JsonIgnore
    private Collection<SubEntity> subEntities;



    private String name;
    private String symbol;

    public Currency() {
        super();
    }

    public Currency(final String name, String symbol) {
        super();
        this.name = name;
        this.symbol=symbol;
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

    public Collection<MainEntity> getMainEntities() {
        return mainEntities;
    }

    public void setMainEntities(Collection<MainEntity> mainEntities) {
        this.mainEntities = mainEntities;
    }

    public Collection<SubEntity> getSubEntities() {
        return subEntities;
    }

    public void setSubEntities(Collection<SubEntity> subEntities) {
        this.subEntities = subEntities;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        this.invoices = invoices;
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
        final Currency subsidiary = (Currency) obj;
        if (!getName().equals(subsidiary.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Currency [name=").append(name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}
