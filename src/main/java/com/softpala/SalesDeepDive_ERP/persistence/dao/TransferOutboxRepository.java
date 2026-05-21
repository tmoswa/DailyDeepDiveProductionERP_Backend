package com.softpala.SalesDeepDive_ERP.persistence.dao;
import com.softpala.SalesDeepDive_ERP.persistence.model.TransferOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface TransferOutboxRepository extends JpaRepository<TransferOutbox, Long> {
    List<TransferOutbox> findByStatusOrderByIdAsc(String status);
    List<TransferOutbox> findByIdGreaterThanAndStatusOrderByIdAsc(Long id, String status);
    Optional<TransferOutbox> findByErpTransferId(String erpTransferId);
}
