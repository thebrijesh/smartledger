package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Forms.ProductTransactionForm;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("users/inventory/productTransaction")
public class ProductTransactionController {

    ProductTransactionService productTransactionService;
    ProductService productService;
    BusinessService businessService;

    public ProductTransactionController(ProductTransactionService productTransactionService, ProductService productService, BusinessService businessService) {
        this.productTransactionService = productTransactionService;
        this.productService = productService;
        this.businessService = businessService;
    }

    @PostMapping("/add")
    public String addTransaction(@ModelAttribute ProductTransactionForm productTransactionForm) {

        Product product = productService.getProductById(productTransactionForm.getProductId());
        Business business = product.getBusiness();
        if (isProductLowStock(product)) {
            business.setLowStockProducts(business.getLowStockProducts() - 1);
        }
        ProductTransaction productTransaction = ProductTransaction.builder()
                .amount(productTransactionForm.getAmount())
                .description(productTransactionForm.getDescription())
                .product(product)
                .date(Helper.combineDate(productTransactionForm.getDate(), new Date()))
                .unit(productTransactionForm.getStockQuantity())
                .stockTransactionType(StockTransactionType.valueOf(productTransactionForm.getTransactionType()))
                .description(productTransactionForm.getDescription())
                .build();

        productTransactionService.addProductTransaction(productTransaction);
        if(isProductLowStock(product)){
            business.setLowStockProducts(business.getLowStockProducts() + 1);
        }

        businessService.saveBusiness(business);
        return "redirect:/users/inventory/products/details/" + product.getId();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductTransaction>> getAllProductTransactionByProductId(@PathVariable("productId") Long productId) {
        List<ProductTransaction> productTransactions = productTransactionService.getAllProductTransactionByProductId(productId);
        return new ResponseEntity<>(productTransactions, HttpStatus.OK);
    }

    @DeleteMapping("/delete-product-transactions/{id}")
    public ResponseEntity<Void> deleteProductTransactions(@PathVariable("id") Long id) {
        ProductTransaction productTransaction = productTransactionService.getProductTransactionById(id);
        Product product = productTransaction.getProduct();
        Business business = product.getBusiness();
//        Product productDB = new Product();
//        productDB.setStockQuantity(product.getStockQuantity());
//        productDB.setSalePrice(product.getSalePrice());
        if(isProductLowStock(product)){
            business.setLowStockProducts(business.getLowStockProducts() - 1);
        }
        int oldQuantity = product.getStockQuantity();
        if (productTransaction.getStockTransactionType().equals(StockTransactionType.IN)) {
            double totalAmount = product.getPurchasePrice() * oldQuantity;
            double totalTransactionAmount = productTransaction.getAmount() * productTransaction.getUnit();
            product.setStockQuantity(oldQuantity - productTransaction.getUnit());
            double purchasePrice = (totalAmount - totalTransactionAmount) / (oldQuantity - productTransaction.getUnit());
            if (Double.isNaN(purchasePrice)) {
                purchasePrice = 0d;
            }
            product.setPurchasePrice(purchasePrice);
        } else {
           product.setStockQuantity(product.getStockQuantity() + productTransaction.getUnit());
        }
        productService.addProduct(product);
        productTransactionService.deleteProductTransaction(id);
        if (isProductLowStock(product)) {
            business.setLowStockProducts(business.getLowStockProducts() + 1);
            businessService.saveBusiness(business);

        }
//        business.setTotalProductsStock(business.getTotalProductsStock() + updateStockQuantity(productDB,productTransaction.getProduct()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public String updateTransaction(@ModelAttribute ProductTransactionForm productTransactionForm) {
        Product product = productService.getProductById(productTransactionForm.getProductId());
        ProductTransaction productTransactionDB = productTransactionService.getProductTransactionById(productTransactionForm.getId());
        Product productDB = new Product();
        productDB.setStockQuantity(product.getStockQuantity());
        productDB.setSalePrice(product.getSalePrice());
        Business business = product.getBusiness();
        if(isProductLowStock(product)){
            business.setLowStockProducts(business.getLowStockProducts() - 1);
        }
        ProductTransaction productTransaction = new ProductTransaction();
        productTransaction.setId(productTransactionForm.getId());
        productTransaction.setAmount(productTransactionForm.getAmount());
        productTransaction.setProduct(product);
        productTransaction.setDate(Helper.combineDate(productTransactionForm.getDate(), productTransactionDB.getCreatedAt()));
        productTransaction.setUnit(productTransactionForm.getStockQuantity());
        productTransaction.setStockTransactionType(StockTransactionType.valueOf(productTransactionForm.getTransactionType()));
        productTransaction.setDescription(productTransactionForm.getDescription());


        productTransactionService.updateProductTransaction(productTransaction);
        if(isProductLowStock(product)){
            business.setLowStockProducts(business.getLowStockProducts() + 1);
        }
        business.setTotalProductsStock(business.getTotalProductsStock() + updateStockQuantity(productDB,productTransactionDB.getProduct()));
        businessService.saveBusiness(business);
        return "redirect:/users/inventory/products/details/" + product.getId();
    }

    public boolean isProductLowStock(Product product){
        return product.getStockQuantity() < product.getLowStock();
    }

    public int updateStockQuantity(Product product,Product newProduct){
        int stockValue = 0;
        if(product.getSalePrice() != 0){
            stockValue -= ((int) (product.getStockQuantity() * product.getSalePrice()));
            System.out.println(stockValue);
        }
        if(newProduct.getSalePrice() != 0){
            stockValue += (int) (newProduct.getStockQuantity() * newProduct.getSalePrice());
            System.out.println(stockValue);
        }
        return stockValue;
    }


}
