package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.inventory.Product;
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
    ProductRepository productRepository;
    BusinessRepository businessRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BusinessRepository businessRepository) {
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
    }
    @Override
    public Product addProduct(Product product) {
        Optional<Business> businessOptional = businessRepository.findById(product.getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        Product savedBillProduct = productRepository.save(product);
        business.getProducts().add(savedBillProduct);
        businessRepository.save(business);
        return savedBillProduct;
    }

    @Override
    public List<Product> getAllProductByBusinessId(Long businessId) {
        return productRepository.getAllProductByBusinessId(businessId);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
