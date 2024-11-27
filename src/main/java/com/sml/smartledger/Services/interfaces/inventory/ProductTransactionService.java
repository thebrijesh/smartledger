package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.inventory.ProductTransaction;

import java.util.List;

public interface ProductTransactionService {
    public ProductTransaction addTransaction(ProductTransaction productTransaction);
    public List<ProductTransaction> getAllProductTransactionByProductId(Long productId);
    public void deleteProductTransaction(Long id);

}
