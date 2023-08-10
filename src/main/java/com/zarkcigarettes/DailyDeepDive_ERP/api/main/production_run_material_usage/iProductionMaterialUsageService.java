package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionMaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;

import java.util.Collection;

public interface iProductionMaterialUsageService {
    Collection<ProductionMaterialUsage> productionMaterialUsageList(Long productionRunID);
    //MaterialUsage saveProductionMaterialUsage(ProductionMaterialUsage[] productionMaterialUsage);
    ProductionMaterialUsage saveProductionMaterialUsage(ProductionMaterialUsage productionMaterialUsage);
    boolean deleteProductionMaterialUsage(Long id);
    //boolean updateProductionMaterialUsage(Long id, ProductionMaterialUsage[] productionMaterialUsage);
    boolean updateProductionMaterialUsage(Long id, ProductionMaterialUsage productionMaterialUsage);

    boolean deleteProductionMaterialUsageByProductionRun(ProductionRun productionRun);
}
