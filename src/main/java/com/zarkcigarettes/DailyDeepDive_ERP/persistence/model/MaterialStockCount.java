package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Entity
public class MaterialStockCount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="product_id", nullable=true)
    private Product product_production_run;

    @OneToMany(mappedBy = "materialStockCount")
    @JsonIgnore
    private Collection<MaterialCount> materialCounts;

    @Column(columnDefinition="TEXT")
    private String summary_comments;
    @Nullable
    private LocalDate count_date;



    public MaterialStockCount() {
        super();
    }

    public MaterialStockCount(Product product_production_run, Collection<MaterialCount> materialCounts, String summary_comments, @Nullable LocalDate count_date) {
        this.product_production_run = product_production_run;
        this.materialCounts = materialCounts;
        this.summary_comments = summary_comments;
        this.count_date = count_date;
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

    public Collection<MaterialCount> getMaterialCounts() {
        return materialCounts;
    }

    public void setMaterialCounts(Collection<MaterialCount> materialCounts) {
        this.materialCounts = materialCounts;
    }

    public String getSummary_comments() {
        return summary_comments;
    }

    public void setSummary_comments(String summary_comments) {
        this.summary_comments = summary_comments;
    }

    @Nullable
    public LocalDate getCount_date() {
        return count_date;
    }

    public void setCount_date(@Nullable LocalDate count_date) {
        this.count_date = count_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialStockCount that = (MaterialStockCount) o;
        return Objects.equals(id, that.id) && Objects.equals(product_production_run, that.product_production_run) && Objects.equals(materialCounts, that.materialCounts) && Objects.equals(summary_comments, that.summary_comments) && Objects.equals(count_date, that.count_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_production_run, materialCounts, summary_comments, count_date);
    }

    @Override
    public String toString() {
        return "MaterialStockCount{" +
                "id=" + id +
                ", product_production_run=" + product_production_run +
                ", materialCounts=" + materialCounts +
                ", summary_comments='" + summary_comments + '\'' +
                ", count_date=" + count_date +
                '}';
    }
}