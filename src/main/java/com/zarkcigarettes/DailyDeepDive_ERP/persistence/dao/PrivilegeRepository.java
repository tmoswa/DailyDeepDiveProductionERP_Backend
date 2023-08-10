package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Query("SELECT s FROM Privilege s where s.name=?1")
    Optional<Privilege> findApplicationPreviledgeDetailsByName(String name);

    Privilege findPrivilegeByName(String name);

    @Override
    void delete(Privilege privilege);



}
