package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;

import java.util.Collection;

public interface iMaterialUsageService {
    Collection<MaterialUsage> materialUsageList(Long ProdutID);
    MaterialUsage saveMaterialUsage(MaterialUsage materialUsage);
    boolean deleteMaterialUsage(Long id);
    boolean updateMaterialUsage(Long id, MaterialUsage materialUsage);
}
