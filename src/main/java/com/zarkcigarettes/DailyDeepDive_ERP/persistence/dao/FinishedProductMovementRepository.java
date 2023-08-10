package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface FinishedProductMovementRepository extends JpaRepository<FinishedProductMovement, Long> {

    @Override
    void delete(FinishedProductMovement finishedProductMovement);

    @Query("SELECT s FROM FinishedProductMovement s where s.product_usage=?1")
    Collection<FinishedProductMovement> findFinishedProductMovementByProduct(Product product);
}
