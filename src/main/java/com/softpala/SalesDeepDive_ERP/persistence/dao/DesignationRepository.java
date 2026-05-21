package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

    Designation findByName(String name);

    @Override
    void delete(Designation role);

    Optional<Designation> findApplicationDesignationDetailsByName(String name);
}
