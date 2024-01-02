package com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms;

import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.iCurrencyService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.production_run.ProductionRunServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.purchase_order.PurchaseOrderServiceImplementation;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.CurrencyRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.NTMsRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionMaterialUsageRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.ProductionRunRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NTMsServiceImplementation implements iNTMsService {

    private final NTMsRepository ntMsRepository;
    private final ProductionMaterialUsageRepository productionMaterialUsageRepository;
    private final ProductionRunServiceImplementation productionRunServiceImplementation;
    private final MaterialUsageServiceImplementation materialUsageServiceImplementation;
    private final PurchaseOrderServiceImplementation purchaseOrderServiceImplementation;
    private final ActivityLogService activityLogService;
    private final ProductServiceImplementation productServiceImplementation;

    @Override
    public Collection<ntmsUsed> ntmsList(int limit) {
        LocalDate startDate=LocalDate.parse("2023-08-01");
        LocalDate endDate=startDate.plusYears(3);
        Collection<ntmsUsed> usedNTMs=this.ntmsUsedList(startDate, endDate, 1000000);
        List<NTMs> ntmsOpeningBalance=ntMsRepository.findAllByOrderBySequenceAsc();

        ArrayList<NTMsWithCountingIssues> countingIssues= new ArrayList<>();

        LocalDate ld=LocalDate.parse("2023-11-30");
        if(ld.isBefore(endDate)) {
            for (NTMs ntMsFin1 : ntmsOpeningBalance) {
                NTMsWithCountingIssues ntMsWithCountingIssues = new NTMsWithCountingIssues();
                ntMsWithCountingIssues.ntm = ntMsFin1;
                switch (ntMsFin1.getCode()) {
                    case "AKA14-20-1":
                        ntMsWithCountingIssues.adjustCounting = 101;
                        break;
                    case "008":
                        ntMsWithCountingIssues.adjustCounting = 16930;
                        break;
                    case "005":
                        ntMsWithCountingIssues.adjustCounting = 3.2;
                        break;
                    case "006":
                        ntMsWithCountingIssues.adjustCounting = 1.36;
                        break;
                    case "007":
                        ntMsWithCountingIssues.adjustCounting = 1.86;
                        break;
                    case "010":
                        ntMsWithCountingIssues.adjustCounting = 1.9;
                        break;
                    case "009":
                        ntMsWithCountingIssues.adjustCounting = 2.05;
                        break;
                    case "011":
                        ntMsWithCountingIssues.adjustCounting = 0.09;
                        break;
                    case "012":
                        ntMsWithCountingIssues.adjustCounting = 0.4;
                        break;
                    case "013":
                        ntMsWithCountingIssues.adjustCounting = 0.73;
                        break;
                    case "001":
                        ntMsWithCountingIssues.adjustCounting = 5.9;
                        break;
                    case "002":
                        ntMsWithCountingIssues.adjustCounting = -59.2;
                        break;
                    case "003":
                        ntMsWithCountingIssues.adjustCounting = -24.3;
                        break;
                    case "014":
                        ntMsWithCountingIssues.adjustCounting = -3.15;
                        break;
                    case "015":
                        ntMsWithCountingIssues.adjustCounting = 8;
                        break;
                    default:
                        ntMsWithCountingIssues.adjustCounting = 0;
                        break;
                }
                countingIssues.add(ntMsWithCountingIssues);
            }
        }

        Collection<PurchaseOrder> deliveredPurchaseOrders=purchaseOrderServiceImplementation.totalPurchaseOrderList(90000).
                stream().filter(ds_po->ds_po.getStatus().equals("Delivered"))
                .collect(Collectors.toList()).stream().filter(d_po->(d_po.getDelivery_date().isAfter(startDate) && d_po.getDelivery_date().isBefore(endDate))).collect(Collectors.toList());

        for(ntmsUsed usedNTM:usedNTMs){
            NTMs ntmOpeningBalance=ntmsOpeningBalance.stream().filter(ntms->ntms.getId()==usedNTM.getId()).findAny().get();
            List<PurchaseOrder> deliveredNTMPOs=deliveredPurchaseOrders.stream().filter(ntms->ntms.getNtMs().getId()==usedNTM.getId()).collect(Collectors.toList());
            double deliveredPOs=0;
            for(PurchaseOrder deliveredNTMPO:deliveredNTMPOs){
                deliveredPOs+=deliveredNTMPO.getDelivered_quantity();
            }
            double availableQuantity=ntmOpeningBalance.getQuantity()+deliveredPOs-usedNTM.getQuantity();
            usedNTM.setQuantity(availableQuantity);

            if(countingIssues.stream().anyMatch(ntmZ-> Objects.equals(ntmZ.ntm.getId(), usedNTM.getId()))){
                NTMsWithCountingIssues countingIssues0=countingIssues.stream().filter(ntmZ-> Objects.equals(ntmZ.ntm.getId(), usedNTM.getId())).findAny().get();
                usedNTM.setQuantity(usedNTM.getQuantity()+countingIssues0.getAdjustCounting());
            }

        }

        return usedNTMs;
    }

    @Override
    public NTMs saveNTMs(NTMs ntMs) {
        activityLogService.addActivityLog("Added Material : " + ntMs.getName() + " , with quantity " + ntMs.getQuantity() + " , of Entity: " + ntMs.getMain_entity_material().getLegal_name(), "Material");
        return ntMsRepository.save(ntMs);
    }

    @Override
    public boolean deleteNTMs(Long id) {
        boolean exists = ntMsRepository.existsById(id);
        if (!exists) {
            return Boolean.FALSE;
        }
        activityLogService.addActivityLog("deleted Material : " + ntMsRepository.findById(id).get().getName() + " , with quantity " + ntMsRepository.findById(id).get().getQuantity() + " , of Entity: " + ntMsRepository.findById(id).get().getMain_entity_material().getLegal_name(), "Material");
        ntMsRepository.deleteById(id);
        return Boolean.TRUE;

    }

    @Override
    public boolean updateNTMs(Long id, NTMs ntMs) {
        NTMs details = ntMsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ntms with id %d not found", id)));

        if (details.getName().length() > 0) {
            activityLogService.addActivityLog("Update Material : " + details.getName() + " , from quantity " + details.getQuantity() + " , of Entity: " + ntMs.getMain_entity_material().getLegal_name() + " , to Quantity: " + ntMs.getQuantity(), "Material");

            details.setName(ntMs.getName());
            details.setCode(ntMs.getCode());
            details.setSize(ntMs.getSize());
            details.setDescription(ntMs.getDescription());
            details.setQuantity(details.getQuantity());
            details.setUnit_of_measure(ntMs.getUnit_of_measure());
            details.setLead_time(ntMs.getLead_time());
            details.setMain_entity_material(ntMs.getMain_entity_material());
            details.setSequence(ntMs.getSequence());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Collection<NTMs> ntmList(int limit) {
        return ntMsRepository.findAllNTMs(limit);
    }

    public Collection<ntmsUsed> ntmsUsedList(LocalDate from, LocalDate to, int limit) {
        ArrayList<ntmsUsed> ntMsFin = new ArrayList<>();
        Collection<NTMs> availableNTMs =ntMsRepository.findAllByOrderBySequenceAsc();
        List<ProductionMaterialUsage> productionMaterialUsages = productionMaterialUsageRepository.findAll()
                .stream().filter(m_used -> m_used.getProductionRun().getFrom_date().isAfter(from) && m_used.getProductionRun().getFrom_date().isBefore(to))
                .collect(Collectors.toList());



        for (NTMs nt : availableNTMs) {
            NTMs ntMs = nt;
            ntmsUsed ntm= new ntmsUsed();
            ntm.setId(ntMs.getId());
            ntm.setCode(ntMs.getCode());
            ntm.setSize(ntMs.getSize());
            ntm.setName(ntMs.getName());
            ntm.setDescription(ntMs.getDescription());
            ntm.setLead_time(ntMs.getLead_time());
            ntm.setUnit_of_measure(ntMs.getUnit_of_measure());
            ntm.setMain_entity_material(ntMs.getMain_entity_material());
            ntm.setQuantity(0);
            for (ProductionMaterialUsage productionMaterialUsage : productionMaterialUsages) {
                if (productionMaterialUsage.getNtMs_usage().getId() == nt.getId()) {
                    ntm.setQuantity(ntm.getQuantity() + productionMaterialUsage.getQuantity());
                }
            }


            ntMsFin.add(ntm);
        }
        return ntMsFin;
    }


    public Collection<ntmsUsed> openingBalance(LocalDate endDate) {
        LocalDate startDate=LocalDate.parse("2023-08-01");
        Collection<ntmsUsed> usedNTMs=this.ntmsUsedList(startDate, endDate, 1000000);
        List<NTMs> ntmsOpeningBalance=ntMsRepository.findAllByOrderBySequenceAsc();



        ArrayList<NTMsWithCountingIssues> countingIssues= new ArrayList<>();

        LocalDate ld=LocalDate.parse("2023-11-29");
        if(ld.isBefore(endDate)) {
            for (NTMs ntMsFin1 : ntmsOpeningBalance) {
                NTMsWithCountingIssues ntMsWithCountingIssues = new NTMsWithCountingIssues();
                ntMsWithCountingIssues.ntm = ntMsFin1;
                switch (ntMsFin1.getCode()) {
                    case "AKA14-20-1":
                        ntMsWithCountingIssues.adjustCounting = 101;
                        break;
                    case "008":
                        ntMsWithCountingIssues.adjustCounting = 16930;
                        break;
                    case "005":
                        ntMsWithCountingIssues.adjustCounting = 3.2;
                        break;
                    case "006":
                        ntMsWithCountingIssues.adjustCounting = 1.36;
                        break;
                    case "007":
                        ntMsWithCountingIssues.adjustCounting = 1.86;
                        break;
                    case "010":
                        ntMsWithCountingIssues.adjustCounting = 1.9;
                        break;
                    case "009":
                        ntMsWithCountingIssues.adjustCounting = 2.05;
                        break;
                    case "011":
                        ntMsWithCountingIssues.adjustCounting = 0.09;
                        break;
                    case "012":
                        ntMsWithCountingIssues.adjustCounting = 0.4;
                        break;
                    case "013":
                        ntMsWithCountingIssues.adjustCounting = 0.73;
                        break;
                    case "001":
                        ntMsWithCountingIssues.adjustCounting = 5.9;
                        break;
                    case "002":
                        ntMsWithCountingIssues.adjustCounting = -59.2;
                        break;
                    case "003":
                        ntMsWithCountingIssues.adjustCounting = -24.3;
                        break;
                    case "014":
                        ntMsWithCountingIssues.adjustCounting = -3.15;
                        break;
                    case "015":
                        ntMsWithCountingIssues.adjustCounting = 8;
                        break;
                    default:
                        ntMsWithCountingIssues.adjustCounting = 0;
                        break;
                }
                countingIssues.add(ntMsWithCountingIssues);
            }
        }

        Collection<PurchaseOrder> deliveredPurchaseOrders=purchaseOrderServiceImplementation.totalPurchaseOrderList(90000).
                stream().filter(ds_po->ds_po.getStatus().equals("Delivered"))
                .collect(Collectors.toList()).stream().filter(d_po->(d_po.getDelivery_date().isAfter(startDate) && d_po.getDelivery_date().isBefore(endDate))).collect(Collectors.toList());

        for(ntmsUsed usedNTM:usedNTMs){
            NTMs ntmOpeningBalance=ntmsOpeningBalance.stream().filter(ntms->ntms.getId()==usedNTM.getId()).findAny().get();
            List<PurchaseOrder> deliveredNTMPOs=deliveredPurchaseOrders.stream().filter(ntms->ntms.getNtMs().getId()==usedNTM.getId()).collect(Collectors.toList());
            double deliveredPOs=0;
            for(PurchaseOrder deliveredNTMPO:deliveredNTMPOs){
                deliveredPOs+=deliveredNTMPO.getDelivered_quantity();
            }
            double availableQuantity=ntmOpeningBalance.getQuantity()+deliveredPOs-usedNTM.getQuantity();
            usedNTM.setQuantity(availableQuantity);

            if(countingIssues.stream().anyMatch(ntmZ-> Objects.equals(ntmZ.ntm.getId(), usedNTM.getId()))){
                NTMsWithCountingIssues countingIssues0=countingIssues.stream().filter(ntmZ-> Objects.equals(ntmZ.ntm.getId(), usedNTM.getId())).findAny().get();
                usedNTM.setQuantity(usedNTM.getQuantity()+countingIssues0.getAdjustCounting());
            }

        }

        return usedNTMs;
    }

    public Collection<completeNtmsUsed> completeNtmsUsed(LocalDate from, LocalDate to, int limit) {
        Collection<NTMs> allNTMs =ntMsRepository.findAllByOrderBySequenceAsc();
        ArrayList<NTMsWithCountingIssues> countingIssues= new ArrayList<>();
        LocalDate ld=LocalDate.parse("2023-11-28");
        if(ld.isAfter(from) && ld.isBefore(to)) {
            log.info("we are here");
            for (NTMs ntMsFin : allNTMs) {
                NTMsWithCountingIssues ntMsWithCountingIssues = new NTMsWithCountingIssues();
                ntMsWithCountingIssues.ntm = ntMsFin;
                switch (ntMsFin.getCode()) {
                    case "AKA14-20-1":
                        ntMsWithCountingIssues.adjustCounting = 101;
                        break;
                    case "008":
                        ntMsWithCountingIssues.adjustCounting = 16930;
                        break;
                    case "005":
                        ntMsWithCountingIssues.adjustCounting = 3.2;
                        break;
                    case "006":
                        ntMsWithCountingIssues.adjustCounting = 1.36;
                        break;
                    case "007":
                        ntMsWithCountingIssues.adjustCounting = 1.86;
                        break;
                    case "010":
                        ntMsWithCountingIssues.adjustCounting = 1.9;
                        break;
                    case "009":
                        ntMsWithCountingIssues.adjustCounting = 2.05;
                        break;
                    case "011":
                        ntMsWithCountingIssues.adjustCounting = 0.09;
                        break;
                    case "012":
                        ntMsWithCountingIssues.adjustCounting = 0.4;
                        break;
                    case "013":
                        ntMsWithCountingIssues.adjustCounting = 0.73;
                        break;
                    case "001":
                        ntMsWithCountingIssues.adjustCounting = 5.9;
                        break;
                    case "002":
                        ntMsWithCountingIssues.adjustCounting = -59.2;
                        break;
                    case "003":
                        ntMsWithCountingIssues.adjustCounting = -24.3;
                        break;
                    case "014":
                        ntMsWithCountingIssues.adjustCounting = -3.15;
                        break;
                    case "015":
                        ntMsWithCountingIssues.adjustCounting = 8;
                        break;
                    default:
                        ntMsWithCountingIssues.adjustCounting = 0;
                        break;
                }
                countingIssues.add(ntMsWithCountingIssues);
            }
        }



        ArrayList<completeNtmsUsed> ntMsFin = new ArrayList<>();
        Collection<ntmsUsed> availableNTMs =this.ntmsUsedList(from,to,limit);

        List<ntmsUsed> ntmsOpeningBalance=new ArrayList<>(this.openingBalance(from));

        Collection<PurchaseOrder> deliveredPurchaseOrders=purchaseOrderServiceImplementation.totalPurchaseOrderList(90000).
                stream().filter(ds_po->ds_po.getStatus().equals("Delivered"))
                .collect(Collectors.toList()).stream().filter(d_po->(d_po.getDelivery_date().isAfter(from.plusDays(1)) && d_po.getDelivery_date().isBefore(to.plusDays(1)))).collect(Collectors.toList());

        Collection<ProductServiceImplementation.ProducedProduct> producedProducts= productServiceImplementation.producedList(from,to,limit);

        Collection <MaterialUsage> materialUsage = materialUsageServiceImplementation.materialUsageList(producedProducts.stream().findFirst().get().getId());


        for (ntmsUsed nt : availableNTMs) {
            completeNtmsUsed ntm= new completeNtmsUsed();
            ntmsUsed ntmOpeningBalance=ntmsOpeningBalance.stream().filter(ntms->ntms.getId()==nt.getId()).findAny().get();
            List<PurchaseOrder> deliveredNTMPOs=deliveredPurchaseOrders.stream().filter(ntms->ntms.getNtMs().getId()==nt.getId()).collect(Collectors.toList());
            double deliveredPOs=0;
            for(PurchaseOrder deliveredNTMPO:deliveredNTMPOs){
                deliveredPOs+=deliveredNTMPO.getDelivered_quantity();
            }
            double availableQuantity=ntmOpeningBalance.getQuantity()+deliveredPOs-nt.getQuantity();
            if(countingIssues.stream().anyMatch(ntmZ->ntmZ.ntm.getId()==nt.getId())){
                NTMsWithCountingIssues countingIssues0=countingIssues.stream().filter(ntmZ->ntmZ.ntm.getId()==nt.getId()).findAny().get();
                ntm.setCountingIssues(countingIssues0);
                 availableQuantity=ntmOpeningBalance.getQuantity()+deliveredPOs-nt.getQuantity()+countingIssues0.getAdjustCounting();
            }


            ntm.setOpening_stock(ntmOpeningBalance.getQuantity());
            ntm.setDelivered_ntms(deliveredPOs);
            ntm.setUsed_ntms(nt.getQuantity());
            nt.setQuantity(availableQuantity);
            ntm.setNtmsUsed(nt);

            ProductServiceImplementation.ProducedProduct producedProduct=producedProducts.stream().filter(pp->pp.getMain_entity_product().equals(nt.getMain_entity_material())).findAny().get();
            ntm.setProduced_quantity(producedProduct.getQuantity());
            if(materialUsage.stream()
                    .anyMatch(materialUsage2 -> materialUsage2.getNtMs_usage().getId().equals(nt.getId()))){
                MaterialUsage materialUsage1=materialUsage.stream()
                        .filter(materialUsage2 -> materialUsage2.getNtMs_usage().getId().equals(nt.getId()))
                        .findAny().get();
                ntm.setUsage_per_case(materialUsage1.getQuantity());
            }else {
                ntm.setUsage_per_case(0);
            }


            ntMsFin.add(ntm);
        }
        return ntMsFin;
    }

    public Collection<NTMsRequiredExpected> ntmsRequiredExpectedList(int limit) {
        ArrayList<NTMsRequiredExpected> ntMsRequiredExpected = new ArrayList<>();

        Collection<ntmsUsed> availableNTMs = this.ntmsList(limit);
        Collection<ProductionRun> productionRuns = productionRunServiceImplementation.productionRunList(100).stream().filter(pr->pr.getStatus().equals("Planned")).collect(Collectors.toList());


        for (ntmsUsed ntMs : availableNTMs) {
            NTMsRequiredExpected ntMsRequiredExpected1 = new NTMsRequiredExpected();
            ntMsRequiredExpected1.ntMs = ntMs;
            int productionRunsDup = 0;
            for (ProductionRun productionRun : productionRuns) {

                Period duration = Period.between(LocalDate.now(), productionRun.getFrom_date());
                int durationIndays = (duration.getMonths() * 30) + duration.getDays();

                Month month = LocalDate.now().getMonth();
                Month month1=productionRun.getFrom_date().getMonth();


                Collection<ProductionRun> dailyProductionRuns2 = productionRunServiceImplementation.productionRunList(100).stream().filter(pr->pr.getStatus().equals("Completed")).collect(Collectors.toList()).stream().filter(pr->pr.getFrom_date().getMonth().equals(month)).collect(Collectors.toList());
                double alreadyProduced=0;
                for(ProductionRun ppr:dailyProductionRuns2){
                    alreadyProduced+=ppr.getQuantity();
                }

                if(month.equals(month1)){
                    durationIndays=1;
                }

                if (durationIndays > 0 || month.equals(month1)) {
                    log.info("GenerationdurationIndays------ " + productionRun.getFrom_date() + "_______" + durationIndays + "");
                    double productionQnty = productionRun.getQuantity();
                    if(month.equals(month1)){
                        productionQnty=productionQnty-alreadyProduced;
                    }
                    Product productForProduction = productionRun.getProduct_production_run();

                    MaterialUsage materialUsage = materialUsageServiceImplementation.materialUsageList(productForProduction.getId())
                            .stream()
                            .filter(materialUsage1 -> materialUsage1.getNtMs_usage().getId().equals(ntMs.getId()))
                            .findAny().get();


                    if (materialUsage != null) {
                        if (ntMsRequiredExpected.size() > 0) {
                            try {
                                NTMsRequiredExpected ntMsRequiredExpected2 = ntMsRequiredExpected
                                        .stream().filter(ntMsRequiredExpected3 -> ntMsRequiredExpected3.ntMs.getId() == ntMs.getId())
                                        .findAny()
                                        .get();
                                
                                if (ntMsRequiredExpected2 != null) {
                                    if (durationIndays < 31) {
                                        ntMsRequiredExpected1.quantity_required_30 = ntMsRequiredExpected2.quantity_required_30 + (materialUsage.getQuantity() * (productionQnty / 50));
                                    } else if (durationIndays < 61) {
                                        ntMsRequiredExpected1.quantity_required_60 = ntMsRequiredExpected2.quantity_required_60 + (materialUsage.getQuantity() * (productionQnty / 50));
                                    } else if (durationIndays < 91) {
                                        ntMsRequiredExpected1.quantity_required_90 = ntMsRequiredExpected2.quantity_required_90 + (materialUsage.getQuantity() * (productionQnty / 50));
                                    } else if (durationIndays < 121) {
                                        ntMsRequiredExpected1.quantity_required_120 = ntMsRequiredExpected2.quantity_required_120 + (materialUsage.getQuantity() * (productionQnty / 50));
                                    } else if (durationIndays < 151) {
                                        ntMsRequiredExpected1.quantity_required_150 = ntMsRequiredExpected2.quantity_required_150 + (materialUsage.getQuantity() * (productionQnty / 50));
                                    } else if (durationIndays < 181) {
                                        ntMsRequiredExpected1.quantity_required_180 = ntMsRequiredExpected2.quantity_required_180 + (materialUsage.getQuantity() * (productionQnty / 50));
                                    }
                                }
                            } catch (NoSuchElementException e) {
                                ntMsRequiredExpected1 = updateEmptyData(durationIndays, ntMsRequiredExpected1, materialUsage, productionQnty / 50);
                            }
                        } else {
                            ntMsRequiredExpected1 = updateEmptyData(durationIndays, ntMsRequiredExpected1, materialUsage, productionQnty / 50);
                        }

                    }
                }
                productionRunsDup++;
            }

            //Lets do the intransit here
            Collection<PurchaseOrder> purchaseOrders = purchaseOrderServiceImplementation.totalPurchaseOrderList(9000)
                    .stream()
                    .filter(po -> po.getStatus().equals("Initiated"))
                    .collect(Collectors.toList());

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                if (purchaseOrder.getNtMs().equals(ntMs)) {
                    Period period = Period.between(LocalDate.now(), purchaseOrder.getDelivery_date());
                    int months = period.getMonths();
                    int durationIndays = (months * 30) + period.getDays();


                    if (ntMsRequiredExpected.size() > 0) {

                        try {
                            NTMsRequiredExpected ntMsRequiredExpected2 = ntMsRequiredExpected
                                    .stream().filter(ntMsRequiredExpected3 -> ntMsRequiredExpected3.ntMs.getId() == ntMs.getId())
                                    .findAny()
                                    .get();

                            if (ntMsRequiredExpected2 != null) {

                                log.info("Generation_Expected_0------");
                                log.info(ntMsRequiredExpected2.toString());

                                if (durationIndays < 31) {
                                    ntMsRequiredExpected1.quantity_expected_30 = ntMsRequiredExpected2.quantity_expected_30 + purchaseOrder.getQuantity();
                                } else if (durationIndays < 61) {
                                    ntMsRequiredExpected1.quantity_expected_60 = ntMsRequiredExpected2.quantity_expected_60 + purchaseOrder.getQuantity();
                                } else if (durationIndays < 91) {
                                    ntMsRequiredExpected1.quantity_expected_90 = ntMsRequiredExpected2.quantity_expected_90 + purchaseOrder.getQuantity();
                                } else if (durationIndays < 121) {
                                    ntMsRequiredExpected1.quantity_expected_120 = ntMsRequiredExpected2.quantity_expected_120 + purchaseOrder.getQuantity();
                                } else if (durationIndays < 151) {
                                    ntMsRequiredExpected1.quantity_expected_150 = ntMsRequiredExpected2.quantity_expected_150 + purchaseOrder.getQuantity();
                                } else if (durationIndays < 181) {
                                    ntMsRequiredExpected1.quantity_expected_180 = ntMsRequiredExpected2.quantity_expected_180 + purchaseOrder.getQuantity();
                                }
                            }
                        } catch (NoSuchElementException e) {
                            ntMsRequiredExpected1 = updateEmptyDataExpected(durationIndays, ntMsRequiredExpected1, purchaseOrder.getQuantity());
                        }
                    } else {
                        ntMsRequiredExpected1 = updateEmptyDataExpected(durationIndays, ntMsRequiredExpected1, purchaseOrder.getQuantity());
                    }
                }
            }

            ntMsRequiredExpected.add(ntMsRequiredExpected1);
        }

        return ntMsRequiredExpected;
    }

    private NTMsRequiredExpected updateEmptyData(int durationIndays, NTMsRequiredExpected ntMsRequiredExpected1, MaterialUsage materialUsage, double productionQnty) {
        if (durationIndays < 31) {
            ntMsRequiredExpected1.quantity_required_30 = materialUsage.getQuantity() * productionQnty;
        } else if (durationIndays < 61) {
            ntMsRequiredExpected1.quantity_required_60 = materialUsage.getQuantity() * productionQnty;
        } else if (durationIndays < 91) {
            ntMsRequiredExpected1.quantity_required_90 = materialUsage.getQuantity() * productionQnty;
        } else if (durationIndays < 121) {
            ntMsRequiredExpected1.quantity_required_120 = materialUsage.getQuantity() * productionQnty;
        } else if (durationIndays < 151) {
            ntMsRequiredExpected1.quantity_required_150 = materialUsage.getQuantity() * productionQnty;
        } else if (durationIndays < 181) {
            ntMsRequiredExpected1.quantity_required_180 = materialUsage.getQuantity() * productionQnty;
        }
        return ntMsRequiredExpected1;
    }

    private NTMsRequiredExpected updateEmptyDataExpected(int durationIndays, NTMsRequiredExpected ntMsRequiredExpected1, double expectedQuantity) {
        if (durationIndays < 31) {
            ntMsRequiredExpected1.quantity_expected_30 = expectedQuantity;
        } else if (durationIndays < 61) {
            ntMsRequiredExpected1.quantity_expected_60 = expectedQuantity;
        } else if (durationIndays < 91) {
            ntMsRequiredExpected1.quantity_expected_90 = expectedQuantity;
        } else if (durationIndays < 121) {
            ntMsRequiredExpected1.quantity_expected_120 = expectedQuantity;
        } else if (durationIndays < 151) {
            ntMsRequiredExpected1.quantity_expected_150 = expectedQuantity;
        } else if (durationIndays < 181) {
            ntMsRequiredExpected1.quantity_expected_180 = expectedQuantity;
        }
        return ntMsRequiredExpected1;
    }

    @Data
    static
    class NTMsRequiredExpected {
        private ntmsUsed ntMs;
        private double quantity_required_30;
        private double quantity_expected_30;
        private double quantity_required_60;
        private double quantity_expected_60;
        private double quantity_required_90;
        private double quantity_expected_90;
        private double quantity_required_120;
        private double quantity_expected_120;
        private double quantity_required_150;
        private double quantity_expected_150;
        private double quantity_required_180;
        private double quantity_expected_180;
    }

    @Data
    static
    class NTMsUsed {
        private NTMs ntMs;
    }


    @Data
    static
    class completeNtmsUsed{
        ntmsUsed ntmsUsed;
        double opening_stock;
        double used_ntms;
        double delivered_ntms;
        double produced_quantity;
        double usage_per_case;
        NTMsWithCountingIssues countingIssues;
    }

    @Data
    static
    class NTMsWithCountingIssues{
        NTMs ntm;
        double adjustCounting;
    }
}
