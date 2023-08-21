package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_stock_count;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_count.MaterialCountServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run.iProductionRunService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_keynote.ProductionRunKeyNoteServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage.ProductionRunMaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MaterialStockCountRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionRunRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialStockCount;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaterialStockCountServiceImplementation implements iMaterialStockCountService {

    private final MaterialStockCountRepository materialStockCountRepository;
    private final ProductRepository productRepository;
    private final MaterialCountServiceImplementation stockCountServiceImplementation;

    private final ActivityLogService activityLogService;
    private final Path root = Paths.get("uploads/");
    @Override
    public Collection<MaterialStockCount> materialStockCountList(int limit) {
        return materialStockCountRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Collection<MaterialStockCount> materialStockCountList(Long productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", productID)));
        return materialStockCountRepository.findMaterialStockCountByProduct(product);
    }


    @Override
    public MaterialStockCount saveMaterialStockCount(MaterialStockCount materialStockCount) {

        Product product = productRepository.findById(materialStockCount.getProduct_production_run().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", materialStockCount.getProduct_production_run().getId())));

        activityLogService.addActivityLog("Added Material Stock Count of Product materials: "+materialStockCount.getProduct_production_run().getName() ,"Material Stock Count");

        return materialStockCountRepository.save(materialStockCount);
    }



    @Override
    public boolean deleteMaterialStockCount(Long id) {

            boolean exists = materialStockCountRepository.existsById(id);

            if (!exists) {
                return  Boolean.FALSE;
            }

        MaterialStockCount details = materialStockCountRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Material Stock Count with id %d not found", id)));

        Product product=      productRepository.findById(details.getProduct_production_run().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", details.getId())));

        activityLogService.addActivityLog("Deleted Material Stock Count dated : "+details.getCount_date() ,"Material Stock Count");

        boolean deleteMaterialContents=stockCountServiceImplementation.deleteMaterialCountByMaterialStockCount(details);

        if(deleteMaterialContents){
            materialStockCountRepository.deleteById(id);
            return  Boolean.TRUE;
        }else{
            return  Boolean.FALSE;
        }




    }
@Override
    public boolean updateMaterialStockCount(Long id, MaterialStockCount materialStockCount) {
    MaterialStockCount details = materialStockCountRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("MaterialStockCount with id %d not found", id)));

    if (details.getProduct_production_run().getName().length() > 0) {

        activityLogService.addActivityLog("Updated Material Stock Count of : "+details.getProduct_production_run().getName() +" , dated"+ details.getCount_date(),"Material Stock Count");



        details.setProduct_production_run(materialStockCount.getProduct_production_run());
        details.setCount_date(materialStockCount.getCount_date());
        details.setSummary_comments(materialStockCount.getSummary_comments());

        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
