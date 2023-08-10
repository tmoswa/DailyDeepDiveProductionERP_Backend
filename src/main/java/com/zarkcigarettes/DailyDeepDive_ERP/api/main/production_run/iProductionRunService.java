package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;

import java.util.Collection;

public interface iProductionRunService {
    Collection<ProductionRun> productionRunList(int limit);
    Collection<ProductionRun> productionRunList(Long ProdutID);
    ProductionRun saveProductionRun(ProductionRun productionRun);
    boolean deleteProductionRun(Long id);
    boolean updateProductionRun(Long id, ProductionRun productionRun);
}
