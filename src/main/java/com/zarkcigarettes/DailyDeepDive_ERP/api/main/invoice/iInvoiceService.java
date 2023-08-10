package com.zarkcigarettes.DailyDeepDive_ERP.api.main.invoice;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Invoice;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface iInvoiceService {

    public void init();

    Collection<Invoice> invoiceList(Long invoiceID);

    Invoice save(Invoice invoice,MultipartFile file);
    boolean deleteInvoice(Long id);

    boolean updateInvoice(Long id, Invoice invoice,MultipartFile file);
}
