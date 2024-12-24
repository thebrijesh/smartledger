package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.inventory.ProductRepository;
import com.sml.smartledger.Repository.inventory.ProductTransactionRepository;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTransactionServiceImpl implements ProductTransactionService {
    ProductTransactionRepository productTransactionRepository;
    ProductRepository productRepository;

    BusinessRepository businessRepository;
    @Autowired
    public ProductTransactionServiceImpl( BusinessRepository businessRepository , ProductTransactionRepository productTransactionRepository, ProductRepository productRepository) {
        this.productTransactionRepository = productTransactionRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
    }
    @Override
    public ProductTransaction addProductTransaction(ProductTransaction productTransaction) {
        Optional<Product> billProductOptional = productRepository.findById(productTransaction.getProduct().getId());
        if(billProductOptional.isEmpty()) throw new RuntimeException("Product not found");
        Product billProduct = billProductOptional.get();
        Business business = billProduct.getBusiness();
        ProductTransaction savedproductTransaction = productTransactionRepository.save(productTransaction);

        if(productTransaction.getStockTransactionType() == StockTransactionType.IN){
             billProduct.setStockQuantity(billProduct.getStockQuantity()+productTransaction.getUnit());
             billProduct.setPurchasePrice((billProduct.getPurchasePrice()+(productTransaction.getAmount()*productTransaction.getUnit()))/billProduct.getStockQuantity());

             if(billProduct.getSalePrice() != 0){
                 business.setTotalProductsStock((int) (business.getTotalProductsStock() + (billProduct.getSalePrice() * productTransaction.getUnit())));
             }
        }else{
            billProduct.setStockQuantity(billProduct.getStockQuantity()-productTransaction.getUnit());
            if(billProduct.getSalePrice() != 0){
                business.setTotalProductsStock((int) (business.getTotalProductsStock() - (billProduct.getSalePrice() * productTransaction.getUnit())));
            }
        }

        productRepository.save(billProduct);
        businessRepository.save(business);
        return savedproductTransaction;
    }

    @Override
    public List<ProductTransaction> getAllProductTransactionByProductId(Long productId) {
        return productTransactionRepository.findAllProductTransactionByProductId(productId);
    }

    @Override
    public void deleteProductTransaction(Long id) {
        productTransactionRepository.deleteById(id);
    }


}
