package com.softpala.SalesDeepDive_ERP.persistence;

import com.softpala.SalesDeepDive_ERP.persistence.dao.*;
import com.softpala.SalesDeepDive_ERP.persistence.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@Profile("docker")
public class DemoProductionDataSeeder implements CommandLineRunner {

    private final CurrencyRepository currencyRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final MainEntityRepository mainEntityRepository;
    private final ProductRepository productRepository;
    private final RawMaterialsRepository ntMsRepository;
    private final MaterialUsageRepository materialUsageRepository;
    private final MaterialStockCountRepository materialStockCountRepository;
    private final MaterialCountRepository materialCountRepository;
    private final ProductionRunRepository productionRunRepository;
    private final ProductionMaterialUsageRepository productionMaterialUsageRepository;
    private final FinishedProductMovementRepository finishedProductMovementRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public DemoProductionDataSeeder(
            CurrencyRepository currencyRepository,
            EntityTypeRepository entityTypeRepository,
            MainEntityRepository mainEntityRepository,
            ProductRepository productRepository,
            RawMaterialsRepository ntMsRepository,
            MaterialUsageRepository materialUsageRepository,
            MaterialStockCountRepository materialStockCountRepository,
            MaterialCountRepository materialCountRepository,
            ProductionRunRepository productionRunRepository,
            ProductionMaterialUsageRepository productionMaterialUsageRepository,
            FinishedProductMovementRepository finishedProductMovementRepository,
            PurchaseOrderRepository purchaseOrderRepository
    ) {
        this.currencyRepository = currencyRepository;
        this.entityTypeRepository = entityTypeRepository;
        this.mainEntityRepository = mainEntityRepository;
        this.productRepository = productRepository;
        this.ntMsRepository = ntMsRepository;
        this.materialUsageRepository = materialUsageRepository;
        this.materialStockCountRepository = materialStockCountRepository;
        this.materialCountRepository = materialCountRepository;
        this.productionRunRepository = productionRunRepository;
        this.productionMaterialUsageRepository = productionMaterialUsageRepository;
        this.finishedProductMovementRepository = finishedProductMovementRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public void run(String... args) {
        try {
            Currency usd = ensureCurrency("USD", "$", "US Dollar");

            EntityType supplierType = ensureEntityType("Supplier");
            EntityType customerType = ensureEntityType("Customer");

            MainEntity factory = ensureMainEntity(
                    "Ecoshelter FMCG Manufacturing Demo",
                    "32 Willowvale Industrial Road, Harare",
                    "Zimbabwe",
                    "+263 77 112 3400",
                    "operations@example.co.zw",
                    "Harare Central Plant",
                    "Demo manufacturing entity for production and waste management presentation.",
                    usd,
                    Arrays.asList(customerType)
            );

            MainEntity supplier = ensureMainEntity(
                    "Prime Inputs Suppliers Demo",
                    "18 Coventry Road, Workington, Harare",
                    "Zimbabwe",
                    "+263 71 234 5601",
                    "supply.team@distributor-demo.co.zw",
                    "Raw Materials Depot Harare",
                    "Primary supplier of packaging and beverage ingredients.",
                    usd,
                    Arrays.asList(supplierType)
            );

            ensureMainEntity(
                    "AgriBase Commodities Demo",
                    "56 Belmont East, Bulawayo",
                    "Zimbabwe",
                    "+263 78 345 6702",
                    "agri.orders@demo.ecoshelter.co.zw",
                    "Bulk Commodities Hub",
                    "Supplier of starches, grains, and edible oils for snack production.",
                    usd,
                    Arrays.asList(supplierType)
            );

            Map<String, RawMaterials> materials = seedRawMaterials(factory);
            Map<String, Product> products = seedFinishedProducts(factory);

            seedBillOfMaterialUsage(products, materials);
            seedMaterialStockSnapshots(products, materials);
            seedProductionAndTransfers(products, materials);
            seedDemoPurchaseOrders(factory, supplier);
        } catch (Exception ex) {
            // Keep the service bootable even if demo seed data is inconsistent.
            System.err.println("DemoProductionDataSeeder skipped due to seed error: " + ex.getMessage());
        }
    }

    private Currency ensureCurrency(String name, String symbol, String label) {
        Currency existing = currencyRepository.findByName(name);
        if (existing != null) {
            return existing;
        }
        Currency currency = new Currency(label, symbol);
        currency.setName(name);
        return currencyRepository.save(currency);
    }

    private EntityType ensureEntityType(String name) {
        EntityType existing = entityTypeRepository.findClientTypeByName(name);
        if (existing != null) {
            return existing;
        }
        return entityTypeRepository.save(new EntityType(name));
    }

    private MainEntity ensureMainEntity(
            String legalName,
            String address,
            String country,
            String phone,
            String email,
            String warehouse,
            String description,
            Currency currency,
            Collection<EntityType> entityTypes
    ) {
        MainEntity existing = mainEntityRepository.findMainEntityByLegal_name(legalName);
        if (existing != null) {
            return existing;
        }

        MainEntity entity = new MainEntity();
        entity.setLegal_name(legalName);
        entity.setFull_address(address);
        entity.setCountry(country);
        entity.setContact_name("Demo Operations Lead");
        entity.setContact_number(phone);
        entity.setContact_email(email);
        entity.setWarehouse(warehouse);
        entity.setDescription(description);
        entity.setActive_status(true);
        entity.setMain_entity_currency(currency);
        entity.setMain_entity_type(entityTypes);

        return mainEntityRepository.save(entity);
    }

    private Map<String, RawMaterials> seedRawMaterials(MainEntity factory) {
        Map<String, RawMaterials> materials = new HashMap<>();

        materials.put("RM-WATER-001",        ensureMaterial("Treated Process Water",     "RM-WATER-001",        "Bulk",    "L",    240000, 0,  1, "Potable treated water for beverage formulation.", factory));
        materials.put("RM-SUGAR-001",         ensureMaterial("Refined Sugar",             "RM-SUGAR-001",        "50kg",    "kg",   68000,  5,  2, "Food grade refined sugar.", factory));
        materials.put("RM-COLA-FLV-001",      ensureMaterial("Cola Flavor Concentrate",   "RM-COLA-FLV-001",     "25kg",    "kg",   4200,   10, 3, "Concentrate for cola profile beverages.", factory));
        materials.put("RM-ORANGE-FLV-001",    ensureMaterial("Orange Flavor Concentrate", "RM-ORANGE-FLV-001",   "25kg",    "kg",   3900,   10, 4, "Concentrate for orange beverages.", factory));
        materials.put("RM-ENERGY-BASE-001",   ensureMaterial("Energy Drink Premix",       "RM-ENERGY-BASE-001",  "20kg",    "kg",   2100,   14, 5, "Vitamin and stimulant premix for energy drinks.", factory));
        materials.put("RM-CO2-001",           ensureMaterial("Food Grade Carbon Dioxide", "RM-CO2-001",          "Cylinder","kg",   5400,   4,  6, "Gas input for carbonated drinks.", factory));
        materials.put("RM-PREFORM-2L-001",    ensureMaterial("PET Preform 2L",            "RM-PREFORM-2L-001",   "Each",    "pcs",  118000, 12, 7, "Preform for 2L bottles.", factory));
        materials.put("RM-PREFORM-500-001",   ensureMaterial("PET Preform 500ml",         "RM-PREFORM-500-001",  "Each",    "pcs",  164000, 12, 8, "Preform for 500ml bottles.", factory));
        materials.put("RM-PREFORM-1_5L-001",  ensureMaterial("PET Preform 1.5L",          "RM-PREFORM-1_5L-001", "Each",    "pcs",  94000,  12, 9, "Preform for 1.5L bottles.", factory));
        materials.put("RM-CAP-28MM-001",      ensureMaterial("28mm Bottle Cap",           "RM-CAP-28MM-001",     "Each",    "pcs",  410000, 7,  10,"Tamper-evident closure cap.", factory));
        materials.put("RM-LABEL-001",         ensureMaterial("Wraparound Label Film",     "RM-LABEL-001",        "Roll",    "rolls",960,    9,  11,"Shrink/label film for bottle branding.", factory));
        materials.put("RM-CARTON-001",        ensureMaterial("Corrugated Carton Case",    "RM-CARTON-001",       "Each",    "pcs",  73000,  8,  12,"Transport cartons for finished product distribution.", factory));
        materials.put("RM-POTATO-001",        ensureMaterial("Dehydrated Potato Flakes",  "RM-POTATO-001",       "25kg",    "kg",   26000,  6,  13,"Snack base input for crisp production.", factory));
        materials.put("RM-MAIZE-001",         ensureMaterial("Maize Grits",               "RM-MAIZE-001",        "50kg",    "kg",   31000,  6,  14,"Corn snack base material.", factory));
        materials.put("RM-OIL-001",           ensureMaterial("Refined Vegetable Oil",     "RM-OIL-001",          "20L",     "L",    18000,  5,  15,"Cooking medium for snacks.", factory));
        materials.put("RM-SEASON-SALT-001",   ensureMaterial("Salt Seasoning Blend",      "RM-SEASON-SALT-001",  "20kg",    "kg",   7600,   5,  16,"Seasoning for salted and flavored snacks.", factory));
        materials.put("RM-FLOUR-001",         ensureMaterial("Wheat Flour",               "RM-FLOUR-001",        "50kg",    "kg",   22000,  6,  17,"Bakery flour for biscuit production.", factory));
        materials.put("RM-SHORTEN-001",       ensureMaterial("Bakery Shortening",         "RM-SHORTEN-001",      "25kg",    "kg",    8400,  7,  18,"Fat component for biscuit dough.", factory));
        materials.put("RM-SUGAR-ICING-001",   ensureMaterial("Icing Sugar",               "RM-SUGAR-ICING-001",  "25kg",    "kg",    9200,  5,  19,"Sugar component for cream biscuit filling.", factory));
        materials.put("RM-BISCUIT-FLVR-001",  ensureMaterial("Vanilla Cream Flavor",      "RM-BISCUIT-FLVR-001", "10kg",    "kg",    1250,  11, 20,"Flavor profile input for cream biscuits.", factory));
        materials.put("RM-BISCUIT-FILM-001",  ensureMaterial("Biscuit Packaging Film",    "RM-BISCUIT-FILM-001", "Roll",    "rolls", 740,   9,  21,"Packaging roll stock for biscuit packs.", factory));

        return materials;
    }

    private RawMaterials ensureMaterial(String name, String code, String size, String uom, double quantity, int leadTime, int sequence, String description, MainEntity factory) {
        RawMaterials existing = ntMsRepository.findByCode(code);
        if (existing != null) {
            return existing;
        }

        RawMaterials material = new RawMaterials(name, code, size, description, quantity, uom, leadTime, sequence);
        material.setMain_entity_material(factory);
        return ntMsRepository.save(material);
    }

    private Map<String, Product> seedFinishedProducts(MainEntity factory) {
        Map<String, Product> products = new HashMap<>();

        products.put("PRD-COLA-2L",     ensureProduct("Zambezi Cola 2L",             "PRD-COLA-2L",     "2L",    "bottles", 12000, "Carbonated cola beverage in 2L retail pack.", factory));
        products.put("PRD-ORANGE-500",  ensureProduct("CoolBurst Orange 500ml",       "PRD-ORANGE-500",  "500ml", "bottles", 24000, "Fruit-flavored drink for route and cooler channels.", factory));
        products.put("PRD-ENERGY-500",  ensureProduct("Spark Energy Drink 500ml",     "PRD-ENERGY-500",  "500ml", "bottles", 18000, "Energy beverage for convenience and kiosk trade.", factory));
        products.put("PRD-WATER-1_5L",  ensureProduct("AquaPure Still Water 1.5L",    "PRD-WATER-1_5L",  "1.5L",  "bottles", 20000, "Still bottled water for FMCG retail movement.", factory));
        products.put("PRD-CRISPS-50",   ensureProduct("CrunchMax Salted Crisps 50g",  "PRD-CRISPS-50",   "50g",   "packs",   30000, "Salted potato snack in impulse-size format.", factory));
        products.put("PRD-BISCUIT-150", ensureProduct("SweetBite Cream Biscuits 150g","PRD-BISCUIT-150", "150g",  "packs",   22000, "Cream-filled biscuit for general trade outlets.", factory));

        return products;
    }

    private Product ensureProduct(String name, String code, String size, String uom, double quantity, String description, MainEntity factory) {
        Product existing = productRepository.findByCode(code);
        if (existing != null) {
            return existing;
        }

        Product product = new Product(name, code, size, description, quantity, uom);
        product.setMain_entity_product(factory);
        return productRepository.save(product);
    }

    private void seedBillOfMaterialUsage(Map<String, Product> products, Map<String, RawMaterials> materials) {
        for (Product product : products.values()) {
            Collection<MaterialUsage> existing = materialUsageRepository.findMaterialUsageByProduct(product);
            if (!existing.isEmpty()) {
                continue;
            }

            if ("PRD-COLA-2L".equals(product.getCode())) {
                addUsage(product, materials.get("RM-WATER-001"),       78.0);
                addUsage(product, materials.get("RM-SUGAR-001"),       10.0);
                addUsage(product, materials.get("RM-COLA-FLV-001"),     0.8);
                addUsage(product, materials.get("RM-CO2-001"),          0.2);
                addUsage(product, materials.get("RM-PREFORM-2L-001"),   4.5);
                addUsage(product, materials.get("RM-CAP-28MM-001"),     1.0);
                addUsage(product, materials.get("RM-LABEL-001"),        1.0);
                addUsage(product, materials.get("RM-CARTON-001"),       4.5);
            } else if ("PRD-ORANGE-500".equals(product.getCode())) {
                addUsage(product, materials.get("RM-WATER-001"),       74.0);
                addUsage(product, materials.get("RM-SUGAR-001"),        9.5);
                addUsage(product, materials.get("RM-ORANGE-FLV-001"),   1.0);
                addUsage(product, materials.get("RM-PREFORM-500-001"),  6.0);
                addUsage(product, materials.get("RM-CAP-28MM-001"),     1.5);
                addUsage(product, materials.get("RM-LABEL-001"),        2.0);
                addUsage(product, materials.get("RM-CARTON-001"),       6.0);
            } else if ("PRD-ENERGY-500".equals(product.getCode())) {
                addUsage(product, materials.get("RM-WATER-001"),       72.0);
                addUsage(product, materials.get("RM-SUGAR-001"),        8.0);
                addUsage(product, materials.get("RM-ENERGY-BASE-001"),  3.0);
                addUsage(product, materials.get("RM-CO2-001"),          0.5);
                addUsage(product, materials.get("RM-PREFORM-500-001"),  6.0);
                addUsage(product, materials.get("RM-CAP-28MM-001"),     1.5);
                addUsage(product, materials.get("RM-LABEL-001"),        2.0);
                addUsage(product, materials.get("RM-CARTON-001"),       7.0);
            } else if ("PRD-WATER-1_5L".equals(product.getCode())) {
                addUsage(product, materials.get("RM-WATER-001"),       92.0);
                addUsage(product, materials.get("RM-PREFORM-1_5L-001"), 3.5);
                addUsage(product, materials.get("RM-CAP-28MM-001"),     1.5);
                addUsage(product, materials.get("RM-LABEL-001"),        1.0);
                addUsage(product, materials.get("RM-CARTON-001"),       2.0);
            } else if ("PRD-CRISPS-50".equals(product.getCode())) {
                addUsage(product, materials.get("RM-POTATO-001"),      52.0);
                addUsage(product, materials.get("RM-MAIZE-001"),       18.0);
                addUsage(product, materials.get("RM-OIL-001"),         12.0);
                addUsage(product, materials.get("RM-SEASON-SALT-001"),  5.0);
                addUsage(product, materials.get("RM-BISCUIT-FILM-001"), 6.0);
                addUsage(product, materials.get("RM-CARTON-001"),       7.0);
            } else if ("PRD-BISCUIT-150".equals(product.getCode())) {
                addUsage(product, materials.get("RM-FLOUR-001"),       45.0);
                addUsage(product, materials.get("RM-SHORTEN-001"),     14.0);
                addUsage(product, materials.get("RM-SUGAR-ICING-001"), 16.0);
                addUsage(product, materials.get("RM-BISCUIT-FLVR-001"), 2.0);
                addUsage(product, materials.get("RM-BISCUIT-FILM-001"), 8.0);
                addUsage(product, materials.get("RM-CARTON-001"),      15.0);
            }
        }
    }

    private void addUsage(Product product, RawMaterials material, double usagePerMC) {
        MaterialUsage usage = new MaterialUsage(material, product, "Usage per MC (50 units) in BOM", usagePerMC);
        materialUsageRepository.save(usage);
    }

    private void seedMaterialStockSnapshots(Map<String, Product> products, Map<String, RawMaterials> materials) {
        for (Product product : products.values()) {
            Collection<MaterialStockCount> snapshots = materialStockCountRepository.findMaterialStockCountByProduct(product);
            if (!snapshots.isEmpty()) {
                continue;
            }

            MaterialStockCount snapshot = new MaterialStockCount();
            snapshot.setProduct_production_run(product);
            snapshot.setCount_date(LocalDate.now().minusDays(1));
            snapshot.setSummary_comments("Demo stock count for production planning and allowable material drawdown.");
            snapshot = materialStockCountRepository.save(snapshot);

            for (MaterialUsage usage : materialUsageRepository.findMaterialUsageByProduct(product)) {
                RawMaterials material = usage.getRawMaterialUsage();
                double stockBase = Math.max(usage.getQuantity() * 100, 500);
                MaterialCount count = new MaterialCount(material, snapshot, product, "Available raw material stock for demo", stockBase);
                materialCountRepository.save(count);
            }

            RawMaterials carton = materials.get("RM-CARTON-001");
            MaterialCount cartonReserve = new MaterialCount(carton, snapshot, product, "Reserve packaging stock", 1200);
            materialCountRepository.save(cartonReserve);
        }
    }

    private void seedProductionAndTransfers(Map<String, Product> products, Map<String, RawMaterials> materials) {
        // Realistic per-product waste factors: 1.5% – 5% above standard usage per MC
        Map<String, Double> wasteFactors = new HashMap<>();
        wasteFactors.put("PRD-COLA-2L",     1.028); // 2.8% waste
        wasteFactors.put("PRD-ORANGE-500",  1.033); // 3.3% waste
        wasteFactors.put("PRD-ENERGY-500",  1.021); // 2.1% waste
        wasteFactors.put("PRD-WATER-1_5L",  1.015); // 1.5% waste
        wasteFactors.put("PRD-CRISPS-50",   1.045); // 4.5% waste
        wasteFactors.put("PRD-BISCUIT-150", 1.038); // 3.8% waste

        // Multiple demo production run date windows spread across the reporting period
        List<LocalDate[]> runWindows = Arrays.asList(
                new LocalDate[]{LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 14)},
                new LocalDate[]{LocalDate.of(2026, 3, 17), LocalDate.of(2026, 3, 21)},
                new LocalDate[]{LocalDate.of(2026, 4,  7), LocalDate.of(2026, 4, 11)},
                new LocalDate[]{LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)}
        );

