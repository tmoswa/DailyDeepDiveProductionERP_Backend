package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Designation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Increaments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncreamentsRepository extends JpaRepository<Increaments, Long> {


}
