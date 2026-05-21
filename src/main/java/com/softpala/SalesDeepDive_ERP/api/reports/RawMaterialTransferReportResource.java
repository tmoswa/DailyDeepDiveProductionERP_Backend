package com.softpala.SalesDeepDive_ERP.api.reports;
import com.softpala.SalesDeepDive_ERP.api.util.Response;
import com.softpala.SalesDeepDive_ERP.persistence.dao.TransferOutboxRepository;
import com.softpala.SalesDeepDive_ERP.persistence.model.TransferOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class RawMaterialTransferReportResource {
    private final RawMaterialTransferReportService reportService;
    private final TransferOutboxRepository transferOutboxRepository;
    @GetMapping("/reports/raw-material-transfers")
    public ResponseEntity<Response> rawMaterialTransfers(@RequestParam("from") String from, @RequestParam("to") String to) {
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("rows", reportService.report(start, end)))
                        .message("Raw material transfer report generated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping("/reports/variance-alerts/run")
    public ResponseEntity<Response> runVarianceAlerts(@RequestParam("from") String from, @RequestParam("to") String to) {
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("alerts", reportService.evaluateVariance(start, end)))
                        .message("Variance alerts evaluated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/transfers/outbox")
    public ResponseEntity<Map<String, Object>> transferOutbox(
            @RequestParam(value = "sinceId", required = false) Long sinceId,
            @RequestParam(value = "limit", defaultValue = "200") int limit
    ) {
        return ResponseEntity.ok(of("data", reportService.outbox(sinceId, limit)));
    }
    @PostMapping("/transfers/outbox/{id}/ack")
    public ResponseEntity<Map<String, Object>> acknowledgeOutbox(@PathVariable("id") Long id) {
        TransferOutbox item = transferOutboxRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Outbox item not found"));
        item.setSyncedAt(LocalDateTime.now());
        item.setStatus("SYNCED");
        transferOutboxRepository.save(item);
        return ResponseEntity.ok(of("status", "ok", "id", id));
    }
}
