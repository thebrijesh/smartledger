package com.sml.smartledger.Controller;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class ApiController {
    final PartyTransactionService partyTransactionService;
    PartyService partyService;
    BusinessService businessService;
    ProductTransactionService productTransactionService;
    ProductService productService;
    @Autowired
    public ApiController(PartyTransactionService partyTransactionService , PartyService partyService, BusinessService businessService, ProductTransactionService productTransactionService, ProductService productService) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
        this.businessService = businessService;
        this.productTransactionService = productTransactionService;
        this.productService = productService;
    }

    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id){
        partyTransactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("delete-party/{id}")
    public ResponseEntity<Void> deleteParty(@PathVariable("id") Long id) {
        partyService.deleteParty(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("delete-product-transactions/{id}")
    public ResponseEntity<Void> deleteProductTransactions(@PathVariable("id") Long id) {
        ProductTransaction productTransaction = productTransactionService.getProductTransactionById(id);
        Product product = productTransaction.getProduct();
        if(productTransaction.getStockTransactionType().equals(StockTransactionType.IN)) {
            int quantity = product.getStockQuantity();
            System.out.println("quantity " + quantity);
            double totalAmount = product.getPurchasePrice() * quantity;
            System.out.println("totalAmount " + totalAmount);
            double totalTransactionAmount = productTransaction.getAmount() * productTransaction.getUnit();
            System.out.println(totalTransactionAmount);
            product.setStockQuantity(quantity - productTransaction.getUnit());
            double purchasePrice = (totalAmount - totalTransactionAmount) / (quantity - productTransaction.getUnit());
            System.out.println(purchasePrice);
            if (Double.isNaN(purchasePrice)) {
                // Handle the NaN case
                System.out.println("Result is NaN");
                purchasePrice = 0d;
            }
            product.setPurchasePrice(purchasePrice);
        } else {
            product.setStockQuantity(product.getStockQuantity() + productTransaction.getUnit());
        }
        productService.addProduct(product);
        productTransactionService.deleteProductTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-all-transaction/{id}")
    public ResponseEntity<List<PartyTransaction>> getAllTransaction(@PathVariable("id") Long id){
        Business business =  businessService.getBusinessById(id);
        return new ResponseEntity<>(partyTransactionService.getAllTransactionsByPartyIds( business.getParties()), HttpStatus.OK);
    }

}
