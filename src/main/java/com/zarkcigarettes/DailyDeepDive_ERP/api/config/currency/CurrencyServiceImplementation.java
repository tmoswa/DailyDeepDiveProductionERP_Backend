package com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.CurrencyRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
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
public class CurrencyServiceImplementation implements iCurrencyService {

    private final CurrencyRepository currencyRepository;
    @Override
    public Collection<Currency> currencyList(int limit) {
        return  currencyRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Currency saveCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public boolean deleteCurrency(Long id) {
            boolean exists = currencyRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        currencyRepository.deleteById(id);
            return  Boolean.TRUE;

    }
@Override
    public boolean updateCurrency(Long id, Currency currency) {
    Currency details = currencyRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("currency with id %d not found", id)));

    if (details.getName().length() > 0) {
        details.setName(currency.getName());
        details.setSymbol(currency.getSymbol());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
