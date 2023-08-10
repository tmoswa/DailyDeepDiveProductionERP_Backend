package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, Long> {

    @Override
    void delete(MaterialUsage materialUsage);

    @Query("SELECT s FROM MaterialUsage s where s.product_usage=?1")
    Collection<MaterialUsage> findMaterialUsageByProduct(Product product);
}
