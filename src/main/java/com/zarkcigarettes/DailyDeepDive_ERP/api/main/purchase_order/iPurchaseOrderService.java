package com.zarkcigarettes.DailyDeepDive_ERP.api.main.purchase_order;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface iPurchaseOrderService {

    public void init();

    Collection<PurchaseOrder> purchaseOrderList(Long entityID);

    PurchaseOrder save(PurchaseOrder purchaseOrder,MultipartFile file);
    boolean deletePurchaseOrder(Long id);

    boolean updatePurchaseOrder(Long id, PurchaseOrder purchaseOrder,MultipartFile file);
    boolean deliverPurchaseOrder(Long id, PurchaseOrder purchaseOrder);

    public Collection<PurchaseOrder> totalPurchaseOrderList(int limit);
}
