package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface MaterialCountRepository extends JpaRepository<MaterialCount, Long> {

    @Override
    void delete(MaterialCount materialCount);
    @Query("DELETE FROM MaterialCount where production_run_id=?1")
    void deleteByMaterialStockCount(Long materalStockCountID);

    @Query("SELECT s FROM MaterialCount s where s.materialStockCount=?1")
    Collection<MaterialCount> findMaterialCountByMaterialStockCount(MaterialStockCount materialStockCount);

    @Query("SELECT s FROM MaterialCount s where s.ntMs_usage=?1")
    Collection<MaterialCount> findMaterialCountByntMs_usage(NTMs ntms);
}
