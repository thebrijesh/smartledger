package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productTransaction")
public class ProductTransactionController {
    @Autowired
    ProductTransactionService productTransactionService;
    @PostMapping("/add")
    public ResponseEntity<ProductTransaction> addTransaction(@RequestBody ProductTransaction productTransaction){
        ProductTransaction productTransaction1 = productTransactionService.addTransaction(productTransaction);
        return new ResponseEntity<>(productTransaction1, HttpStatus.CREATED);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductTransaction>> getAllProductTransactionByProductId(@PathVariable("productId") Long productId) {
        List<ProductTransaction> productTransactions = productTransactionService.getAllProductTransactionByProductId(productId);
        return new ResponseEntity<>(productTransactions,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductTransaction> deleteProductTransaction(@PathVariable Long id) {
        productTransactionService.deleteProductTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
