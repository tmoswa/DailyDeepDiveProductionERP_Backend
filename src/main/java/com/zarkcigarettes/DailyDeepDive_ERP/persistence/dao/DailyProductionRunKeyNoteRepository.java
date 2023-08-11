package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface DailyProductionRunKeyNoteRepository extends JpaRepository<DailyProductionRunKeyNote, Long> {

    @Override
    void delete(DailyProductionRunKeyNote dailyProductionRunKeyNote);
    @Query("DELETE FROM DailyProductionRunKeyNote where production_run_id=?1")
    void deleteByRun(Long productionRun);

    @Query("SELECT s FROM DailyProductionRunKeyNote s where s.productionRun=?1")
    Collection<DailyProductionRunKeyNote> findDailyProductionRunKeyNoteByProductionRun(ProductionRun productionRun);

    @Query("SELECT s FROM DailyProductionRunKeyNote s where s.product_usage=?1")
    Collection<DailyProductionRunKeyNote> findDailyProductionRunKeyNoteByproduct_usage(Product product);
}
