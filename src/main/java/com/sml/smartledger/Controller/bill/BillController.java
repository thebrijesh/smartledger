package com.sml.smartledger.Controller.bill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sml.smartledger.Forms.*;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.*;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.*;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.AdditionalChargesService;
import com.sml.smartledger.Services.interfaces.bill.BillService;
import com.sml.smartledger.Services.interfaces.bill.CustomFieldsService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sml.smartledger.Helper.AppConstants.unitList;
import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/transactions")
public class BillController {


    BillService billService;
    UserService userService;
    ProductService productService;
    ProductTransactionService productTransactionService;
    ServiceTransactionService serviceTransactionService;
    ServicesService servicesService;
    PartyService partyService;
    AdditionalChargesService additionalChargesService;
    CustomFieldsService customFieldsService;
    @Autowired
    public BillController(CustomFieldsService customFieldsService , AdditionalChargesService additionalChargesService, PartyService partyService, ServiceTransactionService serviceTransactionService, ServicesService serviceService, ProductTransactionService productTransactionService , ProductService productService ,BillService billService, UserService userService) {
        this.billService = billService;
        this.userService = userService;
        this.productService = productService;
        this.productTransactionService = productTransactionService;
        this.servicesService = serviceService;
        this.serviceTransactionService = serviceTransactionService;
        this.partyService = partyService;
        this.additionalChargesService = additionalChargesService;
        this.customFieldsService = customFieldsService;
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<Bill>> getAllBills(@PathVariable("businessId") Long businessId) {
        List<Bill> billList = billService.getAllBills(businessId);
        return new ResponseEntity<>(billList, HttpStatus.OK);
    }

    @PostMapping("/create-bill")
    public ResponseEntity<Void> createBill(@RequestBody BillForm bill) throws ParseException {
        System.out.println("billll"  + bill);
        List<String> terms = bill.getTerms();
         Business business = partyService.getPartyById(bill.getPartyId()).getBusiness();
        Bill savedBill = new Bill();
        savedBill.setParty(partyService.getPartyById(bill.getPartyId()));
        savedBill.setAmount(bill.getAmount());
        savedBill.setDate(new SimpleDateFormat().parse(bill.getDate()));
        savedBill.setPaymentType(PaymentType.valueOf(bill.getPaymentType()));
        savedBill.setAmountDue(bill.getDueAmount());
        savedBill.setBusiness(business);
        savedBill.setBillType(BillType.valueOf(bill.getBillType()));
        savedBill.setTermsAndConditions(terms);

        List<ProductTransactionForm> productTransactionFormList = bill.getProducts();
        List<ServiceTransactionForm> serviceTransactionFormList = bill.getServices();

        List<ProductTransaction> productTransactionList = new ArrayList<>();
        List<ServiceTransaction> serviceTransactionList = new ArrayList<>();
        for(ProductTransactionForm productTransactionForm: productTransactionFormList) {
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setProduct(productService.getProductById(productTransactionForm.getProductId()));
            productTransaction.setAmount(productTransactionForm.getAmount());
            productTransaction.setUnit(productTransactionForm.getStockQuantity());
            productTransaction.setStockTransactionType((bill.getBillType().equalsIgnoreCase("purchase")) ? StockTransactionType.IN : StockTransactionType.OUT);
            productTransaction.setDescription(productTransactionForm.getDescription());
            productTransaction.setDate(new SimpleDateFormat().parse(productTransactionForm.getDate()));
            productTransaction.setBill(savedBill);
            productTransactionService.addProductTransaction(productTransaction);
            productTransactionList.add(productTransaction);
        }

        for(ServiceTransactionForm serviceTransactionForm: serviceTransactionFormList) {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.setService(servicesService.getServiceById(serviceTransactionForm.getServiceId()));
            serviceTransaction.setAmount(serviceTransactionForm.getAmount());
            serviceTransaction.setUnit(serviceTransactionForm.getStockQuantity());
            serviceTransaction.setServiceTransactionType(ServiceTransactionType.SALE);
            serviceTransaction.setDescription(serviceTransactionForm.getDescription());
            serviceTransaction.setDate(new SimpleDateFormat().parse(serviceTransactionForm.getDate()));
            serviceTransaction.setBill(savedBill);
            serviceTransactionService.addServiceTransaction(serviceTransaction);
            serviceTransactionList.add(serviceTransaction);
        }

        List<AdditionalCharges> additionalChargesList = new ArrayList<>();
        for (chargesForm additionalChargesForm: bill.getAdditionalCharges()) {
            AdditionalCharges additionalCharges = new AdditionalCharges();
            additionalCharges.setAmount(additionalChargesForm.getAmount());
            additionalCharges.setName(additionalChargesForm.getName());
            additionalCharges.setBill(savedBill);
            additionalCharges.setBusiness(business);
            additionalChargesService.createAdditionalCharges(additionalCharges);
            additionalChargesList.add(additionalCharges);
        }

        List<CustomFields> customFieldsList = new ArrayList<>();
        for (customFieldsForm customFieldsForm: bill.getCustomFields()) {
            CustomFields customFields = new CustomFields();
            customFields.setName(customFieldsForm.getName());
            customFields.setValue(customFieldsForm.getValue());
            customFields.setBill(savedBill);
            customFields.setBusiness(business);
            customFieldsService.createCustomField(customFields);
            customFieldsList.add(customFields);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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

        List<AdditionalCharges> additionalChargesList = business.getAdditionalCharges();
        List<ProductTransaction> productTransactionList = new ArrayList<>();
        List<ServiceTransaction> serviceTransactionList = new ArrayList<>();

        for (Product product : productList) {
            System.out.println("product: " + product);
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setProduct(product);
            productTransaction.setUnit(0);
            productTransactionList.add(productTransaction);
        }
        for (Service service : serviceList) {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.setService(service);
            serviceTransaction.setUnit(0);
            serviceTransactionList.add(serviceTransaction);

        }



        billForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        List<Party> parties;
        if(billType.equalsIgnoreCase("purchase")) {
            billForm.setBillType(String.valueOf(BillType.PURCHASE));
            parties = business.getParties().stream().filter(party -> party.getPartyType().equals(PartyType.SUPPLIER)).toList();
            model.addAttribute("billType", "Purchase");
        } else {
            billForm.setBillType(String.valueOf(BillType.SALE));
            parties = business.getParties().stream().filter(party -> party.getPartyType().equals(PartyType.CUSTOMER)).toList();
            model.addAttribute("billType", "Sales");
        }

        System.out.println("parties: " + parties);
        System.out.println("productTransactions: " + productTransactionList);
        System.out.println("serviceTransactions: " + serviceTransactionList);
        model.addAttribute("billForm", billForm);
        model.addAttribute("AdditionalCharges", additionalChargesList);
        model.addAttribute("CustomFields", business.getCustomFields());
        model.addAttribute("Terms", business.getTermsAndConditions());
        model.addAttribute("partyList", parties);
        model.addAttribute("productTransactions", productTransactionList);
        model.addAttribute("serviceTransactions", serviceTransactionList);
        model.addAttribute("selectedBusiness", business);

        return "/user/bill/add_bill";
    }



}
