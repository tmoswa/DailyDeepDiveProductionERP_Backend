package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.SubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubEntityRepository extends JpaRepository<SubEntity, Long> {

    @Query("SELECT s FROM SubEntity s where s.name=?1")
    Optional<SubEntity> findMainEntityByNameD(String name);

    @Query("SELECT s FROM SubEntity s where s.name=?1")
    SubEntity findSubEntityByName(String name);

    @Override
    void delete(SubEntity subEntity);



}
