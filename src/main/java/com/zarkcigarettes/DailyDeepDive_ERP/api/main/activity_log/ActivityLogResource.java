package com.zarkcigarettes.DailyDeepDive_ERP.api.main.activity_log;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.finished_product_movement.FinishedProductMovementServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/activity-log")
public class ActivityLogResource {

    private final ActivityLogServiceImplementation activityLogServiceImplementation;
    @Autowired
    public ActivityLogResource(ActivityLogServiceImplementation activityLogServiceImplementation) {
        this.activityLogServiceImplementation = activityLogServiceImplementation;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllActivityLog() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("activity_logs",activityLogServiceImplementation.activityLogList()))
                        .message("successfully retrieved all activity logs")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteActivityLog(@PathVariable("id") Long id){

        boolean successfullyDeleted = activityLogServiceImplementation.deleteActivityLog(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("activity_logs",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted activity_log":"Error in deleting activity_log, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




}
