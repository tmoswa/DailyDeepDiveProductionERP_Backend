package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.iMaterialUsageService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.*;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductionRunMaterialUsageServiceImplementation implements iProductionMaterialUsageService {

    private final MaterialUsageRepository materialUsageRepository;
    private final ProductRepository productRepository;
    private final ProductionRunRepository productionRunRepository;
    private final ProductionMaterialUsageRepository productionMaterialUsageRepository;
    private final NTMsRepository ntMsRepository;
    @Override
    public Collection<ProductionMaterialUsage> productionMaterialUsageList(Long productionRunID) {
        ProductionRun productionRun=      productionRunRepository.findById(productionRunID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product run with id %d not found", productionRunID)));
        return  productionMaterialUsageRepository.findProductionMaterialUsageByProductionRun(productionRun);
    }

    @Override
    public ProductionMaterialUsage saveProductionMaterialUsage(ProductionMaterialUsage productionMaterialUsage) {
        NTMs details=      ntMsRepository.findById(productionMaterialUsage.getNtMs_usage().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", productionMaterialUsage.getNtMs_usage().getId())));

        double quantityNew=details.getQuantity()-productionMaterialUsage.getQuantity();
        details.setQuantity(quantityNew);

        return productionMaterialUsageRepository.save(productionMaterialUsage);
    }

    @Override
    public boolean deleteProductionMaterialUsage(Long id) {
            boolean exists = productionMaterialUsageRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        ProductionMaterialUsage productionMaterialUsage = productionMaterialUsageRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ProductionRun with id %d not found", id)));

        NTMs details=      ntMsRepository.findById(productionMaterialUsage.getNtMs_usage().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", productionMaterialUsage.getNtMs_usage().getId())));

        double quantityNew=details.getQuantity()+productionMaterialUsage.getQuantity();
        details.setQuantity(quantityNew);

        productionMaterialUsageRepository.deleteById(id);
            return  Boolean.TRUE;

    }


    @Override
    public boolean deleteProductionMaterialUsageByProductionRun(ProductionRun productionRun) {

        Collection<ProductionMaterialUsage> productionMaterialUsages= productionMaterialUsageList(productionRun.getId());
        for(ProductionMaterialUsage productionMaterialUsage:productionMaterialUsages){
          if(productionMaterialUsageRepository.existsById(productionMaterialUsage.getId())){
              productionMaterialUsageRepository.deleteById(productionMaterialUsage.getId());
          }
        }
        return  Boolean.TRUE;

    }
@Override
    public boolean updateProductionMaterialUsage(Long id, ProductionMaterialUsage productionMaterialUsage) {
    ProductionMaterialUsage details = productionMaterialUsageRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("production material with id %d not found", id)));

    if (details.getDescription().length() > 0) {

        NTMs NTMsDetails=      ntMsRepository.findById(productionMaterialUsage.getNtMs_usage().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", productionMaterialUsage.getNtMs_usage().getId())));

        double quantityNew=NTMsDetails.getQuantity()-details.getQuantity()+productionMaterialUsage.getQuantity();
        details.setQuantity(quantityNew);


        details.setNtMs_usage(productionMaterialUsage.getNtMs_usage());
        details.setProduct_usage(productionMaterialUsage.getProduct_usage());
        details.setQuantity(productionMaterialUsage.getQuantity());
        details.setDescription(productionMaterialUsage.getDescription());
        details.setProductionRun(productionMaterialUsage.getProductionRun());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
