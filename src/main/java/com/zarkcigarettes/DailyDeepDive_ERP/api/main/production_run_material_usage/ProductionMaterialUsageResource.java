package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionMaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionMaterialUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/production-material-usage")
public class ProductionMaterialUsageResource {

    private final MaterialUsageServiceImplementation materialUsageServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final ProductServiceImplementation productServiceImplementation;
    private final ProductionRunMaterialUsageServiceImplementation productionRunMaterialUsageServiceImplementation;
    private final ProductionMaterialUsageRepository productionMaterialUsageRepository;
    @Autowired
    public ProductionMaterialUsageResource(MaterialUsageServiceImplementation materialUsageServiceImplementation,
                                           NTMsServiceImplementation ntMsServiceImplementation,
                                           ProductServiceImplementation productServiceImplementation,
                                           ProductionRunMaterialUsageServiceImplementation productionRunMaterialUsageServiceImplementation,
                                           ProductionMaterialUsageRepository productionMaterialUsageRepository) {
        this.materialUsageServiceImplementation = materialUsageServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
        this.productServiceImplementation=productServiceImplementation;
        this.productionRunMaterialUsageServiceImplementation=productionRunMaterialUsageServiceImplementation;
        this.productionMaterialUsageRepository=productionMaterialUsageRepository;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllProductionMaterialUsage(@PathVariable("id") Long productionRunID) {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_material_usage",productionRunMaterialUsageServiceImplementation.productionMaterialUsageList(productionRunID)))
                        .message("successfully retrieved all production material usage")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerProductionMaterialUsage(@RequestBody ProductionMaterialUsage[] productionMaterialUsages) {
        for(ProductionMaterialUsage productionMaterialUsage:productionMaterialUsages){
            productionRunMaterialUsageServiceImplementation.saveProductionMaterialUsage(productionMaterialUsage);
        }
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_material_usage","Saved"))
                        .message("successfully created production material usage")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteProductionMaterialUsage(@PathVariable("id") Long id){

        boolean successfullyDeleted = productionRunMaterialUsageServiceImplementation.deleteProductionMaterialUsage(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_material_usage",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted production_material_usage":"Error in deleting production_material_usage, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateProductionMaterialUsage(@PathVariable("id") Long id,@RequestBody ProductionMaterialUsage[] productionMaterialUsages) {

        for(ProductionMaterialUsage productionMaterialUsage:productionMaterialUsages){
            Collection<ProductionMaterialUsage> productionMaterialUsag=  productionMaterialUsageRepository.findProductionMaterialUsageByntMs_usage(productionMaterialUsage.getNtMs_usage())
                    .stream().filter(pm->pm.getProductionRun().equals(productionMaterialUsage.getProductionRun())).collect(Collectors.toList());
            for(ProductionMaterialUsage productionMaterialUsa: productionMaterialUsag){
                productionRunMaterialUsageServiceImplementation.updateProductionMaterialUsage(productionMaterialUsa.getId(),productionMaterialUsage);
            }
        }
        boolean updatedSuccessfully=true;
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_material_usage",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated production_material_usage":"Error in updating production_material_usage, Either production_material_usage is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
