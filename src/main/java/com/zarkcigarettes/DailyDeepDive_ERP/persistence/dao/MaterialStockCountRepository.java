package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialStockCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MaterialStockCountRepository extends JpaRepository<MaterialStockCount, Long> {

    @Override
    void delete(MaterialStockCount materialStockCount);

    @Query("SELECT s FROM MaterialStockCount s where s.product_production_run=?1 ORDER BY id DESC")
    Collection<MaterialStockCount> findMaterialStockCountByProduct(Product product);

    public List<MaterialStockCount> findAllByOrderByIdDesc();

}
