package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.inventory.ProductTransaction;

import java.util.List;

public interface ProductTransactionService {
     ProductTransaction addProductTransaction(ProductTransaction productTransaction);
     List<ProductTransaction> getAllProductTransactionByProductId(Long productId);
     void deleteProductTransaction(Long id);

}
