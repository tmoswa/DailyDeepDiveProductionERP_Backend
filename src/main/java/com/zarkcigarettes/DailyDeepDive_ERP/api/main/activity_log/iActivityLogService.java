package com.zarkcigarettes.DailyDeepDive_ERP.api.main.activity_log;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ActivityLog;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;

import java.util.Collection;

public interface iActivityLogService {
    Collection<ActivityLog> activityLogList();
    ActivityLog saveActivityLog(ActivityLog activityLog);
    boolean deleteActivityLog(Long id);
}
