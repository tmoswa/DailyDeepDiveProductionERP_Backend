package com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run_keynote;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.DailyProductionRunKeyNote;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionMaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ProductionRun;

import java.util.Collection;

public interface iProductionRunKeyNoteService {
    Collection<DailyProductionRunKeyNote> productionRunKeyNoteList(Long productionRunID);
    DailyProductionRunKeyNote saveDailyProductionRunKeyNote(DailyProductionRunKeyNote dailyProductionRunKeyNote);
    boolean deleteDailyProductionRunKeyNote(Long id);
    boolean updateDailyProductionRunKeyNote(Long id, DailyProductionRunKeyNote productionRunKeyNote);

    boolean deleteDailyProductionRunKeyNoteByProductionRun(ProductionRun productionRun);
}
