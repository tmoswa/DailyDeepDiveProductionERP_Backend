package com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/currency")
public class CurrencyResource {

    private final CurrencyServiceImplementation currencyServiceImplementation;
    @Autowired
    public CurrencyResource(CurrencyServiceImplementation currencyServiceImplementation) {
        this.currencyServiceImplementation = currencyServiceImplementation;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllCurrency() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("currencies",currencyServiceImplementation.currencyList(30)))
                        .message("successfully retrieved all currencies")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerCurrency(@RequestBody Currency currency) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("currencies",currencyServiceImplementation.saveCurrency(currency)))
                        .message("successfully created currencies")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteCurrency(@PathVariable("id") Long id){

        boolean successfullyDeletedCurrency= currencyServiceImplementation.deleteCurrency(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("currency",successfullyDeletedCurrency))
                        .message(successfullyDeletedCurrency?"successfully deleted currency":"Error in deleting currency, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateCurrency(@PathVariable("id") Long id,@RequestBody Currency currency) {

        boolean updatedSuccessfully=currencyServiceImplementation.updateCurrency(id,currency);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("currency",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated currency":"Error in updating currency, Either currency is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
