package com.zarkcigarettes.DailyDeepDive_ERP.api.finished_product_movement;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/finished-product")
public class FinishedProductMovementResource {

    private final FinishedProductMovementServiceImplementation finishedProductMovementServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final ProductServiceImplementation productServiceImplementation;
    @Autowired
    public FinishedProductMovementResource(FinishedProductMovementServiceImplementation finishedProductMovementServiceImplementation,
                                           NTMsServiceImplementation ntMsServiceImplementation,
                                           ProductServiceImplementation productServiceImplementation) {
        this.finishedProductMovementServiceImplementation = finishedProductMovementServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
        this.productServiceImplementation=productServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllFinishedProduct(@PathVariable("id") Long productID) {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("finished_product_movement",finishedProductMovementServiceImplementation.finishedProductMovementList(productID)))
                        .data_corresponding(of("products",productServiceImplementation.productList(50)))
                        .message("successfully retrieved all finished product movement")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerFinishedProduct(@RequestBody FinishedProductMovement finishedProductMovement) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("finished_product_movement",finishedProductMovementServiceImplementation.saveFinishedProductMovement(finishedProductMovement)))
                        .message("successfully created finished product")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteFinishedProduct(@PathVariable("id") Long id){

        boolean successfullyDeleted = finishedProductMovementServiceImplementation.deleteFinishedProductMovement(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("finished_product_movement",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted finished_product_movement":"Error in deleting finished_product_movement, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateFinishedProduct(@PathVariable("id") Long id,@RequestBody FinishedProductMovement finishedProductMovement) {

        boolean updatedSuccessfully=finishedProductMovementServiceImplementation.updateFinishedProductMovement(id,finishedProductMovement);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("finished_product_movement",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated finished_product_movement":"Error in updating finished_product_movement, Either finished_product_movement is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
