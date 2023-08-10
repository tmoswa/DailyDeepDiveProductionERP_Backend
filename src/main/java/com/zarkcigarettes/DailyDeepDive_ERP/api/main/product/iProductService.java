package com.zarkcigarettes.DailyDeepDive_ERP.api.main.product;


import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.MaterialUsage;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.NTMs;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Product;

import java.util.Collection;

public interface iProductService {

    Collection<Product> productList(int limit);
    Collection<Product> productListByMainEntity(Long mainEntityID);
    Product saveNTMs(Product product);
    boolean deleteProduct(Long id);
    boolean updateProduct(Long id, Product product);
}
