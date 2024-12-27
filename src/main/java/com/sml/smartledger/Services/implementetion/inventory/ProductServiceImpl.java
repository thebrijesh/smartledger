package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Helper.AppConstants;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.UnitType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.inventory.ProductRepository;
import com.sml.smartledger.Repository.inventory.ProductTransactionRepository;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    BusinessRepository businessRepository;
    ProductTransactionRepository productTransactionRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BusinessRepository businessRepository, ProductTransactionRepository productTransactionRepository) {
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.productTransactionRepository = productTransactionRepository;
    }
    @Override
    public void addProduct(Product product) {
            Business business = product.getBusiness();
        business.setTotalProductsStock((int) (business.getTotalProductsStock() + ( product.getStockQuantity() * product.getSalePrice())));

        productRepository.save(product);
        businessRepository.save(business);

    }

    @Override
    public List<Product> getAllProductByBusinessId(Long businessId) {
        return productRepository.getAllProductByBusinessId(businessId);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
        Business business = product.getBusiness();
        business.setTotalProductsStock((int) (business.getTotalProductsStock() -( product.getStockQuantity() * product.getSalePrice())));
        productTransactionRepository.deleteAllByProductId(id);
        productRepository.deleteById(id);
        businessRepository.save(business);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
    }

    @Override
    public void updateProduct(Product product) {
        Optional<Product> productOptional = productRepository.findById(product.getId());
        Product productDB = productOptional.orElseThrow(() -> new RuntimeException("Product not found"));
        Business business = productDB.getBusiness();
        business.setTotalProductsStock((int) (business.getTotalProductsStock() -( productDB.getStockQuantity() * productDB.getSalePrice())));
        productDB.setName(product.getName());
        productDB.setSalePrice(product.getSalePrice());
        productDB.setLowStock(product.getLowStock());
        productDB.setUnitType(product.getUnitType());
        productDB.setPurchasePrice(product.getPurchasePrice());
        productDB.setDate(product.getDate());
        if(product.getImage() != null) {
            productDB.setCloudinaryImagePublicId(product.getCloudinaryImagePublicId());
            productDB.setImage(product.getImage());
        }
        business.setTotalProductsStock((int) (business.getTotalProductsStock() + ( productDB.getStockQuantity() * productDB.getSalePrice())));

        productRepository.save(productDB);
        businessRepository.save(business);
    }
}
