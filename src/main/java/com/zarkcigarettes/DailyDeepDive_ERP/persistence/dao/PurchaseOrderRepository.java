package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    PurchaseOrder findByName(String name);
    PurchaseOrder findByDescription(String description);
    @Override
    void delete(PurchaseOrder purchaseOrder);

    Optional<PurchaseOrder> findPurchaseOrderByName(String name);
    @Query("SELECT s FROM PurchaseOrder s where s.main_entity_po=?1")
    Collection<PurchaseOrder> findPurchaseOrderByMainEntity(MainEntity mainEntity);
}
