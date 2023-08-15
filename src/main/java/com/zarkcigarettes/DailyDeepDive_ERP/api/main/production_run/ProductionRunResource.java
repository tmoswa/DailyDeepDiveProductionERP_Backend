package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.FileDownloadUtil;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Invoice;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
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
@RequestMapping("api/production-run")
public class ProductionRunResource {

    private final ProductionRunServiceImplementation productionRunServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final ProductServiceImplementation productServiceImplementation;
    @Autowired
    public ProductionRunResource(ProductionRunServiceImplementation productionRunServiceImplementation,
                                 NTMsServiceImplementation ntMsServiceImplementation,
                                 ProductServiceImplementation productServiceImplementation) {
        this.productionRunServiceImplementation = productionRunServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
        this.productServiceImplementation=productServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllMaterialUsage(@PathVariable("id") Long productID) {
        Collection<ProductionRun> productionRunList=productionRunServiceImplementation.productionRunList(productID);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_runs",productionRunList.stream().filter(p_run->p_run.getStatus().equals("Planned")).collect(Collectors.toList())))
                        .data_corresponding(of("ntms",ntMsServiceImplementation.ntmsList(50)))
                        .data_corresponding1(of("products",productServiceImplementation.productList(50)))
                        .data_corresponding2(of("production_daily_runs",productionRunList.stream().filter(p_run->p_run.getStatus().equals("Completed")).collect(Collectors.toList())))
                        .message("successfully retrieved all production runs")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }



    @PostMapping(path="/attachement/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerProductionMaterialUsage(@RequestPart("file") MultipartFile file,@PathVariable("id") Long id) throws JsonProcessingException {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_runs",productionRunServiceImplementation.saveProductionRunWithFile(file,id)))
                        .message("successfully created production run")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


        @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerMaterialUsage(@RequestBody ProductionRun productionRun) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_runs",productionRunServiceImplementation.saveProductionRun(productionRun)))
                        .message("successfully created production run")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteProductionRun(@PathVariable("id") Long id){

        boolean successfullyDeleted = productionRunServiceImplementation.deleteProductionRun(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_runs",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted production_run":"Error in deleting production_run, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @GetMapping(path = "retrieveFile/{name}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Resource> getAllByName(@PathVariable("name") String name) {

        FileDownloadUtil downloadUtil = new FileDownloadUtil();

        Resource resource = null;
        try {
            resource = downloadUtil.getFileAsResource(name);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            throw new RuntimeException("Could not list the files!");
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateMaterialUsage(@PathVariable("id") Long id,@RequestBody ProductionRun productionRun) {

        boolean updatedSuccessfully=productionRunServiceImplementation.updateProductionRun(id,productionRun);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_runs",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated productionRun":"Error in updating productionRun, Either productionRun is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
