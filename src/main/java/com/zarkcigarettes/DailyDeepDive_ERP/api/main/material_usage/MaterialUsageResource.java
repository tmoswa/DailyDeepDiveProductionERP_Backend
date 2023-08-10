package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/material-usage")
public class MaterialUsageResource {

    private final MaterialUsageServiceImplementation materialUsageServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final ProductServiceImplementation productServiceImplementation;
    @Autowired
    public MaterialUsageResource(MaterialUsageServiceImplementation materialUsageServiceImplementation,
                                 NTMsServiceImplementation ntMsServiceImplementation,
                                 ProductServiceImplementation productServiceImplementation) {
        this.materialUsageServiceImplementation = materialUsageServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
        this.productServiceImplementation=productServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllMaterialUsage(@PathVariable("id") Long productID) {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_usage",materialUsageServiceImplementation.materialUsageList(productID)))
                        .data_corresponding(of("ntms",ntMsServiceImplementation.ntmsList(50)))
                        .data_corresponding1(of("products",productServiceImplementation.productList(50)))
                        .message("successfully retrieved all material usage")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerMaterialUsage(@RequestBody MaterialUsage materialUsage) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_usage",materialUsageServiceImplementation.saveMaterialUsage(materialUsage)))
                        .message("successfully created product")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteProduct(@PathVariable("id") Long id){

        boolean successfullyDeleted = materialUsageServiceImplementation.deleteMaterialUsage(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_usage",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted material_usage":"Error in deleting material_usage, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateMaterialUsage(@PathVariable("id") Long id,@RequestBody MaterialUsage materialUsage) {

        boolean updatedSuccessfully=materialUsageServiceImplementation.updateMaterialUsage(id,materialUsage);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_usage",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated material_usage":"Error in updating material_usage, Either material_usage is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
