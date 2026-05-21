package com.softpala.SalesDeepDive_ERP.api.reports.dto;
import java.time.LocalDate;
public class RawMaterialTransferReportRow {
    public Long runId;
    public Long productId;
    public String productCode;
    public String productName;
    public String rawMaterialCode;
    public String rawMaterialName;
    public double allowablePct;
    public double actualPct;
    public double variancePct;
    public double producedQty;
    public double transferredQtyToSales;
    public LocalDate transferDate;
}