        for (Product product : products.values()) {
            double wf = wasteFactors.getOrDefault(product.getCode(), 1.03);
            Collection<ProductionRun> existingRuns = productionRunRepository.findProductionRunByProduct(product);

            if (existingRuns.isEmpty()) {
                // Seed multiple production runs spread across the demo reporting window
                int runIdx = 1;
                for (LocalDate[] window : runWindows) {
                    double runQty = Math.max(product.getQuantity() * (0.4 + 0.05 * runIdx), 3000);
                    ProductionRun run = new ProductionRun(
                            product,
                            "Demo production run " + runIdx + " – " + product.getName(),
                            runQty,
                            window[0],
                            window[1],
                            "Completed",
                            "Yield within allowable limits and waste tracked for reporting.",
                            "RUN-" + product.getCode() + "-" + runIdx,
                            "/demo/runs/" + product.getCode().toLowerCase(Locale.ROOT)
                    );
                    run = productionRunRepository.save(run);

                    Collection<MaterialUsage> usages = materialUsageRepository.findMaterialUsageByProduct(product);
                    for (MaterialUsage usage : usages) {
                        // Correct formula: usage_per_MC * (runQty / 50) * waste_factor
                        double consumed = usage.getQuantity() * (run.getQuantity() / 50.0) * wf;
                        ProductionMaterialUsage pmu = new ProductionMaterialUsage(
                                usage.getRawMaterialUsage(),
                                run,
                                product,
                                "Actual usage from per-MC BOM profile",
                                Math.round(consumed * 100.0) / 100.0
                        );
                        productionMaterialUsageRepository.save(pmu);
                    }
                    runIdx++;
                }
            } else {
                // Repair any existing demo runs that were seeded with the old (incorrect)
                // percentage formula: consumed = runQty * usagePercent / 100
                // Correct formula: consumed = usagePercent_per_MC * runQty / 50 * wasteFactor
                for (ProductionRun existingRun : existingRuns) {
                    if (existingRun.getName() != null && existingRun.getName().startsWith("RUN-")) {
                        repairDemoMaterialUsage(existingRun, product, wf);
                    }
                }
            }

            Collection<FinishedProductMovement> movements = finishedProductMovementRepository.findFinishedProductMovementByProduct(product);
            if (movements.isEmpty()) {
                FinishedProductMovement transfer = new FinishedProductMovement(
                        "TRF-" + product.getCode(),
                        product,
                        "Transfer to Sales Deep Dive distribution warehouse for route sales allocation.",
                        Math.max(product.getQuantity() * 0.35, 2000),
                        LocalDate.now()
                );
                finishedProductMovementRepository.save(transfer);
            }
        }
    }

    /**
     * Detects ProductionMaterialUsage records seeded with the old percentage formula
     * (consumed = runQty * usagePercent / 100) and replaces them with the correct
     * per-MC formula (consumed = usagePerMC * runQty / 50 * wasteFactor).
     * Only updates existing records – does not create missing ones.
     */
    private void repairDemoMaterialUsage(ProductionRun run, Product product, double wasteFactor) {
        Collection<MaterialUsage> usages = materialUsageRepository.findMaterialUsageByProduct(product);
        Collection<ProductionMaterialUsage> existingPMUs =
                productionMaterialUsageRepository.findProductionMaterialUsageByProductionRun(run);

        for (MaterialUsage usage : usages) {
            if (usage.getRawMaterialUsage() == null) continue;

            double correctConsumed = usage.getQuantity() * (run.getQuantity() / 50.0) * wasteFactor;
            double oldConsumed     = (run.getQuantity() * usage.getQuantity()) / 100.0;

            Optional<ProductionMaterialUsage> existingPMU = existingPMUs.stream()
                    .filter(p -> p.getRawMaterialUsage() != null
                            && Objects.equals(p.getRawMaterialUsage().getId(), usage.getRawMaterialUsage().getId()))
                    .findAny();

            if (existingPMU.isPresent()) {
                double current = existingPMU.get().getQuantity();
                // If current value is within 1% of the wrong (old) formula value, fix it
                boolean isUsingOldFormula = oldConsumed > 0
                        && Math.abs(current - oldConsumed) / oldConsumed < 0.01;
                if (isUsingOldFormula) {
                    existingPMU.get().setQuantity(Math.round(correctConsumed * 100.0) / 100.0);
                    existingPMU.get().setDescription("Actual usage from per-MC BOM profile (corrected)");
                    productionMaterialUsageRepository.save(existingPMU.get());
                }
            }
            // Note: missing records are not created here – only existing ones are fixed.
            // New production runs (empty runs) will get correct records from the main seed path.
        }
    }

    /**
     * Seeds demo purchase orders:
     *   - Several "Initiated" orders with past delivery dates (shows on Delayed PO tab)
     *   - Several "Delivered" orders within recent date ranges
     */
    private void seedDemoPurchaseOrders(MainEntity factory, MainEntity supplier) {
        // Use the first demo PO's unique file name as idempotency sentinel
        final String sentinelName = "DEMO-PO-9001-Sugar-Restock.pdf";
        if (purchaseOrderRepository.findByName(sentinelName) != null) {
            return; // already seeded
        }

        String desc = "Demo purchase order – seeded for reporting demonstration";
        LocalDate today = LocalDate.now();

        // --- Delayed (Initiated, delivery_date in the past — show on Delayed PO tab) ---
        saveDemoPO(factory, supplier, "RM-SUGAR-001",
                sentinelName,
                desc + " (Sugar restock, order 9001)", 4000, "Initiated",
                today.minusWeeks(10), today.minusWeeks(7), 9001, 0);

        saveDemoPO(factory, supplier, "RM-PREFORM-2L-001",
                "DEMO-PO-9002-PET-Preform-2L.pdf",
                desc + " (PET preform 2L, order 9002)", 60000, "Initiated",
                today.minusWeeks(9), today.minusWeeks(6), 9002, 0);

        saveDemoPO(factory, supplier, "RM-CO2-001",
                "DEMO-PO-9003-CO2-Cylinders.pdf",
                desc + " (CO2 supply, order 9003)", 500, "Initiated",
                today.minusWeeks(8), today.minusWeeks(4), 9003, 0);

        saveDemoPO(factory, supplier, "RM-CARTON-001",
                "DEMO-PO-9004-Cartons-Export.pdf",
                desc + " (Carton export, order 9004)", 20000, "Initiated",
                today.minusWeeks(6), today.minusWeeks(3), 9004, 0);

        saveDemoPO(factory, supplier, "RM-CAP-28MM-001",
                "DEMO-PO-9005-BottleCaps-28mm.pdf",
                desc + " (Bottle caps, order 9005)", 150000, "Initiated",
                today.minusWeeks(5), today.minusWeeks(2), 9005, 0);

        // --- Delivered (within the 2026-02-01 to today reporting window) ---
        saveDemoPO(factory, supplier, "RM-WATER-001",
                "DEMO-PO-9006-Process-Water-Q1.pdf",
                desc + " (Process water Q1, order 9006)", 120000, "Delivered",
                LocalDate.of(2026, 2, 5), LocalDate.of(2026, 2, 12), 9006, 120000);

        saveDemoPO(factory, supplier, "RM-COLA-FLV-001",
                "DEMO-PO-9007-Cola-Flavour.pdf",
                desc + " (Cola flavour, order 9007)", 2000, "Delivered",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 10), 9007, 2000);

        saveDemoPO(factory, supplier, "RM-ORANGE-FLV-001",
                "DEMO-PO-9008-Orange-Flavour.pdf",
                desc + " (Orange flavour, order 9008)", 1800, "Delivered",
                LocalDate.of(2026, 4, 2), LocalDate.of(2026, 4, 9), 9008, 1800);
    }

    private void saveDemoPO(MainEntity factory, MainEntity supplier, String materialCode,
                             String fileName, String description,
                             double quantity, String status,
                             LocalDate orderDate, LocalDate deliveryDate,
                             int orderNumber, double deliveredQty) {
        // Reload ALL associations fresh from DB to avoid detached-entity Hibernate issues
        MainEntity freshFactory  = mainEntityRepository.findById(factory.getId()).orElse(null);
        MainEntity freshSupplier = mainEntityRepository.findById(supplier.getId()).orElse(null);
        RawMaterials material    = ntMsRepository.findByCode(materialCode);
        if (freshFactory == null || freshSupplier == null || material == null) return;

        PurchaseOrder po = new PurchaseOrder();
        po.setMain_entity_po(freshFactory);
        po.setSupplier(freshSupplier);
        po.setNtMs(material);
        po.setName(fileName);
        po.setDescription(description);
        po.setQuantity(quantity);
        po.setStatus(status);
        po.setUrl("demo/purchase-orders/" + fileName);
        po.setOrder_number(orderNumber);
        po.setOrder_date(orderDate);
        po.setDelivery_date(deliveryDate);
        po.setDelivered_quantity(deliveredQty);
        purchaseOrderRepository.save(po);
    }
}
