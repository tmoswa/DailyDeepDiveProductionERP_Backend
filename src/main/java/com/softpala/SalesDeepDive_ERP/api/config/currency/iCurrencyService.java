package com.softpala.SalesDeepDive_ERP.api.config.currency;


import com.softpala.SalesDeepDive_ERP.persistence.model.Currency;

import java.util.Collection;

public interface iCurrencyService {

    Collection<Currency> currencyList(int limit);

    Currency saveCurrency(Currency currency);

    boolean deleteCurrency(Long id);

    boolean updateCurrency(Long id, Currency currency);
}
