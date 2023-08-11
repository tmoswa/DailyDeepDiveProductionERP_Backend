package com.zarkcigarettes.DailyDeepDive_ERP.api.main.finished_product_movement;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;

import java.util.Collection;

public interface iFinishedProductMovementService {
    Collection<FinishedProductMovement> finishedProductMovementList(Long ProdutID);
    FinishedProductMovement saveFinishedProductMovement(FinishedProductMovement finishedProductMovement);
    boolean deleteFinishedProductMovement(Long id);
    boolean updateFinishedProductMovement(Long id, FinishedProductMovement finishedProductMovement);
}
