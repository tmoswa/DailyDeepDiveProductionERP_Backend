package com.softpala.SalesDeepDive_ERP.api.reports;

import com.softpala.SalesDeepDive_ERP.api.reports.dto.RawMaterialTransferReportRow;
import com.softpala.SalesDeepDive_ERP.persistence.dao.*;
import com.softpala.SalesDeepDive_ERP.persistence.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RawMaterialTransferReportService {

    private final MaterialUsageRepository materialUsageRepository;
    private final ProductionMaterialUsageRepository productionMaterialUsageRepository;
    private final FinishedProductMovementRepository finishedProductMovementRepository;
    private final WasteThresholdRepository wasteThresholdRepository;
    private final VarianceAlertRepository varianceAlertRepository;
    private final TransferOutboxRepository transferOutboxRepository;
    private final ProductRepository productRepository;

    public List<RawMaterialTransferReportRow> report(LocalDate from, LocalDate to) {
        Map<String, Double> allowableByProductMaterial = materialUsageRepository.findAll().stream()
                .collect(Collectors.toMap(
                        mu -> key(mu.getProduct_usage().getId(), mu.getRawMaterialUsage().getId()),
                        MaterialUsage::getQuantity,
                        (a, b) -> a
                ));

        Map<String, Double> transfersByProductDate = finishedProductMovementRepository.findAll().stream()
                .filter(m -> m.getMovement_date() != null && !m.getMovement_date().isBefore(from) && !m.getMovement_date().isAfter(to))
                .collect(Collectors.groupingBy(
                        m -> key(m.getProduct_usage().getId(), m.getMovement_date()),
                        Collectors.summingDouble(FinishedProductMovement::getQuantity)
                ));

        return productionMaterialUsageRepository.findAll().stream()
                .filter(pmu -> pmu.getProductionRun() != null
                        && pmu.getProductionRun().getFrom_date() != null
                        && !pmu.getProductionRun().getFrom_date().isBefore(from)
                        && !pmu.getProductionRun().getFrom_date().isAfter(to))
                .map(pmu -> {
                    ProductionRun run = pmu.getProductionRun();
                    Product product = pmu.getProduct_usage();
                    RawMaterials material = pmu.getRawMaterialUsage();
                    double produced = run.getQuantity();
                    double actualPct = produced > 0 ? (pmu.getQuantity() / produced) * 100.0 : 0.0;
                    double allowablePct = allowableByProductMaterial.getOrDefault(key(product.getId(), material.getId()), 0.0);

                    RawMaterialTransferReportRow row = new RawMaterialTransferReportRow();
                    row.runId = run.getId();
                    row.productId = product.getId();
                    row.productCode = product.getCode();
                    row.productName = product.getName();
                    row.rawMaterialCode = material.getCode();
                    row.rawMaterialName = material.getName();
                    row.allowablePct = allowablePct;
                    row.actualPct = actualPct;
                    row.variancePct = actualPct - allowablePct;
                    row.producedQty = produced;
                    row.transferDate = run.getFrom_date();
                    row.transferredQtyToSales = transfersByProductDate.getOrDefault(key(product.getId(), run.getFrom_date()), 0.0);
                    return row;
                })
                .collect(Collectors.toList());
    }

    public List<VarianceAlert> evaluateVariance(LocalDate from, LocalDate to) {
        List<VarianceAlert> alerts = new ArrayList<>();

        for (RawMaterialTransferReportRow row : report(from, to)) {
            Product resolvedProduct = productRepository.findById(row.productId).orElse(null);
            if (resolvedProduct == null) {
                continue;
            }

            Optional<WasteThreshold> thresholdOpt = wasteThresholdRepository.findByProduct(resolvedProduct);
            if (thresholdOpt.isEmpty()) {
                continue;
            }

            WasteThreshold threshold = thresholdOpt.get();
            String level = null;
            if (row.variancePct >= threshold.getCriticalPct()) {
                level = "CRITICAL";
            } else if (row.variancePct >= threshold.getWarnPct()) {
                level = "WARN";
            }

            if (level == null) {
                continue;
            }

            VarianceAlert alert = new VarianceAlert();
            alert.setProduct(resolvedProduct);
            alert.setRunId(row.runId);
            alert.setLevel(level);
            alert.setMessage(String.format(
                    "%s variance %.2f%% exceeded threshold for %s (%s)",
                    level,
                    row.variancePct,
                    row.productName,
                    row.rawMaterialName
            ));
            alerts.add(varianceAlertRepository.save(alert));
        }

        return alerts;
    }

    public List<TransferOutbox> outbox(Long sinceId, int limit) {
        List<TransferOutbox> rows = sinceId == null
                ? transferOutboxRepository.findByStatusOrderByIdAsc("APPROVED")
                : transferOutboxRepository.findByIdGreaterThanAndStatusOrderByIdAsc(sinceId, "APPROVED");

        return rows.stream().limit(Math.max(limit, 1)).collect(Collectors.toList());
    }

    private String key(Long left, Long right) {
        return left + ":" + right;
    }

    private String key(Long left, LocalDate right) {
        return left + ":" + right;
    }
}
