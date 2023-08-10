package com.zarkcigarettes.DailyDeepDive_ERP.api.main.sub_entity;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.iMainEntityService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MainEntityRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.SubEntityRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.SubEntity;
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
public class SubEntityServiceImplementation implements iSubEntityService {

    private final SubEntityRepository subEntityRepository;
    @Override
    public Collection<SubEntity> subEntityList(int limit) {
        return  subEntityRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public SubEntity saveSubEntity(SubEntity subEntity) {
        return subEntityRepository.save(subEntity);
    }

    @Override
    public boolean deleteSubEntity(Long id) {
            boolean exists = subEntityRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        subEntityRepository.deleteById(id);
            return  Boolean.TRUE;

    }
@Override
    public boolean updateSubEntity(Long id, SubEntity subEntity) {
    SubEntity details = subEntityRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("sub entity with id %d not found", id)));

    if (details.getName().length() > 0) {
        details.setName(subEntity.getName());
        details.setFull_address(subEntity.getFull_address());
        details.setCountry(subEntity.getCountry());
        details.setContact_name(subEntity.getContact_name());
        details.setContact_number(subEntity.getContact_number());
        details.setWarehouse(subEntity.getWarehouse());
        details.setDescription(subEntity.getDescription());
        details.setActive_status(subEntity.getActive_status());
        details.setContact_email(subEntity.getContact_email());

        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
