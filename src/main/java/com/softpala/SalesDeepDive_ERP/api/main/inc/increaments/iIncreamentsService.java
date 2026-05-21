package com.softpala.SalesDeepDive_ERP.api.main.inc.increaments;


import com.softpala.SalesDeepDive_ERP.persistence.model.Currency;
import com.softpala.SalesDeepDive_ERP.persistence.model.Increaments;

import java.util.Collection;

public interface iIncreamentsService {


    boolean updateIncreaments(Long id, Increaments increaments);
    Increaments getIncreaments();
}
