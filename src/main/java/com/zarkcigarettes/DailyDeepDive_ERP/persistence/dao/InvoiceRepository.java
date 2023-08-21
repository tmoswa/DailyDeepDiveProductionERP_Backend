package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Invoice;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findByDescription(String description);
    @Override
    void delete(Invoice invoice);

    @Query("SELECT s FROM Invoice s where s.main_entity_inv=?1 ORDER BY id DESC")
    Collection<Invoice> findInvoiceByMainEntity(MainEntity mainEntity);
}
