package com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms;

import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.CurrencyServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.MainEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/ntms")
@Slf4j
public class NTMsResource {

    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final MainEntityServiceImplementation mainEntityServiceImplementation;
    @Autowired
    public NTMsResource(NTMsServiceImplementation ntMsServiceImplementation,
                        MainEntityServiceImplementation mainEntityServiceImplementation) {
        this.ntMsServiceImplementation = ntMsServiceImplementation;
        this.mainEntityServiceImplementation=mainEntityServiceImplementation;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllNTMs() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("ntms",ntMsServiceImplementation.ntmsList(30000)))
                        .data_corresponding(of("main_entities",mainEntityServiceImplementation.mainEntityList(50000)))
                        .message("successfully retrieved all ntms")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/expectedRequired")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllRequiredExpectedNTMs() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("required_expected_ntms",ntMsServiceImplementation.ntmsRequiredExpectedList(300000)))
                        .data_corresponding(of("main_entities",mainEntityServiceImplementation.mainEntityList(50000)))
                        .message("successfully retrieved all ntms")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/usedMaterials/{fro}/{tto}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getUsedMaterials(@PathVariable("fro") String fro, @PathVariable("tto") String tto, @RequestBody Product product) {
        LocalDate from=LocalDate.parse(fro);
        LocalDate to=LocalDate.parse(tto);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("used_ntms",ntMsServiceImplementation.completeNtmsUsed(from,to,product,900000)))
                        .message("successfully retrieved used ntms")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerNTMs(@RequestBody NTMs ntMs) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("ntms",ntMsServiceImplementation.saveNTMs(ntMs)))
                        .message("successfully created ntms")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteNTMs(@PathVariable("id") Long id){

        boolean successfullyDeletedNTMs = ntMsServiceImplementation.deleteNTMs(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("currency",successfullyDeletedNTMs))
                        .message(successfullyDeletedNTMs?"successfully deleted NTM":"Error in deleting NTM, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateNTM(@PathVariable("id") Long id,@RequestBody NTMs ntMs) {

        boolean updatedSuccessfully=ntMsServiceImplementation.updateNTMs(id,ntMs);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("ntms",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated ntms":"Error in updating ntms, Either currency is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
