package com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.increaments;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.CurrencyRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.IncreamentsRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Increaments;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class IncreamentsServiceImplementation implements iIncreamentsService {

    private final IncreamentsRepository increamentsRepository;

@Override
    public boolean updateIncreaments(Long id, Increaments increaments) {
    Increaments details = increamentsRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("increaments with id %d not found", id)));

    if (details.getProduction_inc() > 0) {
        details.setProduction_inc(increaments.getProduction_inc());
        details.setInvoice_inc(increaments.getInvoice_inc());
        details.setProduction_inc(increaments.getProduction_inc());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }

    @Override
    public Increaments getIncreaments() {
        return  increamentsRepository.findById(1L)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("increaments with id 1 not found")));
    }
}
