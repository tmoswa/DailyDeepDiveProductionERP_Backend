package com.softpala.SalesDeepDive_ERP.api.main.invoice;


import com.softpala.SalesDeepDive_ERP.persistence.model.Invoice;
import com.softpala.SalesDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface iInvoiceService {

    public void init();

    Collection<Invoice> invoiceList(Long invoiceID);

    Invoice save(Invoice invoice,MultipartFile file);
    boolean deleteInvoice(Long id);

    boolean updateInvoice(Long id, Invoice invoice,MultipartFile file);
}
