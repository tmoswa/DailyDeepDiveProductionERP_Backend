package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class DailyProductionRunKeyNote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name="production_run_id", nullable=true)
    private ProductionRun productionRun;


    @ManyToOne(optional = true)
    @JoinColumn(name="product_id", nullable=true)
    private Product product_usage;

    private String production_line_and_target;
    private String representatives_and_technician;
    @Column(columnDefinition="TEXT")
    private String comments;
    private String rag_used;
    private String moisture_content;
    private String average_cigarette_weight;
    private String loose_end_testing;
    private String diameter_testing;
    private String filter_size_stick_alignment;
    private String speed_of_maker_against_waste;
    private String tightness_of_bopp_for_pack;
    private String open_3_random_bricks;
    private String correct_placement_of_aluminium_foil;
    private String whiteness_of_white_inners;
    private String gluing_of_inner_top_flap;
    private String positioning_of_customer_comments;
    private String correct_lettering_and_positioning_on_the_pack;
    private String smoke_tests;
    private String tipping_used;
    private String blanks_used;
    private String filters_used;
    private double quantity_produced;


    public DailyProductionRunKeyNote() {
        super();
    }

    public DailyProductionRunKeyNote(ProductionRun productionRun, Product product_usage, String production_line_and_target, String representatives_and_technician, String comments, String rag_used, String moisture_content, String average_cigarette_weight, String loose_end_testing, String diameter_testing, String filter_size_stick_alignment, String speed_of_maker_against_waste, String tightness_of_bopp_for_pack, String open_3_random_bricks, String correct_placement_of_aluminium_foil, String whiteness_of_white_inners, String gluing_of_inner_top_flap, String positioning_of_customer_comments, String correct_lettering_and_positioning_on_the_pack, String smoke_tests, String tipping_used, String blanks_used, String filters_used, double quantity_produced) {
        this.productionRun = productionRun;
        this.product_usage = product_usage;
        this.production_line_and_target = production_line_and_target;
        this.representatives_and_technician = representatives_and_technician;
        this.comments = comments;
        this.rag_used = rag_used;
        this.moisture_content = moisture_content;
        this.average_cigarette_weight = average_cigarette_weight;
        this.loose_end_testing = loose_end_testing;
        this.diameter_testing = diameter_testing;
        this.filter_size_stick_alignment = filter_size_stick_alignment;
        this.speed_of_maker_against_waste = speed_of_maker_against_waste;
        this.tightness_of_bopp_for_pack = tightness_of_bopp_for_pack;
        this.open_3_random_bricks = open_3_random_bricks;
        this.correct_placement_of_aluminium_foil = correct_placement_of_aluminium_foil;
        this.whiteness_of_white_inners = whiteness_of_white_inners;
        this.gluing_of_inner_top_flap = gluing_of_inner_top_flap;
        this.positioning_of_customer_comments = positioning_of_customer_comments;
        this.correct_lettering_and_positioning_on_the_pack = correct_lettering_and_positioning_on_the_pack;
        this.smoke_tests = smoke_tests;
        this.tipping_used = tipping_used;
        this.blanks_used = blanks_used;
        this.filters_used = filters_used;
        this.quantity_produced = quantity_produced;
    }

