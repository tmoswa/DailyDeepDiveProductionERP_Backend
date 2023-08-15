package com.zarkcigarettes.DailyDeepDive_ERP.api.main.invoice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.CurrencyServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type.EntityTypeServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.FileDownloadUtil;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.MainEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.NTMsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.purchase_order.PurchaseOrderServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Invoice;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/invoice")
@CrossOrigin(origins = "https://production.zarkcigarettes.com")
public class InvoiceResource {

    private final InvoiceServiceImplementation invoiceServiceImplementation;
    private final PurchaseOrderServiceImplementation purchaseOrderServiceImplementation;
    private final EntityTypeServiceImplementation entityTypeServiceImplementation;
    private final MainEntityServiceImplementation mainEntityServiceImplementation;
    private final CurrencyServiceImplementation currencyServiceImplementation;
    @Autowired
    public InvoiceResource(InvoiceServiceImplementation invoiceServiceImplementation, PurchaseOrderServiceImplementation purchaseOrderServiceImplementation,
                           EntityTypeServiceImplementation entityTypeServiceImplementation, MainEntityServiceImplementation mainEntityServiceImplementation,CurrencyServiceImplementation currencyServiceImplementation) {
        this.invoiceServiceImplementation= invoiceServiceImplementation;
        this.purchaseOrderServiceImplementation=purchaseOrderServiceImplementation;
        this.entityTypeServiceImplementation=entityTypeServiceImplementation;
        this.mainEntityServiceImplementation=mainEntityServiceImplementation;
        this.currencyServiceImplementation=currencyServiceImplementation;
    }


    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Response> getAllInvoices(@PathVariable("id") Long entityID) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("invoices",invoiceServiceImplementation.invoiceList(entityID)))
                        .data_corresponding(of("purchase_orders",purchaseOrderServiceImplementation.purchaseOrderList(entityID)))
                        .data_corresponding1(of("currencies",currencyServiceImplementation.currencyList(50)))
                        .message("successfully retrieved all invoices")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping(path = "retrieveFile/{name}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-READ')")
    public ResponseEntity<Resource> getAllInvByName(@PathVariable("name") String name) {

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> registerInvoice(@RequestPart("file") MultipartFile file, @RequestPart(name = "InvDetails") String InvDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Invoice invoice= objectMapper.readValue(InvDetails, Invoice.class);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                      .data(of("invoices",invoiceServiceImplementation.save(invoice,file)))
                        .message("successfully created invoice")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteInvoice(@PathVariable("id") Long id){

        boolean successfullyDeleted = invoiceServiceImplementation.deleteInvoice(id);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("invoices",successfullyDeleted))
                        .message(successfullyDeleted?"successfully deleted invoice":"Error in invoice, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PutMapping(path = "update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-ALTER')")
    public ResponseEntity<Response> updateInvoice(@PathVariable("id") Long id,@RequestPart("file") MultipartFile file, @RequestPart(name = "InvDetails") String InvDetails) {
        boolean updatedSuccessfully=false;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            Invoice invoice= objectMapper.readValue(InvDetails, Invoice.class);
             updatedSuccessfully=invoiceServiceImplementation.updateInvoice(id,invoice,file);

        }catch (JsonProcessingException exception){
        }

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("invoices",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated invoice":"Error in updating invoice, Either invoice is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


}
