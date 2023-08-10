package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Entity
public class ProductionRun {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="product_id", nullable=true)
    private Product product_production_run;

    @OneToMany(mappedBy = "productionRun")
    @JsonIgnore
    private Collection<ProductionMaterialUsage> productionMaterialUsages;
    private String description;

    @Column(columnDefinition="TEXT")
    private String summary_comments;

    private double quantity;
    @Nullable
    private LocalDate from_date;
    @Nullable
    private LocalDate to_date;
    private String status;

    private String name;
    private String url;


    public ProductionRun() {
        super();
    }

    public ProductionRun(Product product_production_run, String description, double quantity, @Nullable LocalDate from_date, @Nullable LocalDate to_date, String status,String summary_comments,String name,String url) {
        this.product_production_run = product_production_run;
        this.description = description;
        this.quantity = quantity;
        this.from_date = from_date;
        this.to_date = to_date;
        this.status = status;
        this.summary_comments=summary_comments;
        this.name=name;
        this.url=url;
    }

//


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct_production_run() {
        return product_production_run;
    }

    public void setProduct_production_run(Product product_production_run) {
        this.product_production_run = product_production_run;
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

    @Nullable
    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(@Nullable LocalDate from_date) {
        this.from_date = from_date;
    }

    @Nullable
    public LocalDate getTo_date() {
        return to_date;
    }

    public void setTo_date(@Nullable LocalDate to_date) {
        this.to_date = to_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary_comments() {
        return summary_comments;
    }

    public void setSummary_comments(String summary_comments) {
        this.summary_comments = summary_comments;
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

    public Collection<ProductionMaterialUsage> getProductionMaterialUsages() {
        return productionMaterialUsages;
    }

    public void setProductionMaterialUsages(Collection<ProductionMaterialUsage> productionMaterialUsages) {
        this.productionMaterialUsages = productionMaterialUsages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionRun that = (ProductionRun) o;
        return Double.compare(that.quantity, quantity) == 0 && Objects.equals(id, that.id) && Objects.equals(product_production_run, that.product_production_run) && Objects.equals(description, that.description) && Objects.equals(from_date, that.from_date) && Objects.equals(to_date, that.to_date) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_production_run, description, quantity, from_date, to_date, status, summary_comments);
    }

    @Override
    public String toString() {
        return "ProductionRun{" +
                "id=" + id +
                ", product_production_run=" + product_production_run +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", from_date=" + from_date +
                ", to_date=" + to_date +
                ", summary_comments=" + summary_comments +
                ", status='" + status + '\'' +
                '}';
    }
}