package com.softpala.SalesDeepDive_ERP.persistence.dao;


import com.softpala.SalesDeepDive_ERP.persistence.model.ActivityLog;
import com.softpala.SalesDeepDive_ERP.persistence.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Override
    void delete(ActivityLog activityLog);

    public List<ActivityLog> findAllByOrderByIdDesc();

}