//


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct_usage() {
        return product_usage;
    }

    public void setProduct_usage(Product product_usage) {
        this.product_usage = product_usage;
    }

    public ProductionRun getProductionRun() {
        return productionRun;
    }

    public void setProductionRun(ProductionRun productionRun) {
        this.productionRun = productionRun;
    }

    public String getProduction_line_and_target() {
        return production_line_and_target;
    }

    public void setProduction_line_and_target(String production_line_and_target) {
        this.production_line_and_target = production_line_and_target;
    }

    public String getRepresentatives_and_technician() {
        return representatives_and_technician;
    }

    public void setRepresentatives_and_technician(String representatives_and_technician) {
        this.representatives_and_technician = representatives_and_technician;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRag_used() {
        return rag_used;
    }

    public void setRag_used(String rag_used) {
        this.rag_used = rag_used;
    }

    public String getMoisture_content() {
        return moisture_content;
    }

    public void setMoisture_content(String moisture_content) {
        this.moisture_content = moisture_content;
    }

    public String getAverage_cigarette_weight() {
        return average_cigarette_weight;
    }

    public void setAverage_cigarette_weight(String average_cigarette_weight) {
        this.average_cigarette_weight = average_cigarette_weight;
    }

    public String getLoose_end_testing() {
        return loose_end_testing;
    }

    public void setLoose_end_testing(String loose_end_testing) {
        this.loose_end_testing = loose_end_testing;
    }

    public String getDiameter_testing() {
        return diameter_testing;
    }

    public void setDiameter_testing(String diameter_testing) {
        this.diameter_testing = diameter_testing;
    }

    public String getFilter_size_stick_alignment() {
        return filter_size_stick_alignment;
    }

    public void setFilter_size_stick_alignment(String filter_size_stick_alignment) {
        this.filter_size_stick_alignment = filter_size_stick_alignment;
    }

    public String getSpeed_of_maker_against_waste() {
        return speed_of_maker_against_waste;
    }

    public void setSpeed_of_maker_against_waste(String speed_of_maker_against_waste) {
        this.speed_of_maker_against_waste = speed_of_maker_against_waste;
    }

    public String getTightness_of_bopp_for_pack() {
        return tightness_of_bopp_for_pack;
    }

    public void setTightness_of_bopp_for_pack(String tightness_of_bopp_for_pack) {
        this.tightness_of_bopp_for_pack = tightness_of_bopp_for_pack;
    }

    public String getOpen_3_random_bricks() {
        return open_3_random_bricks;
    }

    public void setOpen_3_random_bricks(String open_3_random_bricks) {
        this.open_3_random_bricks = open_3_random_bricks;
    }

    public String getCorrect_placement_of_aluminium_foil() {
        return correct_placement_of_aluminium_foil;
    }

    public void setCorrect_placement_of_aluminium_foil(String correct_placement_of_aluminium_foil) {
        this.correct_placement_of_aluminium_foil = correct_placement_of_aluminium_foil;
    }

    public String getWhiteness_of_white_inners() {
        return whiteness_of_white_inners;
    }

    public void setWhiteness_of_white_inners(String whiteness_of_white_inners) {
        this.whiteness_of_white_inners = whiteness_of_white_inners;
    }

    public String getGluing_of_inner_top_flap() {
        return gluing_of_inner_top_flap;
    }

    public void setGluing_of_inner_top_flap(String gluing_of_inner_top_flap) {
        this.gluing_of_inner_top_flap = gluing_of_inner_top_flap;
    }

    public String getPositioning_of_customer_comments() {
        return positioning_of_customer_comments;
    }

    public void setPositioning_of_customer_comments(String positioning_of_customer_comments) {
        this.positioning_of_customer_comments = positioning_of_customer_comments;
    }

    public String getCorrect_lettering_and_positioning_on_the_pack() {
        return correct_lettering_and_positioning_on_the_pack;
    }

    public void setCorrect_lettering_and_positioning_on_the_pack(String correct_lettering_and_positioning_on_the_pack) {
        this.correct_lettering_and_positioning_on_the_pack = correct_lettering_and_positioning_on_the_pack;
    }

    public String getSmoke_tests() {
        return smoke_tests;
    }

    public void setSmoke_tests(String smoke_tests) {
        this.smoke_tests = smoke_tests;
    }

    public String getTipping_used() {
        return tipping_used;
    }

    public void setTipping_used(String tipping_used) {
        this.tipping_used = tipping_used;
    }

    public String getBlanks_used() {
        return blanks_used;
    }

    public void setBlanks_used(String blanks_used) {
        this.blanks_used = blanks_used;
    }

    public String getFilters_used() {
        return filters_used;
    }

    public void setFilters_used(String filters_used) {
        this.filters_used = filters_used;
    }

    public double getQuantity_produced() {
        return quantity_produced;
    }

    public void setQuantity_produced(double quantity_produced) {
        this.quantity_produced = quantity_produced;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyProductionRunKeyNote that = (DailyProductionRunKeyNote) o;
        return Double.compare(that.quantity_produced, quantity_produced) == 0 && Objects.equals(id, that.id) && Objects.equals(productionRun, that.productionRun) && Objects.equals(product_usage, that.product_usage) && Objects.equals(production_line_and_target, that.production_line_and_target) && Objects.equals(representatives_and_technician, that.representatives_and_technician) && Objects.equals(comments, that.comments) && Objects.equals(rag_used, that.rag_used) && Objects.equals(moisture_content, that.moisture_content) && Objects.equals(average_cigarette_weight, that.average_cigarette_weight) && Objects.equals(loose_end_testing, that.loose_end_testing) && Objects.equals(diameter_testing, that.diameter_testing) && Objects.equals(filter_size_stick_alignment, that.filter_size_stick_alignment) && Objects.equals(speed_of_maker_against_waste, that.speed_of_maker_against_waste) && Objects.equals(tightness_of_bopp_for_pack, that.tightness_of_bopp_for_pack) && Objects.equals(open_3_random_bricks, that.open_3_random_bricks) && Objects.equals(correct_placement_of_aluminium_foil, that.correct_placement_of_aluminium_foil) && Objects.equals(whiteness_of_white_inners, that.whiteness_of_white_inners) && Objects.equals(gluing_of_inner_top_flap, that.gluing_of_inner_top_flap) && Objects.equals(positioning_of_customer_comments, that.positioning_of_customer_comments) && Objects.equals(correct_lettering_and_positioning_on_the_pack, that.correct_lettering_and_positioning_on_the_pack) && Objects.equals(smoke_tests, that.smoke_tests) && Objects.equals(tipping_used, that.tipping_used) && Objects.equals(blanks_used, that.blanks_used) && Objects.equals(filters_used, that.filters_used);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productionRun, product_usage, production_line_and_target, representatives_and_technician, comments, rag_used, moisture_content, average_cigarette_weight, loose_end_testing, diameter_testing, filter_size_stick_alignment, speed_of_maker_against_waste, tightness_of_bopp_for_pack, open_3_random_bricks, correct_placement_of_aluminium_foil, whiteness_of_white_inners, gluing_of_inner_top_flap, positioning_of_customer_comments, correct_lettering_and_positioning_on_the_pack, smoke_tests, tipping_used, blanks_used, filters_used, quantity_produced);
    }

    @Override
    public String toString() {
        return "DailyProductionRunKeyNote{" +
                "id=" + id +
                ", productionRun=" + productionRun +
                ", product_usage=" + product_usage +
                ", production_line_and_target='" + production_line_and_target + '\'' +
                ", representatives_and_technician='" + representatives_and_technician + '\'' +
                ", comments='" + comments + '\'' +
                ", rag_used='" + rag_used + '\'' +
                ", moisture_content='" + moisture_content + '\'' +
                ", average_cigarette_weight='" + average_cigarette_weight + '\'' +
                ", loose_end_testing='" + loose_end_testing + '\'' +
                ", diameter_testing='" + diameter_testing + '\'' +
                ", filter_size_stick_alignment='" + filter_size_stick_alignment + '\'' +
                ", speed_of_maker_against_waste='" + speed_of_maker_against_waste + '\'' +
                ", tightness_of_bopp_for_pack='" + tightness_of_bopp_for_pack + '\'' +
                ", open_3_random_bricks='" + open_3_random_bricks + '\'' +
                ", correct_placement_of_aluminium_foil='" + correct_placement_of_aluminium_foil + '\'' +
                ", whiteness_of_white_inners='" + whiteness_of_white_inners + '\'' +
                ", gluing_of_inner_top_flap='" + gluing_of_inner_top_flap + '\'' +
                ", positioning_of_customer_comments='" + positioning_of_customer_comments + '\'' +
                ", correct_lettering_and_positioning_on_the_pack='" + correct_lettering_and_positioning_on_the_pack + '\'' +
                ", smoke_tests='" + smoke_tests + '\'' +
                ", tipping_used='" + tipping_used + '\'' +
                ", blanks_used='" + blanks_used + '\'' +
                ", filters_used='" + filters_used + '\'' +
                ", quantity_produced=" + quantity_produced +
                '}';
    }
}