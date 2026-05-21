package com.softpala.SalesDeepDive_ERP.api.main.main_entity;


import com.softpala.SalesDeepDive_ERP.persistence.model.EntityType;
import com.softpala.SalesDeepDive_ERP.persistence.model.MainEntity;

import java.util.Collection;

public interface iMainEntityService {

    Collection<MainEntity> mainEntityList(int limit);

    MainEntity saveMainEntity(MainEntity mainEntity);

    boolean deleteMainEntity(Long id);

    boolean updateMainEntity(Long id, MainEntity mainEntity);
    Collection<MainEntity> mainEntityListByEntityType(EntityType entityType);
}
