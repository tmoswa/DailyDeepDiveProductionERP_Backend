package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface NTMsRepository extends JpaRepository<NTMs, Long> {

    NTMs findByName(String name);
    NTMs findByCode(String name);
    @Override
    void delete(NTMs currency);

    Optional<NTMs> findCurrencyByName(String name);

    @Query("SELECT s FROM NTMs")
    Collection<NTMs> findAllNTMs(int limit);
}
