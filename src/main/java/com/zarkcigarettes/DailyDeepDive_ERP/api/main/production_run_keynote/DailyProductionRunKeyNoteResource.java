package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_keynote;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage.ProductionRunMaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.DailyProductionRunKeyNoteRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionMaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.DailyProductionRunKeyNote;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
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
@RequestMapping("api/production-run-keyNote")
public class DailyProductionRunKeyNoteResource {

   private final DailyProductionRunKeyNoteRepository dailyProductionRunKeyNoteRepository;
    private final ProductionRunKeyNoteServiceImplementation productionRunKeyNoteServiceImplementation;
    @Autowired
    public DailyProductionRunKeyNoteResource(DailyProductionRunKeyNoteRepository dailyProductionRunKeyNoteRepository,
                                             ProductionRunKeyNoteServiceImplementation productionRunKeyNoteServiceImplementation) {
        this.dailyProductionRunKeyNoteRepository = dailyProductionRunKeyNoteRepository;
        this.productionRunKeyNoteServiceImplementation=productionRunKeyNoteServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllProductionRunKeyNotes(@PathVariable("id") Long productionRunID) {

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_run_keynotes",productionRunKeyNoteServiceImplementation.productionRunKeyNoteList(productionRunID)))
                        .message("successfully retrieved all production run key notes")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerProductionRunKeyNote(@RequestBody DailyProductionRunKeyNote dailyProductionRunKeyNote) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_run_keynote",productionRunKeyNoteServiceImplementation.saveDailyProductionRunKeyNote(dailyProductionRunKeyNote)))
                        .message("successfully created product")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteProductionRunKeyNote(@PathVariable("id") Long id){

        boolean successfullyDeleted = productionRunKeyNoteServiceImplementation.deleteDailyProductionRunKeyNote(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_run_keynote",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted production_run_keynote":"Error in deleting production_run_keynote, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateProductionRunKeyNote(@PathVariable("id") Long id,@RequestBody DailyProductionRunKeyNote dailyProductionRunKeyNote) {

        boolean updatedSuccessfully=productionRunKeyNoteServiceImplementation.updateDailyProductionRunKeyNote(id,dailyProductionRunKeyNote);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("production_run_keynote",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated production_run_keynote":"Error in updating production_run_keynote, Either production_run_keynote is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
