package com.zarkcigarettes.DailyDeepDive_ERP.api.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.UserServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/designation")
public class DesignationResource {

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public DesignationResource(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllDesignation() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("designations",userServiceImplementation.designationList(30)))
                        .message("successfully retrieved all designations")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerSubsidiary(@RequestBody Designation designationDetails) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("designations",userServiceImplementation.saveDesignation(designationDetails)))
                        .message("successfully created designation")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteDesignation(@PathVariable("id") Long designationID){

        boolean successfullyDeletedPermission= userServiceImplementation.deleteDesignation(designationID);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("designations",successfullyDeletedPermission))
                        .message(successfullyDeletedPermission?"successfully deleted designation":"Error in deleting designation, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateDesignationID(@PathVariable("id") Long designationID,@RequestBody Designation designationDetails) {

        boolean updatedSuccessfully=userServiceImplementation.updateDesignationDetails(designationID,designationDetails);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("designations",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated designation":"Error in updating designation, Either designation is already taked or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}