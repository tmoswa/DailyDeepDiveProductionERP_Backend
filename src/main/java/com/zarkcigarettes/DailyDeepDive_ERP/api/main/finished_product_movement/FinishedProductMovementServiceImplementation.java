package com.zarkcigarettes.DailyDeepDive_ERP.api.main.finished_product_movement;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.FinishedProductMovementRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.FinishedProductMovement;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FinishedProductMovementServiceImplementation implements iFinishedProductMovementService {

    private final FinishedProductMovementRepository finishedProductMovementRepository;
    private final ProductRepository productRepository;
    private final ActivityLogService activityLogService;
    @Override
    public Collection<FinishedProductMovement> finishedProductMovementList(Long productID) {
        Product product=      productRepository.findById(productID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", productID)));
        return  finishedProductMovementRepository.findFinishedProductMovementByProduct(product);
    }

    @Override
    public FinishedProductMovement saveFinishedProductMovement(FinishedProductMovement finishedProductMovement) {

        Product product = productRepository.findById(finishedProductMovement.getProduct_usage().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", finishedProductMovement.getProduct_usage().getId())));

        double quantityNew = product.getQuantity() - finishedProductMovement.getQuantity();
        product.setQuantity(quantityNew);

        activityLogService.addActivityLog("Added Stock movement : "+finishedProductMovement.getDescription()+" , of Product :"+finishedProductMovement.getProduct_usage().getName()+" , manual reference: "+finishedProductMovement.getManual_reference(),"Finished Stock Movement");


        return finishedProductMovementRepository.save(finishedProductMovement);
    }

    @Override
    public boolean deleteFinishedProductMovement(Long id) {
            boolean exists = finishedProductMovementRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        FinishedProductMovement details = finishedProductMovementRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("FinishedProductMovement with id %d not found", id)));

        Product product = productRepository.findById(details.getProduct_usage().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", details.getProduct_usage().getId())));

            double quantityNew = product.getQuantity() + details.getQuantity();
            product.setQuantity(quantityNew);

        activityLogService.addActivityLog("Deleted Stock movement : "+details.getDescription()+" , of Product :"+details.getProduct_usage().getName()+" , manual reference: "+details.getManual_reference(),"Finished Stock Movement");

        finishedProductMovementRepository.deleteById(id);
            return  Boolean.TRUE;

    }
@Override
    public boolean updateFinishedProductMovement(Long id, FinishedProductMovement finishedProductMovement) {
    FinishedProductMovement details = finishedProductMovementRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("FinishedProductMovement with id %d not found", id)));

    if (details.getDescription().length() > 0) {
        activityLogService.addActivityLog("Updated Stock movement from description: "+details.getDescription()+" , from Quantity"+ details.getQuantity() + " , to Description : "+finishedProductMovement.getDescription() +"to Quantity"+ finishedProductMovement.getQuantity()+" , of Product :"+finishedProductMovement.getProduct_usage().getName()+" , manual reference: "+finishedProductMovement.getManual_reference(),"Finished Stock Movement");
        Product product = productRepository.findById(details.getProduct_usage().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", details.getProduct_usage().getId())));

        double quantityNew = product.getQuantity() + details.getQuantity() - finishedProductMovement.getQuantity();
        product.setQuantity(quantityNew);
        log.info("->"+quantityNew);

        details.setProduct_usage(finishedProductMovement.getProduct_usage());
        details.setQuantity(finishedProductMovement.getQuantity());
        details.setDescription(finishedProductMovement.getDescription());
        details.setManual_reference(finishedProductMovement.getManual_reference());
        details.setMovement_date(finishedProductMovement.getMovement_date());


        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
}
