package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.iProductService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
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
            return Integer.compare(o1.getNtMs_usage().getSequence(), o2.getNtMs_usage().getSequence());
        }
    };

    @Override
    public Collection<MaterialUsage> materialUsageList(Long productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", productID)));
        Collection<MaterialUsage> materialUsages = materialUsageRepository.findMaterialUsageByProduct(product);
        List<MaterialUsage> materialUsageList=new ArrayList<>(materialUsages);
         Collections.sort(materialUsageList, usageComparator);
        return materialUsageList;
    }

    @Override
    public MaterialUsage saveMaterialUsage(MaterialUsage materialUsage) {
        activityLogService.addActivityLog("Added Material Usage: " + materialUsage.getNtMs_usage().getName() + " ,for Product: " + materialUsage.getProduct_usage().getName() + " , Quantity: " + materialUsage.getQuantity(), "Material Usage");
        return materialUsageRepository.save(materialUsage);
    }

    @Override
    public boolean deleteMaterialUsage(Long id) {
        boolean exists = materialUsageRepository.existsById(id);
        if (!exists) {
            return Boolean.FALSE;
        }
        activityLogService.addActivityLog("Deleted Material Usage: " + materialUsageRepository.findById(id).get().getNtMs_usage().getName() + " ,for Product: " + materialUsageRepository.findById(id).get().getProduct_usage().getName() + " , Quantity: " + materialUsageRepository.findById(id).get().getQuantity(), "Material Usage");
        materialUsageRepository.deleteById(id);
        return Boolean.TRUE;

    }

    @Override
    public boolean updateMaterialUsage(Long id, MaterialUsage materialUsage) {
        MaterialUsage details = materialUsageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("material with id %d not found", id)));

        if (details.getDescription().length() > 0) {
            activityLogService.addActivityLog("Updated Material Usage: " + materialUsage.getNtMs_usage().getName() + " ,for Product: " + materialUsage.getProduct_usage().getName() + " , From Quantity: " + details.getQuantity() + " , To Quantity: " + materialUsage.getQuantity(), "Material Usage");

            details.setNtMs_usage(materialUsage.getNtMs_usage());
            details.setProduct_usage(materialUsage.getProduct_usage());
            details.setQuantity(materialUsage.getQuantity());
            details.setDescription(materialUsage.getDescription());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
