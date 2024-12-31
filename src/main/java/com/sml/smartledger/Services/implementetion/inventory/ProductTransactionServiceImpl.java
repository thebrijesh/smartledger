package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.inventory.ProductRepository;
import com.sml.smartledger.Repository.inventory.ProductTransactionRepository;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductTransactionServiceImpl implements ProductTransactionService {
    ProductTransactionRepository productTransactionRepository;
    ProductService productService;
    ProductRepository productRepository;
    Logger logger = org.slf4j.LoggerFactory.getLogger(ProductTransactionServiceImpl.class);

    BusinessRepository businessRepository;
    @Autowired
    public ProductTransactionServiceImpl( ProductService productService,BusinessRepository businessRepository , ProductTransactionRepository productTransactionRepository, ProductRepository productRepository) {
        this.productTransactionRepository = productTransactionRepository;
        this.productService = productService;
        this.businessRepository = businessRepository;
        this.productRepository = productRepository;
    }
    @Override
    public void addProductTransaction(ProductTransaction productTransaction) {
        Optional<Product> billProductOptional = productRepository.findById(productTransaction.getProduct().getId());
        if(billProductOptional.isEmpty()) throw new RuntimeException("Product not found");
        Product billProduct = billProductOptional.get();
        productTransactionRepository.save(productTransaction);
        if(productTransaction.getStockTransactionType() == StockTransactionType.IN){
             int quantity = billProduct.getStockQuantity();
             billProduct.setStockQuantity(billProduct.getStockQuantity()+productTransaction.getUnit());
             billProduct.setPurchasePrice(((billProduct.getPurchasePrice()*quantity)+(productTransaction.getAmount()*productTransaction.getUnit()))/billProduct.getStockQuantity());

        }else{
            billProduct.setStockQuantity(billProduct.getStockQuantity()-productTransaction.getUnit());
        }
        productService.updateBusiness(0,billProduct.getStockQuantity(),billProduct);
        productService.updateProduct(billProduct);
    }

    @Override
    public List<ProductTransaction> getAllProductTransactionByProductId(Long productId) {
        return productTransactionRepository.findAllProductTransactionByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteProductTransaction(Long id) {
        ProductTransaction productTransactionDB = productTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Transaction not found"));
        Product product = productTransactionDB.getProduct();

        int oldQuantity = product.getStockQuantity();
        if (productTransactionDB.getStockTransactionType().equals(StockTransactionType.IN)) {
            double totalAmount = product.getPurchasePrice() * oldQuantity;
            double totalTransactionAmount = productTransactionDB.getAmount() * productTransactionDB.getUnit();
            product.setStockQuantity(oldQuantity - productTransactionDB.getUnit());
            double purchasePrice = (totalAmount - totalTransactionAmount) / (oldQuantity - productTransactionDB.getUnit());
            if (Double.isNaN(purchasePrice)) {
                purchasePrice = 0d;
            }
            product.setPurchasePrice(purchasePrice);
        } else {
            product.setStockQuantity(product.getStockQuantity() + productTransactionDB.getUnit());
        }
        productService.updateBusiness(oldQuantity,0,product);
        try {
            productTransactionRepository.deleteById(id);
            productService.updateProduct(product);
        } catch (Exception e) {
            logger.error("Error deleting product transaction with id {}", id, e);
            throw new RuntimeException("Error deleting product transaction: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateProductTransaction(ProductTransaction productTransaction) {
        ProductTransaction productTransactionDB = productTransactionRepository.findById(productTransaction.getId()).orElseThrow(() -> new RuntimeException("Product Transaction not found"));
        Product product = productTransactionDB.getProduct();
     int oldQuantity = product.getStockQuantity();
        if (productTransaction.getStockTransactionType().equals(StockTransactionType.IN)) {
            int quantity = product.getStockQuantity();
            double totalAmount = product.getPurchasePrice() * quantity;
            double totalTransactionAmount = productTransactionDB.getAmount() * productTransactionDB.getUnit();
            product.setStockQuantity(quantity - productTransactionDB.getUnit());
            double purchasePrice = (totalAmount - totalTransactionAmount) / (quantity - productTransactionDB.getUnit());
            if (Double.isNaN(purchasePrice)) {
                // Handle the NaN case
                System.out.println("Result is NaN");
                purchasePrice = 0d;
            }
            product.setPurchasePrice(purchasePrice);


            int updatedQuantity = product.getStockQuantity();
            product.setStockQuantity(product.getStockQuantity()+productTransaction.getUnit());
            product.setPurchasePrice(((product.getPurchasePrice()*updatedQuantity)+(productTransaction.getAmount()*productTransaction.getUnit()))/product.getStockQuantity());

        } else {
            product.setStockQuantity(product.getStockQuantity() + productTransactionDB.getUnit());

            product.setStockQuantity(product.getStockQuantity()-productTransaction.getUnit());
        }

        productService.updateBusiness(oldQuantity,product.getStockQuantity(),product);

        productService.updateProduct(product);
        productTransactionDB.setAmount(productTransaction.getAmount());
        productTransactionDB.setProduct(product);
        productTransactionDB.setDate(productTransaction.getDate());
        productTransactionDB.setUnit(productTransaction.getUnit());
        productTransactionDB.setStockTransactionType(productTransaction.getStockTransactionType());
        productTransactionDB.setDescription(productTransaction.getDescription());

        productTransactionRepository.save(productTransactionDB);
    }

    @Override
    public ProductTransaction getProductTransactionById(Long id) {
        return productTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Transaction not found"));
    }


}
