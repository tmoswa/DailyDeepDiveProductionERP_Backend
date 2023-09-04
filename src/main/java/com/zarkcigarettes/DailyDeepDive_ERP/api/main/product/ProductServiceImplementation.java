package com.zarkcigarettes.DailyDeepDive_ERP.api.main.product;

import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms.iNTMsService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.MainEntityRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.NTMsRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionRunRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImplementation implements iProductService {

    private final ProductRepository productRepository;
    private final ProductionRunRepository productionRunRepository;
    private final MainEntityRepository mainEntityRepository;
    private final ActivityLogService activityLogService;
    @Override
    public Collection<Product> productList(int limit) {
        return  productRepository.findAll(PageRequest.of(0,limit)).toList();
    }


    @Override
    public Collection<Product> productListByMainEntity(Long mainEntityID) {
        MainEntity mainEntity=      mainEntityRepository.findById(mainEntityID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("mainEntity with id %d not found", mainEntityID)));
        return  productRepository.findProductByMainEntity(mainEntity);
    }

    public Collection<ProducedProduct> producedList(LocalDate from, LocalDate to, int limit) {
        ArrayList<ProducedProduct> productFin = new ArrayList<>();
        List<Product> availableProducts = productRepository.findAll();
        List<ProductionRun> productionRuns=productionRunRepository.findAll()
                .stream().filter(m_used->m_used.getFrom_date().isAfter(from.minusDays(1)) && m_used.getFrom_date().isBefore(to.plusDays(1)) && m_used.getStatus().equals("Completed"))
                .collect(Collectors.toList());
        for (Product pd : availableProducts) {
            ProducedProduct pp=new ProducedProduct();
            pp.setCode(pd.getCode());
            pp.setSize(pd.getSize());
            pp.setName(pd.getName());
            pp.setDescription(pd.getDescription());
            pp.setMain_entity_product(pd.getMain_entity_product());
            pp.setUnit_of_measure(pd.getUnit_of_measure());
            pp.setQuantity(0);
            for (ProductionRun productionRun : productionRuns) {
                if(productionRun.getProduct_production_run().getId()==pd.getId()){
                    pp.setQuantity(pd.getQuantity() + productionRun.getQuantity());
                }
            }
            productFin.add(pp);
        }
        return productFin;
    }

    @Override
    public Product saveNTMs(Product product) {
        activityLogService.addActivityLog("Added Product : "+product.getName() +" , with quantity "+product.getQuantity()+" , of Entity: "+product.getMain_entity_product().getLegal_name(),"Product");
        return productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
            boolean exists = productRepository.existsById(id);
            if (!exists) {
                return  Boolean.FALSE;
            }
        activityLogService.addActivityLog("Deleted Product : "+productRepository.findById(id).get().getName() +" , with quantity "+productRepository.findById(id).get().getQuantity()+" , of Entity: "+productRepository.findById(id).get().getMain_entity_product().getLegal_name(),"Product");

        productRepository.deleteById(id);
            return  Boolean.TRUE;

    }
@Override
    public boolean updateProduct(Long id, Product product) {
    Product details = productRepository.findById(id)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("product with id %d not found", id)));

    if (details.getName().length() > 0) {
        activityLogService.addActivityLog("Updated Product : "+product.getName() +" , from quantity "+details.getQuantity()+" , To quantity "+product.getQuantity()+" , of Entity: "+product.getMain_entity_product().getLegal_name(),"Product");

        details.setName(product.getName());
        details.setCode(product.getCode());
        details.setSize(product.getSize());
        details.setDescription(product.getDescription());
        details.setQuantity(product.getQuantity());
        details.setUnit_of_measure(product.getUnit_of_measure());
        details.setMain_entity_product(product.getMain_entity_product());
        return  Boolean.TRUE;
    }
    return  Boolean.FALSE;
    }
    @Data
    class ProducedProduct{
        private MainEntity main_entity_product;
        private String name;
        private String code;
        private String size;
        private double quantity;
        private String description;
        private String unit_of_measure;
    }
}


