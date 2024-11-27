package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.bill.BillProduct;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.inventory.ProductRepository;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BusinessRepository businessRepository;
    @Override
    public BillProduct addProduct(BillProduct product) {
        Optional<Business> businessOptional = businessRepository.findById(product.getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        BillProduct savedBillProduct = productRepository.save(product);
        business.getProducts().add(savedBillProduct);
        businessRepository.save(business);
        return savedBillProduct;
    }

    @Override
    public List<BillProduct> getAllProductByBusinessId(Long businessId) {
        return productRepository.getAllProductByBusinessId(businessId);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
