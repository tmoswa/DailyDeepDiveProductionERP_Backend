package com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type;

import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.CurrencyServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/entityType")
public class EntityTypeResource {

    private final EntityTypeServiceImplementation entityTypeServiceImplementation;
    @Autowired
    public EntityTypeResource(EntityTypeServiceImplementation entityTypeServiceImplementation) {
        this.entityTypeServiceImplementation = entityTypeServiceImplementation;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllEntityType() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entity_types",entityTypeServiceImplementation.entityTypeList(30)))
                        .message("successfully retrieved all entity_types")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerCurrency(@RequestBody EntityType entityType) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entity_types",entityTypeServiceImplementation.saveEntityType(entityType)))
                        .message("successfully created entity_types")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteEntityType(@PathVariable("id") Long id){

        boolean successfullyDeletedCurrency= entityTypeServiceImplementation.deleteEntityType(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entity_type",successfullyDeletedCurrency))
                        .message(successfullyDeletedCurrency?"successfully deleted entity_type":"Error in deleting entity_type, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateEntityType(@PathVariable("id") Long id,@RequestBody EntityType entityType) {

        boolean updatedSuccessfully=entityTypeServiceImplementation.updateEntityType(id,entityType);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("currency",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated entityType":"Error in updating entityType, Either entityType is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
