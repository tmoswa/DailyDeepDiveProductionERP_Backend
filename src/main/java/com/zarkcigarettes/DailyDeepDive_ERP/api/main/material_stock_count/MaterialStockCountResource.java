package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_stock_count;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.FileDownloadUtil;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run.ProductionRunServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialStockCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(origins = "https://production.zarkcigarettes.com")
@RequestMapping("api/material-stock-count")
public class MaterialStockCountResource {

    private final MaterialStockCountServiceImplementation materialStockCountServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final ProductServiceImplementation productServiceImplementation;
    @Autowired
    public MaterialStockCountResource(MaterialStockCountServiceImplementation materialStockCountServiceImplementation,
                                      NTMsServiceImplementation ntMsServiceImplementation,
                                      ProductServiceImplementation productServiceImplementation) {
        this.materialStockCountServiceImplementation = materialStockCountServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
        this.productServiceImplementation=productServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllMaterialStockCount(@PathVariable("id") Long productID) {
        Collection<MaterialStockCount> materialStockCount=materialStockCountServiceImplementation.materialStockCountList(productID);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("materialStockCount",materialStockCount))
                        .data_corresponding(of("ntms",ntMsServiceImplementation.ntmsList(5000)))
                        .data_corresponding1(of("products",productServiceImplementation.productList(5000)))
                        .message("successfully retrieved all material Stock Count")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }





        @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerMaterialStockCount(@RequestBody MaterialStockCount materialStockCount) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("materialStockCount",materialStockCountServiceImplementation.saveMaterialStockCount(materialStockCount)))
                        .data_corresponding1(of("products",productServiceImplementation.productList(500)))
                        .message("successfully created production run")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteMaterialStockCount(@PathVariable("id") Long id){

        boolean successfullyDeleted = materialStockCountServiceImplementation.deleteMaterialStockCount(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("materialStockCount",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted materialStockCount":"Error in deleting materialStockCount, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }



    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateMaterialStockCount(@PathVariable("id") Long id,@RequestBody MaterialStockCount materialStockCount) {

        boolean updatedSuccessfully=materialStockCountServiceImplementation.updateMaterialStockCount(id,materialStockCount);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("materialStockCount",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated materialStockCount":"Error in updating materialStockCount, Either materialStockCount is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
