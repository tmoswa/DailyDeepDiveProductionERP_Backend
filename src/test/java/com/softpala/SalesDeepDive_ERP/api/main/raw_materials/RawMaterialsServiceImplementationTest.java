package com.softpala.SalesDeepDive_ERP.api.main.raw_materials;

import com.softpala.SalesDeepDive_ERP.api.main.inc.ActivityLogService;
import com.softpala.SalesDeepDive_ERP.api.main.material_usage.MaterialUsageServiceImplementation;
import com.softpala.SalesDeepDive_ERP.api.main.product.ProductServiceImplementation;
import com.softpala.SalesDeepDive_ERP.api.main.production_run.ProductionRunServiceImplementation;
import com.softpala.SalesDeepDive_ERP.api.main.purchase_order.PurchaseOrderServiceImplementation;
import com.softpala.SalesDeepDive_ERP.persistence.dao.MaterialUsageRepository;
import com.softpala.SalesDeepDive_ERP.persistence.dao.ProductRepository;
import com.softpala.SalesDeepDive_ERP.persistence.dao.ProductionMaterialUsageRepository;
import com.softpala.SalesDeepDive_ERP.persistence.dao.RawMaterialsRepository;
import com.softpala.SalesDeepDive_ERP.persistence.model.MainEntity;
import com.softpala.SalesDeepDive_ERP.persistence.model.MaterialUsage;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;
import com.softpala.SalesDeepDive_ERP.persistence.model.RawMaterials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RawMaterialsServiceImplementationTest {

    @Mock
    private RawMaterialsRepository rawMaterialsRepository;
    @Mock
    private ProductionMaterialUsageRepository productionMaterialUsageRepository;
    @Mock
    private ProductionRunServiceImplementation productionRunServiceImplementation;
    @Mock
    private MaterialUsageServiceImplementation materialUsageServiceImplementation;
    @Mock
    private PurchaseOrderServiceImplementation purchaseOrderServiceImplementation;
    @Mock
    private ActivityLogService activityLogService;
    @Mock
    private ProductServiceImplementation productServiceImplementation;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MaterialUsageRepository materialUsageRepository;

    @Test
    void completeNtmsUsed_skipsMissingRawMaterialReferences() {
        RawMaterialsServiceImplementation service = spy(new RawMaterialsServiceImplementation(
                rawMaterialsRepository,
                productionMaterialUsageRepository,
                productionRunServiceImplementation,
                materialUsageServiceImplementation,
                purchaseOrderServiceImplementation,
                activityLogService,
                productServiceImplementation,
                productRepository,
                materialUsageRepository
        ));

        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to = LocalDate.of(2026, 5, 21);

        MainEntity entity = new MainEntity("Factory A");
        entity.setId(3L);

        Product product = new Product();
        product.setId(7L);
        product.setName("Blue Kings");
        product.setMain_entity_product(entity);

        RawMaterials rawMaterial = new RawMaterials();
        rawMaterial.setId(11L);
        rawMaterial.setName("Tear Tape");
        rawMaterial.setCode("RM-11");
        rawMaterial.setSize("Small");
        rawMaterial.setDescription("Raw material for test");
        rawMaterial.setLead_time(5);
        rawMaterial.setUnit_of_measure("roll");
        rawMaterial.setSequence(1);
        rawMaterial.setMain_entity_material(entity);

        MaterialUsage orphanUsage = new MaterialUsage();
        orphanUsage.setId(101L);
        orphanUsage.setProduct_usage(product);
        orphanUsage.setDescription("Missing raw material reference");
        orphanUsage.setQuantity(1.0);
        orphanUsage.setRawMaterialUsage(null);

        MaterialUsage validUsage = new MaterialUsage();
        validUsage.setId(102L);
        validUsage.setProduct_usage(product);
        validUsage.setDescription("Valid raw material usage");
        validUsage.setQuantity(2.5);
        validUsage.setRawMaterialUsage(rawMaterial);

        rawMaterialsUsed usedDuringPeriod = new rawMaterialsUsed();
        usedDuringPeriod.setId(rawMaterial.getId());
        usedDuringPeriod.setName(rawMaterial.getName());
        usedDuringPeriod.setCode(rawMaterial.getCode());
        usedDuringPeriod.setSize(rawMaterial.getSize());
        usedDuringPeriod.setDescription(rawMaterial.getDescription());
        usedDuringPeriod.setLead_time(rawMaterial.getLead_time());
        usedDuringPeriod.setUnit_of_measure(rawMaterial.getUnit_of_measure());
        usedDuringPeriod.setMain_entity_material(entity);
        usedDuringPeriod.setQuantity(5.0);

        rawMaterialsUsed openingBalance = new rawMaterialsUsed();
        openingBalance.setId(rawMaterial.getId());
        openingBalance.setName(rawMaterial.getName());
        openingBalance.setCode(rawMaterial.getCode());
        openingBalance.setSize(rawMaterial.getSize());
        openingBalance.setDescription(rawMaterial.getDescription());
        openingBalance.setLead_time(rawMaterial.getLead_time());
        openingBalance.setUnit_of_measure(rawMaterial.getUnit_of_measure());
        openingBalance.setMain_entity_material(entity);
        openingBalance.setQuantity(20.0);

        rawMaterialsUsed closingBalance = new rawMaterialsUsed();
        closingBalance.setId(rawMaterial.getId());
        closingBalance.setName(rawMaterial.getName());
        closingBalance.setCode(rawMaterial.getCode());
        closingBalance.setSize(rawMaterial.getSize());
        closingBalance.setDescription(rawMaterial.getDescription());
        closingBalance.setLead_time(rawMaterial.getLead_time());
        closingBalance.setUnit_of_measure(rawMaterial.getUnit_of_measure());
        closingBalance.setMain_entity_material(entity);
        closingBalance.setQuantity(15.0);

        ProductServiceImplementation.ProducedProduct producedProduct = mock(ProductServiceImplementation.ProducedProduct.class);
        when(producedProduct.getId()).thenReturn(product.getId());
        when(producedProduct.getQuantity()).thenReturn(50.0);

        doReturn(List.of(closingBalance)).when(service).ntmsList(anyInt());
        doReturn(List.of(openingBalance)).when(service).openingBalance(any(LocalDate.class));
        doReturn(List.of(usedDuringPeriod)).when(service).ntmsUsedList(any(LocalDate.class), any(LocalDate.class), anyInt(), eq(true), eq(product));

        when(materialUsageRepository.findMaterialUsageByProduct(product)).thenReturn(List.of(orphanUsage, validUsage));
        when(purchaseOrderServiceImplementation.totalPurchaseOrderList(anyInt())).thenReturn(Collections.emptyList());
        when(productServiceImplementation.producedList(from, to, 100)).thenReturn(List.of(producedProduct));
        when(materialUsageServiceImplementation.materialUsageList(product.getId())).thenReturn(List.of(orphanUsage, validUsage));

        Collection<RawMaterialsServiceImplementation.completeNtmsUsed> result = assertDoesNotThrow(
                () -> service.completeNtmsUsed(from, to, product, 100)
        );

        assertEquals(1, result.size());

        RawMaterialsServiceImplementation.completeNtmsUsed summary = result.iterator().next();
        assertEquals(rawMaterial.getId(), summary.getNtmsUsed().getId());
        assertEquals(2.5, summary.getUsage_per_case());
        assertEquals(15.0, summary.getClosing_balance());
        assertEquals(50.0, summary.getProduced_quantity());
    }
}

