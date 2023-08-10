package com.zarkcigarettes.DailyDeepDive_ERP.api.main.sub_entity;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.SubEntity;

import java.util.Collection;

public interface iSubEntityService {

    Collection<SubEntity> subEntityList(int limit);

    SubEntity saveSubEntity(SubEntity subEntity);

    boolean deleteSubEntity(Long id);

    boolean updateSubEntity(Long id, SubEntity subEntity);
}
