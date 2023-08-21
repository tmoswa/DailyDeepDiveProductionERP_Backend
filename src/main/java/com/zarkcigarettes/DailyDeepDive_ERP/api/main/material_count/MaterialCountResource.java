package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_count;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage.ProductionRunMaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MaterialCountRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MaterialStockCountRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionMaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialStockCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionMaterialUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/material-count")
public class MaterialCountResource {

    private final MaterialCountServiceImplementation materialCountServiceImplementation;
    private final MaterialCountRepository materialCountRepository;
    private final MaterialStockCountRepository materialStockCountRepository;
    @Autowired
    public MaterialCountResource(MaterialCountServiceImplementation materialCountServiceImplementation,
                                 MaterialCountRepository materialCountRepository,
                                 MaterialStockCountRepository materialStockCountRepository) {
        this.materialCountServiceImplementation = materialCountServiceImplementation;
        this.materialCountRepository=materialCountRepository;
        this.materialStockCountRepository=materialStockCountRepository;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllMaterialCount(@PathVariable("id") Long productID) {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_count",materialCountServiceImplementation.materialCountList(productID)))
                        .message("successfully retrieved all material count")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerMaterialCount(@RequestBody MaterialCount[] materialCounts) {
        for(MaterialCount materialCount:materialCounts){
            materialCountServiceImplementation.saveMaterialCount(materialCount);
        }
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_count","Saved"))
                        .message("successfully created material count")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteMaterialCount(@PathVariable("id") Long id){

        boolean successfullyDeleted = materialCountServiceImplementation.deleteMaterialCount(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_count",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted material count":"Error in deleting material count, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateMaterialCount(@PathVariable("id") Long id,@RequestBody MaterialCount[] materialCounts) {

        for(MaterialCount materialCount:materialCounts){

            MaterialStockCount details = materialStockCountRepository.findById(id)
                    .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("production material with id %d not found", id)));

            MaterialCount materialCounts_0=materialCountRepository.findMaterialCountByMaterialStockCount(details)
                    .stream().filter(materialCount2->materialCount2.getNtMs_usage().getName().equals(materialCount.getNtMs_usage().getName())).findAny().get();

            materialCountServiceImplementation.updateMaterialCount(materialCounts_0.getId(),materialCount);

            }
/*
            Collection<MaterialCount> materialCounts1=  materialCountRepository.findMaterialCountByntMs_usage(materialCount.getNtMs_usage())
                    .stream().filter(pm->pm.getMaterialStockCount().equals(materialCount.getMaterialStockCount())).collect(Collectors.toList());
            for(MaterialCount materialCount2: materialCounts1){
                materialCountServiceImplementation.updateMaterialCount(materialCount2.getId(),materialCount);
            }*/

        boolean updatedSuccessfully=true;
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("material_count",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated material_count":"Error in updating material_count, Either material_count is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
