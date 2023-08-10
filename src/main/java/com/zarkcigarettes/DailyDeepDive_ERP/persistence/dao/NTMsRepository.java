package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NTMsRepository extends JpaRepository<NTMs, Long> {

    NTMs findByName(String name);
    NTMs findByCode(String name);
    @Override
    void delete(NTMs currency);

    Optional<NTMs> findCurrencyByName(String name);
}
