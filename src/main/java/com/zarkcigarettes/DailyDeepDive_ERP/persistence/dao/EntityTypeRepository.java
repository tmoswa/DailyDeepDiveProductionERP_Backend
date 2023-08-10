package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EntityTypeRepository extends JpaRepository<EntityType, Long> {

    @Query("SELECT s FROM EntityType s where s.name=?1")
    Optional<EntityType> findClientTypeDetailsByName(String name);

    EntityType findClientTypeByName(String name);

    @Override
    void delete(EntityType clientType);



}
