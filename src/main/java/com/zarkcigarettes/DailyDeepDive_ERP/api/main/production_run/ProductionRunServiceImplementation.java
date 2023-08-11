package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.iMaterialUsageService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_keynote.ProductionRunKeyNoteServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage.ProductionRunMaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionMaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionRunRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductionRunServiceImplementation implements iProductionRunService {

    private final ProductionRunRepository productionRunRepository;
    private final ProductRepository productRepository;
    private final ProductionRunMaterialUsageServiceImplementation productionRunMaterialUsageServiceImplementation;
    private final ProductionRunKeyNoteServiceImplementation productionRunKeyNoteServiceImplementation;

    private final ActivityLogService activityLogService;
    private final Path root = Paths.get("uploads/");
    @Override
    public Collection<ProductionRun> productionRunList(int limit) {
        return productionRunRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Collection<ProductionRun> productionRunList(Long productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", productID)));
        return productionRunRepository.findProductionRunByProduct(product);
    }


    @Override
    public ProductionRun saveProductionRun(ProductionRun productionRun) {

        Product product = productRepository.findById(productionRun.getProduct_production_run().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", productionRun.getProduct_production_run().getId())));
        if (productionRun.getStatus().equals("Completed")) {
            double quantityNew = product.getQuantity() + productionRun.getQuantity();
            product.setQuantity(quantityNew);
        }
        activityLogService.addActivityLog("Added Production Run of : "+productionRun.getProduct_production_run().getName() +" , of quantity "+productionRun.getQuantity(),"Production Run");

        return productionRunRepository.save(productionRun);
    }

    public ProductionRun saveProductionRunWithFile(MultipartFile file, Long id) {

    LocalDateTime currentDateAndTime = LocalDateTime.now();

        try{
        Files.copy(file.getInputStream(), this.root.resolve(currentDateAndTime + "_" + file.getOriginalFilename()));
    } catch(Exception e){
        throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }

        boolean exists = productionRunRepository.existsById(id);

        if (exists) {

        }

        ProductionRun details = productionRunRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ProductionRun with id %d not found", id)));
        details.setName(file.getOriginalFilename());
        details.setUrl(currentDateAndTime+"_"+file.getOriginalFilename());

         updateProductionRun(id, details);

        return details;


}

    @Override
    public boolean deleteProductionRun(Long id) {

            boolean exists = productionRunRepository.existsById(id);

            if (!exists) {
                return  Boolean.FALSE;
            }

        ProductionRun details = productionRunRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ProductionRun with id %d not found", id)));

        Product product=      productRepository.findById(details.getProduct_production_run().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", details.getId())));


        activityLogService.addActivityLog("Deleted Production Run of : "+details.getProduct_production_run().getName() +" , of quantity "+details.getQuantity()+" , dated"+ details.getFrom_date(),"Production Run");

        if(details.getStatus().equals("Completed")){
            double quantityNew=product.getQuantity()-details.getQuantity();
            product.setQuantity(quantityNew);

            boolean deleteProductionRunKeyNote=productionRunKeyNoteServiceImplementation.deleteDailyProductionRunKeyNoteByProductionRun(details);

            boolean deleteMaterialContents=productionRunMaterialUsageServiceImplementation.deleteProductionMaterialUsageByProductionRun(details);

            if(deleteMaterialContents){
                productionRunRepository.deleteById(id);
                return  Boolean.TRUE;
            }else{
                return  Boolean.FALSE;
            }
        }
        else{
                productionRunRepository.deleteById(id);
                return  Boolean.TRUE;
        }




    }
@Override
    public boolean updateProductionRun(Long id, ProductionRun productionRun) {
    ProductionRun details = productionRunRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ProductionRun with id %d not found", id)));

    if (details.getDescription().length() > 0) {

        activityLogService.addActivityLog("Updated Production Run of : "+details.getProduct_production_run().getName() +" , of quantity "+details.getQuantity()+" , dated"+ details.getFrom_date(),"Production Run");

        Product product=      productRepository.findById(details.getProduct_production_run().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", details.getId())));

        double quantityNew=product.getQuantity()-details.getQuantity()+productionRun.getQuantity();
        product.setQuantity(quantityNew);

        details.setProduct_production_run(productionRun.getProduct_production_run());
        details.setStatus(productionRun.getStatus());
        details.setQuantity(productionRun.getQuantity());
        details.setDescription(productionRun.getDescription());
        details.setSummary_comments(productionRun.getSummary_comments());
        details.setFrom_date(productionRun.getFrom_date());
        details.setFrom_date(productionRun.getTo_date());
        details.setName(productionRun.getName());
        details.setUrl(productionRun.getUrl());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
