package com.softpala.SalesDeepDive_ERP.persistence.dao;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;
import com.softpala.SalesDeepDive_ERP.persistence.model.WasteThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface WasteThresholdRepository extends JpaRepository<WasteThreshold, Long> {
    Optional<WasteThreshold> findByProduct(Product product);
}
