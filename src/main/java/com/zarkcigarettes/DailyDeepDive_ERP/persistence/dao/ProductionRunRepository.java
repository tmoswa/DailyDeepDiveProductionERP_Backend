package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ProductionRunRepository extends JpaRepository<ProductionRun, Long> {

    @Override
    void delete(ProductionRun productionRun);

    @Query("SELECT s FROM ProductionRun s where s.product_production_run=?1")
    Collection<ProductionRun> findProductionRunByProduct(Product product);


}
