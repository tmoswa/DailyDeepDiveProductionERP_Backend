package com.zarkcigarettes.DailyDeepDive_ERP.api.config.entity_type;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.EntityTypeRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
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
public class EntityTypeServiceImplementation implements iEntityTypeService {

    private final EntityTypeRepository entityTypeRepository;
    @Override
    public Collection<EntityType> entityTypeList(int limit) {
        return  entityTypeRepository.findAll(PageRequest.of(0,limit)).toList();
    }


    @Override
    public EntityType entityTypeByName(String name) {
        return  entityTypeRepository.findClientTypeByName(name);
    }


    @Override
    public EntityType saveEntityType(EntityType entityType) {
        return entityTypeRepository.save(entityType);
    }

    @Override
    public boolean deleteEntityType(Long id) {
            boolean exists = entityTypeRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        entityTypeRepository.deleteById(id);
            return  Boolean.TRUE;

    }
@Override
    public boolean updateEntityType(Long id, EntityType entityType) {
    EntityType details = entityTypeRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("entity Type with id %d not found", id)));

    if (details.getName().length() > 0) {
        details.setName(entityType.getName());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
