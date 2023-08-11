package com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc;

import com.zarkcigarettes.DailyDeepDive_ERP.auth.LoggedInUserDetails;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ActivityLogRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.UserRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ActivityLog;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    public void addActivityLog(String description,String module){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ActivityLog activityLog=new ActivityLog();
        LoggedInUserDetails loggedUser=new LoggedInUserDetails(userRepository);
        Optional<User> clientUser=loggedUser.getLoggedUser(auth);
        assert clientUser.orElse(null) != null;
        activityLog.setUser_details(clientUser.orElse(null).getFirstName()+" "+clientUser.orElse(null).getLastName());
        activityLog.setActivity_description(description);
        activityLog.setModule(module);
        activityLog.setDate(LocalDate.now());
        activityLogRepository.save(activityLog);
        loggedUser=null;

    }
}
