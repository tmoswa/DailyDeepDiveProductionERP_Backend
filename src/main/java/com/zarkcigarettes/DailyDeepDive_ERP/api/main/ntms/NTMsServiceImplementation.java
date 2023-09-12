package com.zarkcigarettes.DailyDeepDive_ERP.api.main.ntms;

import com.zarkcigarettes.DailyDeepDive_ERP.api.config.currency.iCurrencyService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.inc.ActivityLogService;
import com.zarkcigarettes.DailyDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Override
    public Collection<NTMs> ntmsList(int limit) {
        log.info(ntMsRepository.findAllByOrderBySequenceAsc().toString());
        return ntMsRepository.findAllByOrderBySequenceAsc();
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
            details.setQuantity(ntMs.getQuantity());
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
                .stream().filter(m_used -> m_used.getProductionRun().getFrom_date().isAfter(from.minusDays(1)) && m_used.getProductionRun().getFrom_date().isBefore(to.plusDays(1)))
                .collect(Collectors.toList());

        for (NTMs nt : availableNTMs) {
            NTMs ntMs = nt;
            ntmsUsed ntm=new ntmsUsed();
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



    public Collection<NTMsRequiredExpected> ntmsRequiredExpectedList(int limit) {
        ArrayList<NTMsRequiredExpected> ntMsRequiredExpected = new ArrayList<>();

        Collection<NTMs> availableNTMs = ntMsRepository.findAllByOrderBySequenceAsc();
        Collection<ProductionRun> productionRuns = productionRunServiceImplementation.productionRunList(100).stream().filter(pr->pr.getStatus().equals("Planned")).collect(Collectors.toList());


        for (NTMs ntMs : availableNTMs) {
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
                            .filter(materialUsage1 -> materialUsage1.getNtMs_usage().equals(ntMs))
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
    class NTMsRequiredExpected {
        private NTMs ntMs;
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
    class NTMsUsed {
        private NTMs ntMs;
    }
    @Data
    class ntmsUsed{
        private MainEntity main_entity_material;
        private String name;
        private String code;
        private String size;
        private double quantity;
        private int lead_time;
        private String description;
        private String unit_of_measure;
    }

}
