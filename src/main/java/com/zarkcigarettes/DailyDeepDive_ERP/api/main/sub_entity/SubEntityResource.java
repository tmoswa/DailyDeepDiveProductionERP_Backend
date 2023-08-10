package com.zarkcigarettes.DailyDeepDive_ERP.api.main.sub_entity;

import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.CurrencyServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type.EntityTypeServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.MainEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.SubEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/sub-entity")
public class SubEntityResource {

    private final SubEntityServiceImplementation subEntityServiceImplementation;

    private final MainEntityServiceImplementation mainEntityServiceImplementation;
    private final EntityTypeServiceImplementation entityTypeServiceImplementation;
    private final CurrencyServiceImplementation currencyServiceImplementation;


    public SubEntityResource(SubEntityServiceImplementation subEntityServiceImplementation, MainEntityServiceImplementation mainEntityServiceImplementation, EntityTypeServiceImplementation entityTypeServiceImplementation, CurrencyServiceImplementation currencyServiceImplementation) {
        this.subEntityServiceImplementation = subEntityServiceImplementation;
        this.mainEntityServiceImplementation = mainEntityServiceImplementation;
        this.entityTypeServiceImplementation = entityTypeServiceImplementation;
        this.currencyServiceImplementation = currencyServiceImplementation;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllEntity() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("main-entities",mainEntityServiceImplementation.mainEntityList(30)))
                        .data_corresponding0(of("entity_types",mainEntityServiceImplementation.mainEntityList(30)))
                        .data_corresponding1(of("currencies",mainEntityServiceImplementation.mainEntityList(30)))
                        .data_corresponding2(of("sub-entities",subEntityServiceImplementation.subEntityList(30)))
                        .message("successfully retrieved all sub-entities")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerSubEntity(@RequestBody SubEntity subEntity) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("sub-entities",subEntityServiceImplementation.saveSubEntity(subEntity)))
                        .message("successfully created sub-entities")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteSubEntity(@PathVariable("id") Long id){

        boolean successfullyDeletedCurrency= subEntityServiceImplementation.deleteSubEntity(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("sub-entities",successfullyDeletedCurrency))
                        .message(successfullyDeletedCurrency?"successfully deleted sub-entity":"Error in deleting entity, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateSubEntity(@PathVariable("id") Long id,@RequestBody SubEntity subEntity) {

        boolean updatedSuccessfully=subEntityServiceImplementation.updateSubEntity(id,subEntity);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("sub-entity",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated sub-entity":"Error in updating entity, Either entity is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
