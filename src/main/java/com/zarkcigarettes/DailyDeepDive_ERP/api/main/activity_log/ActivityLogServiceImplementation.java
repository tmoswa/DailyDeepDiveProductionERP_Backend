package com.zarkcigarettes.DailyDeepDive_ERP.api.main.activity_log;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.finished_product_movement.iFinishedProductMovementService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ActivityLogRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.FinishedProductMovementRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ActivityLog;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ActivityLogServiceImplementation implements iActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    @Override
    public Collection<ActivityLog> activityLogList() {
        return  activityLogRepository.findAllByOrderByIdDesc();
    }

    @Override
    public ActivityLog saveActivityLog(ActivityLog activityLog) {
        return activityLogRepository.save(activityLog);
    }

    @Override
    public boolean deleteActivityLog(Long id) {
            boolean exists = activityLogRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }

        activityLogRepository.deleteById(id);
            return  Boolean.TRUE;

    }

}
