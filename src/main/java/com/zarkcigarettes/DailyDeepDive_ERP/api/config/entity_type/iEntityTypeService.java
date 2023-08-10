package com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;

import java.util.Collection;

public interface iEntityTypeService {

    Collection<EntityType> entityTypeList(int limit);
    EntityType entityTypeByName(String name);
    EntityType saveEntityType(EntityType entityType);

    boolean deleteEntityType(Long id);

    boolean updateEntityType(Long id, EntityType entityType);
}
