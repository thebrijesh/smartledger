package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.inventory.Product;

import java.util.List;

public interface ProductService {

    public Product addProduct(Product product);
    public List<Product> getAllProductByBusinessId(Long billId);
    public void  deleteProduct(Long id);
}
