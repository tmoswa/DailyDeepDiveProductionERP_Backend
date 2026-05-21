package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.ActivityLog;
import com.softpala.SalesDeepDive_ERP.persistence.model.MainEntity;
import com.softpala.SalesDeepDive_ERP.persistence.model.RawMaterials;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RawMaterialsRepository extends JpaRepository<RawMaterials, Long> {

    RawMaterials findByName(String name);
    RawMaterials findByCode(String name);
    @Override
    void delete(RawMaterials currency);

    Optional<RawMaterials> findCurrencyByName(String name);

    @Query("SELECT s FROM RawMaterials s")
    Collection<RawMaterials> findAllNTMs(int limit);

    public List<RawMaterials> findAllByOrderBySequenceAsc();
}
