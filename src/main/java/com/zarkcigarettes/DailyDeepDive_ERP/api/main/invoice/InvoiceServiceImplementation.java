package com.zarkcigarettes.DailyDeepDive_ERP.api.main.invoice;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.increaments.IncreamentsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.purchase_order.iPurchaseOrderService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.InvoiceRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MainEntityRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.PurchaseOrderRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Increaments;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Invoice;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InvoiceServiceImplementation implements iInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MainEntityRepository mainEntityRepository;
    private final IncreamentsServiceImplementation increamentsServiceImplementation;

    private final Path root = Paths.get("uploads/");
    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {

                Files.createDirectory(root);
                System.out.println("Directory created");
            } else {

                System.out.println("Directory already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Collection<Invoice> invoiceList(Long entityID) {
        MainEntity mainEntity=      mainEntityRepository.findById(entityID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("main entity with id %d not found", entityID)));
        return  invoiceRepository.findInvoiceByMainEntity(mainEntity);
    }



    @Override
    public Invoice save(Invoice invoice,MultipartFile file) {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        init();
        try {
            Files.copy(file.getInputStream(), this.root.resolve(currentDateAndTime+"_"+file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        invoice.setName(file.getOriginalFilename());
        invoice.setUrl(currentDateAndTime+"_"+file.getOriginalFilename());

        Increaments increaments=increamentsServiceImplementation.getIncreaments();
        increaments.setInvoice_inc(increaments.getInvoice_inc()+1);
        invoice.setInvoice_number(increaments.getInvoice_inc());
        increamentsServiceImplementation.updateIncreaments(1L,increaments);
        return invoiceRepository.save(invoice);
    }

    @Override
    public boolean deleteInvoice(Long id) {
            boolean exists = invoiceRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        invoiceRepository.deleteById(id);
            return  Boolean.TRUE;

    }
@Override
    public boolean updateInvoice(Long id, Invoice invoice,MultipartFile file) {

    LocalDateTime currentDateAndTime = LocalDateTime.now();
    init();
    try {
        Files.copy(file.getInputStream(), this.root.resolve(currentDateAndTime+"_"+file.getOriginalFilename()));
    } catch (Exception e) {
        throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }



    Invoice details = invoiceRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("purchase order with id %d not found", id)));

    if (details.getName().length() > 0) {
        details.setName(file.getOriginalFilename());
        details.setUrl(currentDateAndTime+"_"+file.getOriginalFilename());
        details.setDescription(invoice.getDescription());
        details.setAmount(invoice.getAmount());
        details.setInvoice_number(invoice.getInvoice_number());
        details.setMain_entity_inv(invoice.getMain_entity_inv());
        details.setStatus(invoice.getStatus());
        details.setPurchaseOrder(invoice.getPurchaseOrder());
        details.setManual_invoice_number(invoice.getManual_invoice_number());
        details.setInvoice_currency(invoice.getInvoice_currency());

        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
