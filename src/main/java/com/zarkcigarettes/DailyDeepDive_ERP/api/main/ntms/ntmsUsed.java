package com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import lombok.Data;

@Data
public  class ntmsUsed{
    Long id;
    private MainEntity main_entity_material;
    private String name;
    private String code;
    private String size;
    private double quantity;
    private int lead_time;
    private String description;
    private String unit_of_measure;
}