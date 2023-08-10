package com.zarkcigarettes.DailyDeepDive_ERP.api.main.purchase_order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type.EntityTypeServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.FileDownloadUtil;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.MainEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/purchase-order")
@CrossOrigin(origins = "http://production.zarkcigarettes.com")
@Slf4j
public class PurchaseOrderResource {

    private final PurchaseOrderServiceImplementation purchaseOrderServiceImplementation;
    private final NTMsServiceImplementation ntMsServiceImplementation;
    private final EntityTypeServiceImplementation entityTypeServiceImplementation;
    private final MainEntityServiceImplementation mainEntityServiceImplementation;
    @Autowired
    public PurchaseOrderResource(PurchaseOrderServiceImplementation purchaseOrderServiceImplementation,NTMsServiceImplementation ntMsServiceImplementation,
                                 EntityTypeServiceImplementation entityTypeServiceImplementation, MainEntityServiceImplementation mainEntityServiceImplementation) {
        this.purchaseOrderServiceImplementation = purchaseOrderServiceImplementation;
        this.ntMsServiceImplementation=ntMsServiceImplementation;
        this.entityTypeServiceImplementation=entityTypeServiceImplementation;
        this.mainEntityServiceImplementation=mainEntityServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllPOs(@PathVariable("id") Long entityID) {
        EntityType entityType=entityTypeServiceImplementation.entityTypeByName("Supplier");
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("purchase_orders",purchaseOrderServiceImplementation.purchaseOrderList(entityID)))
                        .data_corresponding(of("suppliers",mainEntityServiceImplementation.mainEntityListByEntityType(entityType)))
                        .message("successfully retrieved all purchaseOrders")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping(path = "retrieveFile/{name}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Resource> getAllPOByName(@PathVariable("name") String name) {

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

    @GetMapping(path = "/delayed")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getDelayedPurchaseOrder() {
        Collection<PurchaseOrder> purchaseOrders=purchaseOrderServiceImplementation.totalPurchaseOrderList(90000).stream().filter(ds_po->ds_po.getDelivery_date().isBefore(LocalDate.now()))
                .collect(Collectors.toList()).stream().filter(d_po->d_po.getStatus().equals("Initiated")).collect(Collectors.toList());

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("delayedPurchaseOrders",purchaseOrders))
                        .message("successfully retrieved all production runs")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping(path = "/delivered/{fro}/{tto}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getDeliveredPurchaseOrder(@PathVariable("fro") String fro,@PathVariable("tto") String tto) {
        LocalDate from=LocalDate.parse(fro).minusDays(1);
        LocalDate to=LocalDate.parse(tto).plusDays(1);


        Collection<PurchaseOrder> purchaseOrders=purchaseOrderServiceImplementation.totalPurchaseOrderList(90000).
                stream().filter(ds_po->ds_po.getStatus().equals("Delivered"))
                .collect(Collectors.toList()).stream().filter(d_po->(d_po.getDelivery_date().isAfter(from) && d_po.getDelivery_date().isBefore(to))).collect(Collectors.toList());

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("deliveredPurchaseOrders",purchaseOrders))
                        .message("successfully retrieved all delivered Purchase Orders")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerPurchaseOrder(@RequestPart("file") MultipartFile file, @RequestPart(name = "PODetails") String PODetails) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        PurchaseOrder purchaseOrder = objectMapper.readValue(PODetails, PurchaseOrder.class);
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                      .data(of("purchaseOrder",purchaseOrderServiceImplementation.save(purchaseOrder,file)))
                        .message("successfully created purchaseOrder")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deletePurchaseOrder(@PathVariable("id") Long id){

        boolean successfullyDeleted = purchaseOrderServiceImplementation.deletePurchaseOrder(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("purchaseOrder",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted purchaseOrder":"Error in purchaseOrder NTM, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updatePurchaseOrder(@PathVariable("id") Long id,@RequestPart("file") MultipartFile file, @RequestPart(name = "PODetails") String PODetails) {
        boolean updatedSuccessfully=false;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            PurchaseOrder purchaseOrder = objectMapper.readValue(PODetails, PurchaseOrder.class);
             updatedSuccessfully=purchaseOrderServiceImplementation.updatePurchaseOrder(id,purchaseOrder,file);

        }catch (JsonProcessingException exception){
        }

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("purchaseOrder",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated purchaseOrder":"Error in updating purchaseOrder, Either purchaseOrder is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping(path = "deliver/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> deliverPurchaseOrder(@PathVariable("id") Long id, @RequestBody PurchaseOrder purchaseOrder) {
        boolean updatedSuccessfully=purchaseOrderServiceImplementation.deliverPurchaseOrder(id,purchaseOrder);


        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("purchaseOrder",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated purchaseOrder":"Error in updating purchaseOrder, Either purchaseOrder is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
