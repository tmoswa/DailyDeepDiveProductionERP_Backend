package com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.increaments;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Increaments;

import java.util.Collection;

public interface iIncreamentsService {


    boolean updateIncreaments(Long id, Increaments increaments);
    Increaments getIncreaments();
}
