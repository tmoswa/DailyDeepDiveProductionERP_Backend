package com.softpala.SalesDeepDive_ERP.api.main.material_usage;

import com.softpala.SalesDeepDive_ERP.api.main.inc.ActivityLogService;
import com.softpala.SalesDeepDive_ERP.api.main.product.iProductService;
import com.softpala.SalesDeepDive_ERP.persistence.dao.MaterialUsageRepository;
import com.softpala.SalesDeepDive_ERP.persistence.dao.ProductRepository;
import com.softpala.SalesDeepDive_ERP.persistence.model.MainEntity;
import com.softpala.SalesDeepDive_ERP.persistence.model.MaterialUsage;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;
import com.softpala.SalesDeepDive_ERP.persistence.model.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaterialUsageServiceImplementation implements iMaterialUsageService {

    private final MaterialUsageRepository materialUsageRepository;
    private final ProductRepository productRepository;
    private final ActivityLogService activityLogService;

    public static Comparator<MaterialUsage> usageComparator = new Comparator<MaterialUsage>() {
        @Override
        public int compare(MaterialUsage o1, MaterialUsage o2) {
            Integer s1 = o1 != null && o1.getRawMaterialUsage() != null ? o1.getRawMaterialUsage().getSequence() : null;
            Integer s2 = o2 != null && o2.getRawMaterialUsage() != null ? o2.getRawMaterialUsage().getSequence() : null;
            if (s1 == null && s2 == null) {
                return 0;
            }
            if (s1 == null) {
                return 1;
            }
            if (s2 == null) {
                return -1;
            }
            return Integer.compare(s1, s2);
        }
    };

    @Override
    public Collection<MaterialUsage> materialUsageList(Long productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", productID)));
        Collection<MaterialUsage> materialUsages = materialUsageRepository.findMaterialUsageByProduct(product);
        List<MaterialUsage> materialUsageList = new ArrayList<>();
        for (MaterialUsage materialUsage : materialUsages) {
            if (materialUsage.getRawMaterialUsage() != null) {
                materialUsageList.add(materialUsage);
            } else {
                log.warn("Skipping material usage {} because raw material reference is missing", materialUsage.getId());
            }
        }
        Collections.sort(materialUsageList, usageComparator);
        return materialUsageList;
    }

    @Override
    public MaterialUsage saveMaterialUsage(MaterialUsage materialUsage) {
        activityLogService.addActivityLog("Added Material Usage: " + materialUsage.getRawMaterialUsage().getName() + " ,for Product: " + materialUsage.getProduct_usage().getName() + " , Quantity: " + materialUsage.getQuantity(), "Material Usage");
        return materialUsageRepository.save(materialUsage);
    }

    @Override
    public boolean deleteMaterialUsage(Long id) {
        boolean exists = materialUsageRepository.existsById(id);
        if (!exists) {
            return Boolean.FALSE;
        }
        activityLogService.addActivityLog("Deleted Material Usage: " + materialUsageRepository.findById(id).get().getRawMaterialUsage().getName() + " ,for Product: " + materialUsageRepository.findById(id).get().getProduct_usage().getName() + " , Quantity: " + materialUsageRepository.findById(id).get().getQuantity(), "Material Usage");
        materialUsageRepository.deleteById(id);
        return Boolean.TRUE;

    }

    @Override
    public boolean updateMaterialUsage(Long id, MaterialUsage materialUsage) {
        MaterialUsage details = materialUsageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("material with id %d not found", id)));

        if (details.getDescription().length() > 0) {
            activityLogService.addActivityLog("Updated Material Usage: " + materialUsage.getRawMaterialUsage().getName() + " ,for Product: " + materialUsage.getProduct_usage().getName() + " , From Quantity: " + details.getQuantity() + " , To Quantity: " + materialUsage.getQuantity(), "Material Usage");

            details.setRawMaterialUsage(materialUsage.getRawMaterialUsage());
            details.setProduct_usage(materialUsage.getProduct_usage());
            details.setQuantity(materialUsage.getQuantity());
            details.setDescription(materialUsage.getDescription());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
