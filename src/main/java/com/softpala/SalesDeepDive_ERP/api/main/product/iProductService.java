package com.softpala.SalesDeepDive_ERP.api.main.product;


import com.softpala.SalesDeepDive_ERP.persistence.model.MaterialUsage;
import com.softpala.SalesDeepDive_ERP.persistence.model.RawMaterials;
import com.softpala.SalesDeepDive_ERP.persistence.model.Product;

import java.util.Collection;

public interface iProductService {

    Collection<Product> productList(int limit);
    Collection<Product> productListByMainEntity(Long mainEntityID);
    Product saveNTMs(Product product);
    boolean deleteProduct(Long id);
    boolean updateProduct(Long id, Product product);
}
