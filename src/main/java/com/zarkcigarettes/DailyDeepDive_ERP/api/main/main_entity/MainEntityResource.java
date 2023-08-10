package com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity;

import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.CurrencyServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type.EntityTypeServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.sub_entity.SubEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/main-entity")
public class MainEntityResource {

    private final MainEntityServiceImplementation mainEntityServiceImplementation;
    private final EntityTypeServiceImplementation entityTypeServiceImplementation;
    private final CurrencyServiceImplementation currencyServiceImplementation;
    private final SubEntityServiceImplementation subEntityServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;

    public MainEntityResource(MainEntityServiceImplementation mainEntityServiceImplementation, EntityTypeServiceImplementation entityTypeServiceImplementation, CurrencyServiceImplementation currencyServiceImplementation, SubEntityServiceImplementation subEntityServiceImplementation,NTMsServiceImplementation ntMsServiceImplementation) {
        this.mainEntityServiceImplementation = mainEntityServiceImplementation;
        this.entityTypeServiceImplementation = entityTypeServiceImplementation;
        this.currencyServiceImplementation = currencyServiceImplementation;
        this.subEntityServiceImplementation = subEntityServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllEntity() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entities",mainEntityServiceImplementation.mainEntityList(30)))
                        .data_corresponding0(of("entity_types",entityTypeServiceImplementation.entityTypeList(30)))
                        .data_corresponding1(of("currencies",currencyServiceImplementation.currencyList(30)))
                        .data_corresponding2(of("sub_entities",subEntityServiceImplementation.subEntityList(30)))
                        .data_corresponding3(of("allNTMs",ntMsServiceImplementation.ntmsList(30)))
                        .message("successfully retrieved all entities")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerMainEntity(@RequestBody MainEntity mainEntity) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entities",mainEntityServiceImplementation.saveMainEntity(mainEntity)))
                        .message("successfully created entities")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteEntity(@PathVariable("id") Long id){

        boolean successfullyDeletedCurrency= mainEntityServiceImplementation.deleteMainEntity(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entities",successfullyDeletedCurrency))
                        .message(successfullyDeletedCurrency?"successfully deleted entity":"Error in deleting entity, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateEntity(@PathVariable("id") Long id,@RequestBody MainEntity mainEntity) {

        boolean updatedSuccessfully=mainEntityServiceImplementation.updateMainEntity(id,mainEntity);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("entity",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated entity":"Error in updating entity, Either entity is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
