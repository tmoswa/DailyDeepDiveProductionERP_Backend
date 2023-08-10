package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface MainEntityRepository extends JpaRepository<MainEntity, Long> {

    @Query("SELECT s FROM MainEntity s where s.legal_name=?1")
    Optional<MainEntity> findMainEntityByLegalNameD(String name);

    @Query("SELECT s FROM MainEntity s where s.legal_name=?1")
    MainEntity findMainEntityByLegal_name(String name);

    @Override
    void delete(MainEntity mainEntity);

    @Query("SELECT s FROM MainEntity s where s.main_entity_type=?1")
    Collection<MainEntity> mainEntityListByEntityType(Collection<EntityType> entityType);

}
