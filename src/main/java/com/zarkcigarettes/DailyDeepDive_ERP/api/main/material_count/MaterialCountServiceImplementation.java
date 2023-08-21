package com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_count;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
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
public class MaterialCountServiceImplementation implements iMaterialCountService {

    private final MaterialStockCountRepository materialStockCountRepository;
    private final MaterialCountRepository materialCountRepository;
    private final NTMsRepository ntMsRepository;
    private final ActivityLogService activityLogService;
    @Override
    public Collection<MaterialCount> materialCountList(Long materialStockCountID) {
        MaterialStockCount materialStockCount=      materialStockCountRepository.findById(materialStockCountID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product run with id %d not found", materialStockCountID)));
        return  materialCountRepository.findMaterialCountByMaterialStockCount(materialStockCount);
    }

    @Override
    public MaterialCount saveMaterialCount(MaterialCount materialCount) {
        NTMs details=      ntMsRepository.findById(materialCount.getNtMs_usage().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", materialCount.getNtMs_usage().getId())));

        //details.setQuantity(materialCount.getQuantity());

        activityLogService.addActivityLog("Added Material Stock Count of : "+materialCount.getNtMs_usage().getName() +" , quantity is "+materialCount.getQuantity()+" , of Product: "+materialCount.getProduct_usage().getName(),"Material Stock Count");


        return materialCountRepository.save(materialCount);
    }

    @Override
    public boolean deleteMaterialCount(Long id) {
            boolean exists = materialCountRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        MaterialCount materialCount = materialCountRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("materialCount with id %d not found", id)));

        activityLogService.addActivityLog("Deleted Material Count of : "+materialCount.getNtMs_usage().getName() +" , quantity "+materialCount.getQuantity()+" , of Product: "+materialCount.getProduct_usage().getName(),"Material Stock Count");


        materialCountRepository.deleteById(id);
            return  Boolean.TRUE;

    }


    @Override
    public boolean deleteMaterialCountByMaterialStockCount(MaterialStockCount materialStockCount) {

        Collection<MaterialCount> materialCounts= materialCountList(materialStockCount.getId());
        for(MaterialCount materialCount:materialCounts){
          if(materialCountRepository.existsById(materialCount.getId())){
              activityLogService.addActivityLog("Deleted Material Count of : "+materialCount.getNtMs_usage().getName() +" , quantity "+materialCount.getQuantity()+" , of Product: "+materialCount.getProduct_usage().getName(),"Material Stock Count");
              materialCountRepository.deleteById(materialCount.getId());
          }
        }
        return  Boolean.TRUE;

    }
@Override
    public boolean updateMaterialCount(Long id, MaterialCount materialCount) {
    MaterialCount details = materialCountRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("production material with id %d not found", id)));

    if (details.getDescription().length() > 0) {

        NTMs NTMsDetails=      ntMsRepository.findById(materialCount.getNtMs_usage().getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", materialCount.getNtMs_usage().getId())));

      //  double quantityNew=NTMsDetails.getQuantity()-details.getQuantity()+productionMaterialUsage.getQuantity();
      //  details.setQuantity(quantityNew);

        activityLogService.addActivityLog("Updated Stock Count : "+materialCount.getNtMs_usage().getName() +" , from quantity "+details.getQuantity()+" , to quantity "+materialCount.getQuantity()+" , of Product: "+materialCount.getProduct_usage().getName(),"Material Stock Count");

        details.setNtMs_usage(materialCount.getNtMs_usage());
        details.setProduct_usage(materialCount.getProduct_usage());
        details.setQuantity(materialCount.getQuantity());
        details.setDescription(materialCount.getDescription());
        details.setMaterialStockCount(materialCount.getMaterialStockCount());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
