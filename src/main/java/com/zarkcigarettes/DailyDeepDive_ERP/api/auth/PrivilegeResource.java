package com.zarkcigarettes.DailyDeepDive_ERP.api.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.UserServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/permission")
public class PrivilegeResource {

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public PrivilegeResource(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-PERMISSIONS-READ')")
    public ResponseEntity<Response> getAllPrivileges() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("permissions",userServiceImplementation.privilegeList(3000)))
                        .message("successfully retrieved all permissions")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-PERMISSIONS-ALTER')")
    public ResponseEntity<Response> registerPrivilege(@RequestBody Privilege priviledgeDetails) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("permissions",userServiceImplementation.savePrivilege(priviledgeDetails)))
                        .message("successfully created permission")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-PERMISSIONS-DELETE')")
    public ResponseEntity<Response> deletePrivilege(@PathVariable("id") Long privilegeID){

       boolean successfullyDeletedPermission= userServiceImplementation.deletePreviledge(privilegeID);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("permissions",successfullyDeletedPermission))
                        .message(successfullyDeletedPermission?"successfully deleted permission":"Error in deleting permission, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
/*    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE_PERMISSIONS_DELETE')")
    public void deletePrivilege(@PathVariable("id") Long privilegeID){
        userServiceImplementation.deletePreviledge(privilegeID);
    }*/

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-PERMISSIONS-ALTER')")
    public ResponseEntity<Response> updatePrivilege(@PathVariable("id") Long privilegeID,@RequestBody Privilege priviledgeDetails) {

        boolean updatedSuccessfully=userServiceImplementation.updatePrivilegeDetails(privilegeID,priviledgeDetails);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("permissions",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated permission":"Error in updating permission, Either Permission is already taked or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




}
