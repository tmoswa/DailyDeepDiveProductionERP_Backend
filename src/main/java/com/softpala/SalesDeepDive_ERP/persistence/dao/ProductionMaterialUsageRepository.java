package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ProductionMaterialUsageRepository extends JpaRepository<ProductionMaterialUsage, Long> {

    @Override
    void delete(ProductionMaterialUsage productionMaterialUsage);
    @Query("DELETE FROM ProductionMaterialUsage where production_run_id=?1")
    void deleteByRun(Long productionRun);

    @Query("SELECT s FROM ProductionMaterialUsage s where s.productionRun=?1")
    Collection<ProductionMaterialUsage> findProductionMaterialUsageByProductionRun(ProductionRun productionRun);

    @Query("SELECT s FROM ProductionMaterialUsage s where s.rawMaterialUsage=?1")
    Collection<ProductionMaterialUsage> findProductionMaterialUsageByRawMaterialUsage(RawMaterials ntms);

    @Query("SELECT s FROM ProductionMaterialUsage s where s.product_usage=?1")
    Collection<ProductionMaterialUsage> findProductionMaterialUsageByproduct_usage(Product product);
}
