package com.zarkcigarettes.DailyDeepDive_ERP.api.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.UserServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/role")
@Slf4j
public class RoleResource {

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public RoleResource(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-ROLES-READ')")
    public ResponseEntity<Response> getAllPrivileges() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("roles",userServiceImplementation.roleList(300)))
                        .data_corresponding(of("permissions",userServiceImplementation.privilegeList(30000)))
                        .message("successfully retrieved all roles")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-ROLES-ALTER')")
    public ResponseEntity<Response> registerPrivilege(@RequestBody Role roleDetails) {

       return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("roles",userServiceImplementation.saveRole(roleDetails)))
                        .message("successfully created role")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
       );

    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-ROLES-DELETE')")
    public ResponseEntity<Response> deletePrivilege(@PathVariable("id") Long roleID){

       boolean successfullyDeletedRole= userServiceImplementation.deleteRole(roleID);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("roles",successfullyDeletedRole))
                        .message(successfullyDeletedRole?"successfully deleted role":"Error in deleting role, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-ROLES-ALTER')")
    public ResponseEntity<Response> updatePrivilege(@PathVariable("id") Long privilegeID,@RequestBody Role roleDetails) {

        boolean updatedSuccessfully=userServiceImplementation.updateRoleDetails(privilegeID,roleDetails);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("roles",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated role":"Error in updating role, Either Role is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




}
