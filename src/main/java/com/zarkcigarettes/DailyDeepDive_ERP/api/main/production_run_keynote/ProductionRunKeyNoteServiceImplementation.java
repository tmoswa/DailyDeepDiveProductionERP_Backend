package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_keynote;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_material_usage.iProductionMaterialUsageService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.*;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.DailyProductionRunKeyNote;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionMaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;
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
public class ProductionRunKeyNoteServiceImplementation implements iProductionRunKeyNoteService {

    private final DailyProductionRunKeyNoteRepository productionRunKeyNoteRepository;
    private final ProductRepository productRepository;
    private final ProductionRunRepository productionRunRepository;

    private final ActivityLogService activityLogService;
    @Override
    public Collection<DailyProductionRunKeyNote> productionRunKeyNoteList(Long productionRunID) {
        ProductionRun productionRun=      productionRunRepository.findById(productionRunID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product run with id %d not found", productionRunID)));
        return  productionRunKeyNoteRepository.findDailyProductionRunKeyNoteByProductionRun(productionRun);
    }

    @Override
    public DailyProductionRunKeyNote saveDailyProductionRunKeyNote(DailyProductionRunKeyNote productionRunKeyNote) {
        activityLogService.addActivityLog("Added Production Run Key Note : "+productionRunKeyNote.getComments(),"Production Run Key Notes");

        return productionRunKeyNoteRepository.save(productionRunKeyNote);
    }

    @Override
    public boolean deleteDailyProductionRunKeyNote(Long id) {
            boolean exists = productionRunKeyNoteRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        activityLogService.addActivityLog("Deleted Production Run Key Note : "+productionRunKeyNoteRepository.findById(id).get().getComments(),"Production Run Key Notes");

        productionRunKeyNoteRepository.deleteById(id);
            return  Boolean.TRUE;

    }


    @Override
    public boolean deleteDailyProductionRunKeyNoteByProductionRun(ProductionRun productionRun) {

        Collection<DailyProductionRunKeyNote> dailyProductionRunKeyNotes= productionRunKeyNoteList(productionRun.getId());
        for(DailyProductionRunKeyNote dailyProductionRunKeyNote:dailyProductionRunKeyNotes){
          if(productionRunKeyNoteRepository.existsById(dailyProductionRunKeyNote.getId())){
              activityLogService.addActivityLog("Deleted Production Run Key Note : "+dailyProductionRunKeyNote.getComments(),"Production Run Key Notes");

              productionRunKeyNoteRepository.deleteById(dailyProductionRunKeyNote.getId());
          }
        }
        return  Boolean.TRUE;

    }
@Override
    public boolean updateDailyProductionRunKeyNote(Long id, DailyProductionRunKeyNote dailyProductionRunKeyNote) {
    DailyProductionRunKeyNote details = productionRunKeyNoteRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("production Run Key Note with id %d not found", id)));

    if (details.getComments().length() > 0) {
        activityLogService.addActivityLog("Deleted Production Run Key Note : "+dailyProductionRunKeyNote.getComments(),"Production Run Key Notes");

        details.setProductionRun(dailyProductionRunKeyNote.getProductionRun());
        details.setProduct_usage(dailyProductionRunKeyNote.getProduct_usage());
        details.setProduction_line_and_target(dailyProductionRunKeyNote.getProduction_line_and_target());
        details.setRepresentatives_and_technician(dailyProductionRunKeyNote.getRepresentatives_and_technician());
        details.setComments(dailyProductionRunKeyNote.getComments());
        details.setRag_used(dailyProductionRunKeyNote.getRag_used());
        details.setMoisture_content(dailyProductionRunKeyNote.getMoisture_content());
        details.setAverage_cigarette_weight(dailyProductionRunKeyNote.getAverage_cigarette_weight());
        details.setLoose_end_testing(dailyProductionRunKeyNote.getLoose_end_testing());
        details.setDiameter_testing(dailyProductionRunKeyNote.getDiameter_testing());
        details.setFilter_size_stick_alignment(dailyProductionRunKeyNote.getFilter_size_stick_alignment());
        details.setSpeed_of_maker_against_waste(dailyProductionRunKeyNote.getSpeed_of_maker_against_waste());
        details.setTightness_of_bopp_for_pack(dailyProductionRunKeyNote.getTightness_of_bopp_for_pack());
        details.setOpen_3_random_bricks(dailyProductionRunKeyNote.getOpen_3_random_bricks());
        details.setCorrect_placement_of_aluminium_foil(dailyProductionRunKeyNote.getCorrect_placement_of_aluminium_foil());
        details.setWhiteness_of_white_inners(dailyProductionRunKeyNote.getWhiteness_of_white_inners());
        details.setGluing_of_inner_top_flap(dailyProductionRunKeyNote.getGluing_of_inner_top_flap());
        details.setPositioning_of_customer_comments(dailyProductionRunKeyNote.getPositioning_of_customer_comments());
        details.setCorrect_lettering_and_positioning_on_the_pack(dailyProductionRunKeyNote.getCorrect_lettering_and_positioning_on_the_pack());
        details.setSmoke_tests(dailyProductionRunKeyNote.getSmoke_tests());
        details.setTipping_used(dailyProductionRunKeyNote.getTipping_used());
        details.setBlanks_used(dailyProductionRunKeyNote.getBlanks_used());
        details.setFilters_used(dailyProductionRunKeyNote.getFilters_used());
        details.setQuantity_produced(dailyProductionRunKeyNote.getQuantity_produced());


        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
