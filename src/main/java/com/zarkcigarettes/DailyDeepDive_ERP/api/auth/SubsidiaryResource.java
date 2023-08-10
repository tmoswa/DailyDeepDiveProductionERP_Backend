package com.zarkcigarettes.DailyDeepDive_ERP.api.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.UserServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Subsidiary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/subsidiary")
public class SubsidiaryResource {

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public SubsidiaryResource(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllSubsidiary() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("subsidiaries",userServiceImplementation.subsidiaryList(30)))
                        .message("successfully retrieved all subsidiaries")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerSubsidiary(@RequestBody Subsidiary priviledgeDetails) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("subsidiaries",userServiceImplementation.saveSubsidiary(priviledgeDetails)))
                        .message("successfully created subsidiary")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteSubsidiary(@PathVariable("id") Long privilegeID){

        boolean successfullyDeletedPermission= userServiceImplementation.deleteSubsidiary(privilegeID);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("permissions",successfullyDeletedPermission))
                        .message(successfullyDeletedPermission?"successfully deleted subsidiary":"Error in deleting subsidiary, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateSubsidiary(@PathVariable("id") Long privilegeID,@RequestBody Subsidiary priviledgeDetails) {

        boolean updatedSuccessfully=userServiceImplementation.updateSubsidiaryDetails(privilegeID,priviledgeDetails);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("permissions",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated subsidiary":"Error in updating subsidiary, Either subsidiary is already taked or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
