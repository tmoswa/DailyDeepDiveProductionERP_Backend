package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.MainEntity;
import com.softpala.SalesDeepDive_ERP.persistence.model.MaterialUsage;
import com.softpala.SalesDeepDive_ERP.persistence.model.RawMaterials;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);
    Product findByCode(String name);
    @Override
    void delete(Product product);

    Optional<Product> findProductByName(String name);
    @Query("SELECT s FROM Product s where s.main_entity_product=?1")
    Collection<Product> findProductByMainEntity(MainEntity mainEntity);
}
