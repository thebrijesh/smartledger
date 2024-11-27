package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.bill.BillProduct;

import java.util.List;

public interface ProductService {

    public BillProduct addProduct(BillProduct product);
    public List<BillProduct> getAllProductByBusinessId(Long billId);
    public void  deleteProduct(Long id);
}
