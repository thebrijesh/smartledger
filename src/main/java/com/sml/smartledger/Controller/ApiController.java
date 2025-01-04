package com.sml.smartledger.Controller;

import com.sml.smartledger.Forms.PartyForm;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.*;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@RestController()
@RequestMapping("users/api")
public class ApiController {
    final PartyTransactionService partyTransactionService;
    PartyService partyService;
    BusinessService businessService;
    ProductTransactionService productTransactionService;
    ProductService productService;
    UserService userService;
Logger logger = LoggerFactory.getLogger(ApiController.class);
    @Autowired
    public ApiController(UserService userService, PartyTransactionService partyTransactionService, PartyService partyService, BusinessService businessService, ProductTransactionService productTransactionService, ProductService productService) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
        this.businessService = businessService;
        this.productTransactionService = productTransactionService;
        this.productService = productService;
        this.userService = userService;
    }
   @GetMapping("/get-bill-transactions/{id}")
public ResponseEntity<Map<String, List<?>>> getBillTransactions(@PathVariable("id") Long id) {

    Business business = businessService.getBusinessById(id);
    List<Product> productList = business.getProducts();
    List<Service> serviceList = business.getServices();

    List<ProductTransaction> productTransactionList = new ArrayList<>();
    List<ServiceTransaction> serviceTransactionList = new ArrayList<>();

    for (Product product : productList) {
        System.out.println("product: " + product);
        ProductTransaction productTransaction = new ProductTransaction();
        productTransaction.setProduct(product);
        productTransaction.setUnit(1);
        productTransactionList.add(productTransaction);
    }
    for (Service service : serviceList) {
        ServiceTransaction serviceTransaction = new ServiceTransaction();
        serviceTransaction.setService(service);
        serviceTransaction.setUnit(1);
        serviceTransactionList.add(serviceTransaction);
    }

    Map<String, List<?>> response = new HashMap<>();
    response.put("productTransactions", productTransactionList);
    response.put("serviceTransactions", serviceTransactionList);
       logger.info("response: {}", response);
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id) {
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
        if (productTransaction.getStockTransactionType().equals(StockTransactionType.IN)) {
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
    public ResponseEntity<List<PartyTransaction>> getAllTransaction(@PathVariable("id") Long id) {
        Business business = businessService.getBusinessById(id);
        return new ResponseEntity<>(partyTransactionService.getAllTransactionsByPartyIds(business.getParties()), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Party> createParty(@RequestBody PartyForm partyRequest, Authentication authentication) {

        String email = getEmailOfLoggedInUser(authentication);
        User saveUser = userService.getUserByEmail(email);
        Business business = saveUser.getSelectedBusiness();
        // Simulating saving and returning a Party object
        System.out.println("number" + partyRequest.getNumber());
        System.out.println("number" + partyRequest.getName());
        System.out.println("number" + partyRequest.getPartyType());

        Party party = partyService.getPartyByMobileNumber(partyRequest.getNumber());
        if (party != null) {
            return ResponseEntity.ok(party);
        }
        party = new Party();
        party.setName(partyRequest.getName());
        party.setBusiness(business);
        party.setMobile(partyRequest.getNumber());
        party.setPartyType(PartyType.valueOf(partyRequest.getPartyType()));
        partyService.createParty(party);
        // Return the created Party object
        return ResponseEntity.ok(party);
    }

}
