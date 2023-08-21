package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_stock_count;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ActivityLog;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialStockCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;

import java.util.Collection;
import java.util.List;

public interface iMaterialStockCountService {
    Collection<MaterialStockCount> materialStockCountList(int limit);
    Collection<MaterialStockCount> materialStockCountList(Long ProdutID);
    MaterialStockCount saveMaterialStockCount(MaterialStockCount materialStockCount);
    boolean deleteMaterialStockCount(Long id);
    boolean updateMaterialStockCount(Long id, MaterialStockCount materialStockCount);


}
