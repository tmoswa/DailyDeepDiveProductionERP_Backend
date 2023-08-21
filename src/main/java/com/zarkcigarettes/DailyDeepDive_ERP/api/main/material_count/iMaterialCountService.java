package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_count;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialStockCount;

import java.util.Collection;

public interface iMaterialCountService {
    Collection<MaterialCount> materialCountList(Long productionRunID);
    MaterialCount saveMaterialCount(MaterialCount materialCount);
    boolean deleteMaterialCount(Long id);
    boolean updateMaterialCount(Long id, MaterialCount materialCount);

    boolean deleteMaterialCountByMaterialStockCount(MaterialStockCount materialStockCount);
}
