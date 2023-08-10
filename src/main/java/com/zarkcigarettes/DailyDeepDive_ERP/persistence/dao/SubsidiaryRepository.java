package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Privilege;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

    Subsidiary findByName(String name);

    @Override
    void delete(Subsidiary role);

    Optional<Privilege> findApplicationPreviledgeDetailsByName(String name);
}
