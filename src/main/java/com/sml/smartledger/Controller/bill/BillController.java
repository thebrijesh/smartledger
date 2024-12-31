package com.sml.smartledger.Controller.bill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sml.smartledger.Forms.BillForm;
import com.sml.smartledger.Forms.ProductForm;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.BillType;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.*;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.sml.smartledger.Helper.AppConstants.unitList;
import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/transactions")
public class BillController {


    BillService billService;
    UserService userService;
    @Autowired
    public BillController(BillService billService, UserService userService) {
        this.billService = billService;
        this.userService = userService;
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<Bill>> getAllBills(@PathVariable("businessId") Long businessId) {
        List<Bill> billList = billService.getAllBills(businessId);
        return new ResponseEntity<>(billList, HttpStatus.OK);
    }

    @PostMapping("/create-bill")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill savedBill = billService.createBill(bill);
        return new ResponseEntity<>(savedBill, HttpStatus.CREATED);
    }

    @GetMapping("/sales")
    public String getAllSellBill(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();

        List<Bill> billList = billService.getAllSaleBills(business.getId());
        model.addAttribute("bills", billList);
        model.addAttribute("billType", "Sales");
        return "user/bill/sales";
    }
    @GetMapping("/purchase")
    public String getAllPurchaseBill(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();

        List<Bill> billList = billService.getAllSaleBills(business.getId());
        model.addAttribute("bills", billList);
        model.addAttribute("billType", "Purchase");
        return "user/bill/sales";
    }

    @GetMapping("/add-bill/{billType}")
    public String addBillView(@PathVariable("billType") String billType, Model model, Authentication authentication) throws JsonProcessingException {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        BillForm billForm = new BillForm();


        List<Product> productList = business.getProducts();
        List<Service> serviceList = business.getServices();

        Map<Long,ProductTransaction> productTransactionList = new HashMap<>();
        Map<Long,ServiceTransaction> serviceTransactionList = new HashMap<>();

        for (Product product : productList) {
            System.out.println("product: " + product);
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setProduct(product);
            productTransaction.setUnit(0);
            productTransactionList.put(product.getId(),productTransaction);
        }
        for (Service service : serviceList) {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.setService(service);
            serviceTransaction.setUnit(0);
            serviceTransactionList.put(service.getId(),serviceTransaction);
        }


        billForm.setProducts(productTransactionList);
        billForm.setServices(serviceTransactionList);
        billForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        List<Party> parties;
        if(billType.equalsIgnoreCase("purchase")) {
            billForm.setBillType(String.valueOf(BillType.PURCHASE));
            parties = business.getParties().stream().filter(party -> party.getPartyType().equals(PartyType.SUPPLIER)).toList();
        } else {
            billForm.setBillType(String.valueOf(BillType.SALE));
            parties = business.getParties().stream().filter(party -> party.getPartyType().equals(PartyType.CUSTOMER)).toList();
        }

        System.out.println("parties: " + parties);
        System.out.println("products: " + productTransactionList);
        System.out.println("services: " + serviceTransactionList);
        model.addAttribute("billForm", billForm);
        model.addAttribute("partyList", parties);
        model.addAttribute("products", productTransactionList);
        model.addAttribute("services", serviceTransactionList);
        model.addAttribute("selectedBusiness", business);

        return "/user/bill/add_bill";
    }



}
