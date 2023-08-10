package com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByName(String name);
    Currency findBySymbol(String name);
    @Override
    void delete(Currency currency);

    Optional<Currency> findCurrencyByName(String name);
}
