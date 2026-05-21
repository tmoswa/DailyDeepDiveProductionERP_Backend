package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.Designation;
import com.softpala.SalesDeepDive_ERP.persistence.model.Increaments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncreamentsRepository extends JpaRepository<Increaments, Long> {


}
