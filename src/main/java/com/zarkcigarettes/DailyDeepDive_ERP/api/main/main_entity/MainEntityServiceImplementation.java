package com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.LoggedInUserDetails;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ActivityLogRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.EntityTypeRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MainEntityRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.UserRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.ActivityLog;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.EntityType;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MainEntityServiceImplementation implements iMainEntityService {

    private final MainEntityRepository mainEntityRepository;
private final ActivityLogService activityLogService;
    
    @Override
    public Collection<MainEntity> mainEntityList(int limit) {
        return  mainEntityRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Collection<MainEntity> mainEntityListByEntityType(EntityType entityType) {
        Collection<MainEntity> myEntities = mainEntityRepository.findAll(PageRequest.of(0,4000)).toList();
        Collection<MainEntity> myMainEntities=new ArrayList<>();
        for(MainEntity a:myEntities){
            for (EntityType et:a.getMain_entity_type()){
                if(et.getName().equals(entityType.getName())){
                    myMainEntities.add(a);
                }
            }
        }
        return  myMainEntities;
    }

    @Override
    public MainEntity saveMainEntity(MainEntity mainEntity) {
        activityLogService.addActivityLog("Added main entity: "+mainEntity.getLegal_name(),"Entity");
        return mainEntityRepository.save(mainEntity);
    }

    @Override
    public boolean deleteMainEntity(Long id) {
            boolean exists = mainEntityRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        mainEntityRepository.deleteById(id);
        activityLogService.addActivityLog("Deleted main entity: "+mainEntityRepository.findById(id).get().getLegal_name(),"Entity");

        return  Boolean.TRUE;

    }
@Override
    public boolean updateMainEntity(Long id, MainEntity mainEntity) {
    MainEntity details = mainEntityRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("entity main entity with id %d not found", id)));


    if (details.getLegal_name().length() > 0) {
        activityLogService.addActivityLog("Updated main entity: "+details.getLegal_name(),"Entity");

        details.setLegal_name(mainEntity.getLegal_name());
        details.setFull_address(mainEntity.getFull_address());
        details.setCountry(mainEntity.getCountry());
        details.setContact_name(mainEntity.getContact_name());
        details.setContact_number(mainEntity.getContact_number());
        details.setWarehouse(mainEntity.getWarehouse());
        details.setDescription(mainEntity.getDescription());
        details.setActive_status(mainEntity.getActive_status());
        details.setSub_entities(mainEntity.getSub_entities());
        details.setContact_email(mainEntity.getContact_email());
        details.setMain_entity_currency(mainEntity.getMain_entity_currency());
        details.setMain_entity_type(mainEntity.getMain_entity_type());

        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
