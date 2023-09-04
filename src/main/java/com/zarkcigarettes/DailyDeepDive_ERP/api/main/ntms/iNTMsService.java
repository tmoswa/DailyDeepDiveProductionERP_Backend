package com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;

import java.util.Collection;

public interface iNTMsService {

    Collection<NTMs> ntmsList(int limit);
    Collection<NTMs> ntmList(int limit);
    NTMs saveNTMs(NTMs ntMs);
    boolean deleteNTMs(Long id);
    boolean updateNTMs(Long id, NTMs ntMs);

}
