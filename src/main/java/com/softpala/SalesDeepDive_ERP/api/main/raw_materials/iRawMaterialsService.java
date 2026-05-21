package com.softpala.SalesDeepDive_ERP.api.main.raw_materials;


import com.softpala.SalesDeepDive_ERP.persistence.model.Currency;
import com.softpala.SalesDeepDive_ERP.persistence.model.RawMaterials;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;
import com.softpala.SalesDeepDive_ERP.persistence.model.PurchaseOrder;

import java.util.Collection;

public interface iRawMaterialsService {

    Collection<rawMaterialsUsed> ntmsList(int limit);
    Collection<RawMaterials> ntmList(int limit);
    RawMaterials saveNTMs(RawMaterials ntMs);
    boolean deleteNTMs(Long id);
    boolean updateNTMs(Long id, RawMaterials ntMs);

}
