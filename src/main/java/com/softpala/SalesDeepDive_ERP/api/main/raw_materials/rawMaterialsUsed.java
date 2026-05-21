package com.softpala.SalesDeepDive_ERP.api.main.raw_materials;

import com.softpala.SalesDeepDive_ERP.persistence.model.MainEntity;
import lombok.Data;

@Data
public  class rawMaterialsUsed{
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