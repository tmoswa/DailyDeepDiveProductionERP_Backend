package com.zarkcigarettes.DailyDeepDive_ERP.api.main.product;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.MainEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/product")
public class ProductResource {

    private final ProductServiceImplementation productServiceImplementation;
    private final MainEntityServiceImplementation mainEntityServiceImplementation;
    @Autowired
    public ProductResource(ProductServiceImplementation productServiceImplementation,MainEntityServiceImplementation mainEntityServiceImplementation) {
        this.productServiceImplementation = productServiceImplementation;
        this.mainEntityServiceImplementation=mainEntityServiceImplementation;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllProducts() {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("products",productServiceImplementation.productList(30)))
                        .data_corresponding(of("mainEntities",mainEntityServiceImplementation.mainEntityList(30)))
                        .message("successfully retrieved all product")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/produced/{fro}/{tto}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getUsedMaterials(@PathVariable("fro") String fro,@PathVariable("tto") String tto) {
        LocalDate from=LocalDate.parse(fro).minusDays(1);
        LocalDate to=LocalDate.parse(tto).plusDays(1);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("produced_products",productServiceImplementation.producedList(from,to,900)))
                        .message("successfully retrieved used produced_products")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerProduct(@RequestBody Product product) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("products",productServiceImplementation.saveNTMs(product)))
                        .message("successfully created product")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteProduct(@PathVariable("id") Long id){

        boolean successfullyDeleted = productServiceImplementation.deleteProduct(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("product",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted product":"Error in deleting product, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateNTM(@PathVariable("id") Long id,@RequestBody Product product) {

        boolean updatedSuccessfully=productServiceImplementation.updateProduct(id,product);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("product",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated product":"Error in updating product, Either product is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
