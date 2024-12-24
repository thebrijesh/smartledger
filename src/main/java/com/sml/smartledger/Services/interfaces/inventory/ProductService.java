package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.inventory.Product;

import java.util.List;

public interface ProductService {

     void addProduct(Product product);
     List<Product> getAllProductByBusinessId(Long billId);
     void  deleteProduct(Long id);
        Product getProductById(Long id);
}
