package com.softpala.SalesDeepDive_ERP.api.main.activity_log;


import com.softpala.SalesDeepDive_ERP.persistence.model.ActivityLog;
import com.softpala.SalesDeepDive_ERP.persistence.model.FinishedProductMovement;

import java.util.Collection;

public interface iActivityLogService {
    Collection<ActivityLog> activityLogList();
    ActivityLog saveActivityLog(ActivityLog activityLog);
    boolean deleteActivityLog(Long id);
}
