package com.zarkcigarettes.DailyDeepDive_ERP.api.main.purchase_order;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.increaments.IncreamentsServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.main_entity.MainEntityServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MainEntityRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.NTMsRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.PurchaseOrderRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Increaments;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MainEntity;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PurchaseOrderServiceImplementation implements iPurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MainEntityRepository mainEntityRepository;
    private final IncreamentsServiceImplementation increamentsServiceImplementation;
    private final NTMsRepository ntMsRepository;

    private final ActivityLogService activityLogService;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final Path root = Paths.get("uploads/");
    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {

                Files.createDirectory(root);
                System.out.println("Directory created");
            } else {

                System.out.println("Directory already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Collection<PurchaseOrder> purchaseOrderList(Long entityID) {
        MainEntity mainEntity=      mainEntityRepository.findById(entityID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("main entity with id %d not found", entityID)));
        return  purchaseOrderRepository.findPurchaseOrderByMainEntity(mainEntity);
    }

    @Override
    public Collection<PurchaseOrder> totalPurchaseOrderList(int limit) {
        return  purchaseOrderRepository.findAll(PageRequest.of(0,limit)).toList();
    }


    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder,MultipartFile file) {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        init();
        try {
            Files.copy(file.getInputStream(), this.root.resolve(currentDateAndTime+"_"+file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        purchaseOrder.setName(file.getOriginalFilename());
        purchaseOrder.setUrl(currentDateAndTime+"_"+file.getOriginalFilename());

        Increaments increaments=increamentsServiceImplementation.getIncreaments();
        increaments.setPurchase_order_inc(increaments.getPurchase_order_inc()+1);
        purchaseOrder.setOrder_number(increaments.getPurchase_order_inc());
        increamentsServiceImplementation.updateIncreaments(1L,increaments);

        LocalDate today = LocalDate.now();
        //purchaseOrder.setOrder_date(today);
        NTMs ntm=ntMsRepository.findById(purchaseOrder.getNtMs().getId()).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("NTM not found")));
        int leadTime=ntm.getLead_time();
        purchaseOrder.setDelivery_date(today.plusDays(leadTime));
        purchaseOrder.setDelivered_quantity(0.0);

        activityLogService.addActivityLog("Added Purchase Order of: "+purchaseOrder.getNtMs().getName()+" , quantity of: "+purchaseOrder.getQuantity()+" , dated:"+purchaseOrder.getOrder_date(),"Purchase Order");


        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    public boolean deletePurchaseOrder(Long id) {
            boolean exists = purchaseOrderRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        activityLogService.addActivityLog("Deleted Purchase Order of: "+purchaseOrderRepository.findById(id).get().getNtMs().getName()+" , quantity of "+purchaseOrderRepository.findById(id).get().getNtMs().getQuantity(),"Purchase Order");

        purchaseOrderRepository.deleteById(id);

        return  Boolean.TRUE;

    }
@Override
    public boolean updatePurchaseOrder(Long id, PurchaseOrder purchaseOrder,MultipartFile file) {
    LocalDateTime currentDateAndTime = LocalDateTime.now();
    init();
    PurchaseOrder details = purchaseOrderRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("purchase order with id %d not found", id)));

    if (details.getName().length() > 0) {
        if(purchaseOrder.getName().equals("-") || purchaseOrder.getUrl().equals("-")){
            try {
                Files.copy(file.getInputStream(), this.root.resolve(currentDateAndTime+"_"+file.getOriginalFilename()));
            } catch (Exception e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
            details.setName(file.getOriginalFilename());
            details.setUrl(currentDateAndTime+"_"+file.getOriginalFilename());
        }else{
            details.setName(purchaseOrder.getName());
            details.setUrl(purchaseOrder.getUrl());
        }

        details.setDescription(purchaseOrder.getDescription());
        details.setQuantity(purchaseOrder.getQuantity());
        details.setOrder_number(purchaseOrder.getOrder_number());
        details.setSupplier(purchaseOrder.getSupplier());
        details.setNtMs(purchaseOrder.getNtMs());
        details.setStatus(purchaseOrder.getStatus());
        details.setMain_entity_po(purchaseOrder.getMain_entity_po());

        if(purchaseOrder.getStatus().equals("Delivered") && details.getStatus().equals("Initiated")){
            NTMs ntMs=  ntMsRepository.findById(purchaseOrder.getNtMs().getId())
                    .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", id)));
            double totalNTMs=ntMs.getQuantity()+details.getQuantity();
           // ntMs.setQuantity(Double.parseDouble(df.format(totalNTMs)));
        }

        activityLogService.addActivityLog("Updated Purchase Order of: "+purchaseOrder.getNtMs().getName()+" , quantity of: "+purchaseOrder.getQuantity()+ " , status: "+purchaseOrder.getStatus()+" , dated:"+purchaseOrder.getOrder_date(),"Purchase Order");

        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }

    @Override
    public boolean deliverPurchaseOrder(Long id, PurchaseOrder purchaseOrder) {
        init();
        PurchaseOrder details = purchaseOrderRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("purchase order with id %d not found", id)));

        if (purchaseOrder.getStatus().equals("Delivered") && details.getStatus().equals("Initiated")) {

            activityLogService.addActivityLog("Updated Purchase Order of: "+purchaseOrder.getNtMs().getName()+" , quantity of: "+purchaseOrder.getQuantity()+ " , status: "+purchaseOrder.getStatus()+" , dated:"+purchaseOrder.getOrder_date(),"Purchase Order");

            details.setId(id);
                details.setName(purchaseOrder.getName());
                details.setUrl(purchaseOrder.getUrl());

            details.setDescription(purchaseOrder.getDescription());
            details.setQuantity(purchaseOrder.getQuantity());
            details.setOrder_number(purchaseOrder.getOrder_number());
            details.setSupplier(purchaseOrder.getSupplier());
            details.setNtMs(purchaseOrder.getNtMs());
            details.setStatus(purchaseOrder.getStatus());
            details.setMain_entity_po(purchaseOrder.getMain_entity_po());
            details.setOrder_date(purchaseOrder.getOrder_date());
            details.setDelivered_quantity(purchaseOrder.getDelivered_quantity());
            details.setDelivery_date(purchaseOrder.getDelivery_date());

                NTMs ntMs=  ntMsRepository.findById(purchaseOrder.getNtMs().getId())
                        .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", id)));
                double totalNTMs=ntMs.getQuantity()+purchaseOrder.getDelivered_quantity();
               // ntMs.setQuantity(Double.parseDouble(df.format(totalNTMs)));



            return  Boolean.TRUE;
        }
        return  Boolean.FALSE;
    }
}
