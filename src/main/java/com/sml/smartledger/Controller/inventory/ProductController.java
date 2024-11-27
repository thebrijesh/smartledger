package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Model.bill.BillProduct;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/add")
    public ResponseEntity<BillProduct> addProduct(@RequestBody BillProduct product){
        BillProduct billProduct = productService.addProduct(product);
        return new ResponseEntity<>(billProduct, HttpStatus.CREATED);

    }
    @GetMapping("/{billId}")
    public ResponseEntity<List<BillProduct>> getAllProductByBusinessId(@PathVariable("billId") Long businessId){
        List<BillProduct> billProductList = productService.getAllProductByBusinessId(businessId);
        return new ResponseEntity<>(billProductList,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BillProduct> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
