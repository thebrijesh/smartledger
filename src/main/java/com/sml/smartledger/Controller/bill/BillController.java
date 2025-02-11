package com.sml.smartledger.Controller.bill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sml.smartledger.Forms.*;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.*;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.*;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Model.party.TransactionType;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.AdditionalChargesService;
import com.sml.smartledger.Services.interfaces.bill.BillService;
import com.sml.smartledger.Services.interfaces.bill.CustomFieldsService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.slf4j.Logger;
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
import static com.sml.smartledger.Helper.Helper.combineDate;
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
    PartyTransactionService partyTransactionService;

    @Autowired
    public BillController(PartyTransactionService partyTransactionService,CustomFieldsService customFieldsService, AdditionalChargesService additionalChargesService, PartyService partyService, ServiceTransactionService serviceTransactionService, ServicesService serviceService, ProductTransactionService productTransactionService, ProductService productService, BillService billService, UserService userService) {
        this.billService = billService;
        this.userService = userService;
        this.productService = productService;
        this.productTransactionService = productTransactionService;
        this.servicesService = serviceService;
        this.serviceTransactionService = serviceTransactionService;
        this.partyService = partyService;
        this.additionalChargesService = additionalChargesService;
        this.customFieldsService = customFieldsService;
        this.partyTransactionService = partyTransactionService;
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<Bill>> getAllBills(@PathVariable("businessId") Long businessId) {
        List<Bill> billList = billService.getAllBills(businessId);
        return new ResponseEntity<>(billList, HttpStatus.OK);
    }


    @PostMapping("/create-bill")
    public ResponseEntity<Void> createBill(@RequestBody BillForm bill) throws ParseException {
        System.out.println("billll" + bill);

        List<String> terms = bill.getTerms();
        Business business = partyService.getPartyById(bill.getPartyId()).getBusiness();

        Bill savedBill;
         if(bill.getId() != null){
                savedBill = billService.getBillById(bill.getId());
         }else{
             savedBill = new Bill();
         }
        savedBill.setParty(partyService.getPartyById(bill.getPartyId()));
        savedBill.setAmount(bill.getAmount());
        savedBill.setDate(combineDate(bill.getDate(), new Date()));
        savedBill.setPaymentType(PaymentType.valueOf(bill.getPaymentType()));
        savedBill.setAmountDue(bill.getDueAmount());
        savedBill.setBusiness(business);
        savedBill.setBillType(BillType.valueOf(bill.getBillType()));
        savedBill.setTermsAndConditions(terms);
        savedBill.setDiscount(bill.getTotalDiscount());
        if(bill.getDiscountType() == null) {
            savedBill.setDiscountType(DiscountType.FLAT);
        } else {
            savedBill.setDiscountType(DiscountType.valueOf(bill.getDiscountType()));
        }
        billService.createBill(savedBill);



        List<ProductTransactionForm> productTransactionFormList = bill.getProducts();
        List<ServiceTransactionForm> serviceTransactionFormList = bill.getServices();

        List<ProductTransaction> productTransactionList = new ArrayList<>();
        List<ServiceTransaction> serviceTransactionList = new ArrayList<>();
        for (ProductTransactionForm productTransactionForm : productTransactionFormList) {
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setProduct(productService.getProductById(productTransactionForm.getProductId()));
            productTransaction.setAmount(productTransactionForm.getAmount());
            productTransaction.setUnit(productTransactionForm.getStockQuantity());
            productTransaction.setStockTransactionType((bill.getBillType().equalsIgnoreCase("purchase")) ? StockTransactionType.IN : StockTransactionType.OUT);
            productTransaction.setDescription(productTransactionForm.getDescription());
            productTransaction.setDate(savedBill.getDate());
            productTransaction.setBill(savedBill);
            productTransactionService.addProductTransaction(productTransaction);
            productTransactionList.add(productTransaction);
        }

        for (ServiceTransactionForm serviceTransactionForm : serviceTransactionFormList) {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.setService(servicesService.getServiceById(serviceTransactionForm.getServiceId()));
            serviceTransaction.setAmount(serviceTransactionForm.getAmount());
            serviceTransaction.setUnit(serviceTransactionForm.getStockQuantity());
            serviceTransaction.setServiceTransactionType(ServiceTransactionType.SALE);
            serviceTransaction.setDescription(serviceTransactionForm.getDescription());
            serviceTransaction.setDate(savedBill.getDate());
            serviceTransaction.setBill(savedBill);
            serviceTransactionService.addServiceTransaction(serviceTransaction);
            serviceTransactionList.add(serviceTransaction);
        }

        List<AdditionalCharges> additionalChargesList = new ArrayList<>();
        for (chargesForm additionalChargesForm : bill.getAdditionalCharges()) {
            AdditionalCharges additionalCharges = new AdditionalCharges();
            additionalCharges.setAmount(additionalChargesForm.getAmount());
            additionalCharges.setName(additionalChargesForm.getName());
            additionalCharges.setBill(savedBill);
            additionalCharges.setBusiness(business);
            additionalChargesService.createAdditionalCharges(additionalCharges);
            additionalChargesList.add(additionalCharges);
        }

        List<CustomFields> customFieldsList = new ArrayList<>();
        for (customFieldsForm customFieldsForm : bill.getCustomFields()) {
            CustomFields customFields = new CustomFields();
            customFields.setName(customFieldsForm.getName());
            customFields.setValue(customFieldsForm.getValue());
            customFields.setBill(savedBill);
            customFields.setBusiness(business);
            customFieldsService.createCustomField(customFields);
            customFieldsList.add(customFields);
        }

        PartyTransaction partyTransaction = new PartyTransaction();
        if(bill.getDueAmount() > 0){
            partyTransaction.setAmount(bill.getDueAmount());
            partyTransaction.setParty(savedBill.getParty());
            partyTransaction.setTransactionType((savedBill.getBillType().toString().equalsIgnoreCase("purchase")) ? TransactionType.CREDIT : TransactionType.DEBIT);
            partyTransaction.setDate(savedBill.getDate());
            partyTransaction.setDescription("Bill#" + savedBill.getId());
            partyTransaction.setBill(savedBill);
            partyTransactionService.createTransaction(partyTransaction);
            savedBill.setTransaction(partyTransaction);
            billService.updateBill(savedBill);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/sales")
    public String getAllSellBill(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        long totalSales = 0;
        long totalPurchase = 0;
        long totalExpanse = 0;
        List<Bill> billList = billService.getAllBills(business.getId());
        for (Bill bill : billList) {
            if (bill.getBillType().toString().equalsIgnoreCase("sale")) {
                totalSales += bill.getAmount();
            } else if (bill.getBillType().toString().equalsIgnoreCase("purchase")) {
                totalPurchase += bill.getAmount();
            } else if(bill.getBillType().toString().equalsIgnoreCase("sale_return")){
                totalSales -= bill.getAmount();
            } else if(bill.getBillType().toString().equalsIgnoreCase("purchase_return")) {
                totalPurchase -= bill.getAmount();
            }
        }

        List<Expenses> expensesList = business.getExpensesList();
        for (Expenses expenses : expensesList) {
            totalExpanse += expenses.getAmount();
        }

        List<Bill> billList1 = billService.getAllSaleBills(business.getId());
        model.addAttribute("bills", billList1);
        model.addAttribute("billType", "Sales");
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalPurchase", totalPurchase);
        model.addAttribute("totalExpanse", totalExpanse);
        return "user/bill/sales";
    }

    @GetMapping("/purchase")
    public String getAllPurchaseBill(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        long totalSales = 0;
        long totalPurchase = 0;
        long totalExpanse = 0;
        List<Bill> billList = billService.getAllBills(business.getId());
        for (Bill bill : billList) {
            if (bill.getBillType().toString().equalsIgnoreCase("sale")) {
                totalSales += bill.getAmount();
            } else if (bill.getBillType().toString().equalsIgnoreCase("purchase")) {
                totalPurchase += bill.getAmount();
            } else if(bill.getBillType().toString().equalsIgnoreCase("sale_return")){
                totalSales -= bill.getAmount();
            } else if(bill.getBillType().toString().equalsIgnoreCase("purchase_return")) {
                totalPurchase -= bill.getAmount();
            }
        }

        List<Expenses> expensesList = business.getExpensesList();
        for (Expenses expenses : expensesList) {
            totalExpanse += expenses.getAmount();
        }

        List<Bill> billList1 = billService.getPurchaseBills(business.getId());
        model.addAttribute("bills", billList1);
        model.addAttribute("billType", "Purchase");
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalPurchase", totalPurchase);
        model.addAttribute("totalExpanse", totalExpanse);
        return "user/bill/purchase";
    }

    @GetMapping("/view-bill/{billId}")
    public String viewBill(@PathVariable("billId") Long billId, Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        Bill bill = billService.getBillById(billId);

        model.addAttribute("bill", bill);
        return "user/bill/view_bill";
    }

    @GetMapping("/get-bill/{billId}")
    public ResponseEntity<?> getBill(@PathVariable("billId") Long id) {
        try {
            Bill bill = billService.getBillById(id);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/return-bill/{billType}")
    public String returnBillView(@PathVariable("billType") String billType , Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();

        List<Bill> billList = billService.getAllBills(business.getId());
        System.out.println("billList: " + billList);
        Iterator<Bill> iterator = billList.iterator();

        while (iterator.hasNext()) {
            Bill bill = iterator.next();
            if (!bill.getBillType().toString().equalsIgnoreCase(billType)) {
                System.out.println(bill.getBillType().toString());
                System.out.println(billType);
                iterator.remove(); // Safe removal using iterator
            }
        }
        System.out.println("billList: " + billList);

        List<Product> productList = business.getProducts();
        List<Service> serviceList = business.getServices();

        List<ProductTransaction> productTransactionList = new ArrayList<>();
        List<ServiceTransaction> serviceTransactionList = new ArrayList<>();

        for (Product product : productList) {
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
         model.addAttribute("bills", billList);
        model.addAttribute("billType", billType);
        model.addAttribute("productTransactions", productTransactionList);
        model.addAttribute("serviceTransactions", serviceTransactionList);
        return "user/bill/bill_return";
    }



    @GetMapping("/update-bill/{billId}")
    public String updateBill(@PathVariable("billId") Long billId, Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        Bill bill = billService.getBillById(billId);

        List<ProductTransaction> productTransactionList = bill.getProductTransactions();
        List<ServiceTransaction> serviceTransactionList = bill.getServiceTransactions();

        List<CustomFields> customFieldsList = bill.getCustomFields();
        System.out.println("customFieldList" + customFieldsList);
        List<AdditionalCharges> additionalChargesList = bill.getAdditionalCharges();
        System.out.println("additionalChargesList" + additionalChargesList);

        List<Product> productList = business.getProducts();
        List<Service> serviceList = business.getServices();
        model.addAttribute("bill", bill);

        List<ProductTransaction> finalProductTransactionList = bill.getProductTransactions();
        List<ServiceTransaction> finalServiceTransactionList = bill.getServiceTransactions();
        for (Product product : productList) {
            boolean found = false;
            for (ProductTransaction productTransaction : productTransactionList) {
                if (productTransaction.getProduct().getId() == (product.getId())) {
                    found = true;
                    System.out.println("tttttttttttttt");
                    break;
                }
            }
            if (!found) {
                ProductTransaction productTransaction = new ProductTransaction();
                productTransaction.setProduct(product);
                productTransaction.setUnit(0);
                finalProductTransactionList.add(productTransaction);
            }
        }

        for (Service service : serviceList) {
            boolean found = false;
            for (ServiceTransaction serviceTransaction : serviceTransactionList) {
                if (serviceTransaction.getService().getId().equals(service.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                ServiceTransaction serviceTransaction = new ServiceTransaction();
                serviceTransaction.setService(service);
                serviceTransaction.setUnit(0);
                finalServiceTransactionList.add(serviceTransaction);
            }
        }
        if (bill.getBillType().toString().equalsIgnoreCase("purchase")) {
            model.addAttribute("billType", "Purchase");
        } else {
            model.addAttribute("billType", "Sales");
        }
        System.out.println("productTransactions: " + finalProductTransactionList);
        model.addAttribute("productTransactions", productTransactionList);
        model.addAttribute("serviceTransactions", serviceTransactionList);
        model.addAttribute("CustomFields", customFieldsList);
        model.addAttribute("AdditionalCharges", additionalChargesList);
        model.addAttribute("Terms", bill.getTermsAndConditions());
        model.addAttribute("date", new SimpleDateFormat("dd-MM-yyyy").format(bill.getDate()));

        return "user/bill/add_bill";
    }

    @PostMapping("/update-bill/{billId}")
    public void updateBill(@PathVariable("billId") Long billId, @RequestBody BillForm billForm) {
        System.out.println("billForm: " + billForm);
        Bill bill = billService.getBillById(billId);
        bill.setAmount(billForm.getAmount());
        bill.setAmountDue(billForm.getDueAmount());
        bill.setDate(Helper.combineDate(billForm.getDate(), new Date()));
        bill.setPaymentType(PaymentType.valueOf(billForm.getPaymentType()));
        bill.setDiscount(billForm.getTotalDiscount());
        if(billForm.getDiscountType() == null) {
            bill.setDiscountType(DiscountType.FLAT);
        } else {
            bill.setDiscountType(DiscountType.valueOf(billForm.getDiscountType()));
        }
        bill.setBillType(BillType.valueOf(billForm.getBillType()));
        bill.setTermsAndConditions(billForm.getTerms());

        billService.updateBill(bill);
        List<ProductTransaction> oldProductTransactionList = bill.getProductTransactions();
        List<ServiceTransaction> oldServiceTransactionList = bill.getServiceTransactions();

        for(ProductTransaction productTransaction : oldProductTransactionList){
            productTransactionService.deleteProductTransaction(productTransaction.getId());
        }
        for(ServiceTransaction serviceTransaction : oldServiceTransactionList){
            serviceTransactionService.deleteServiceTransaction(serviceTransaction.getId());
        }

        List<ProductTransactionForm> productTransactionFormList = billForm.getProducts();
        List<ServiceTransactionForm> serviceTransactionFormList = billForm.getServices();

        List<AdditionalCharges> oldAdditionalChargesList = bill.getAdditionalCharges();

        for(AdditionalCharges additionalCharges : oldAdditionalChargesList){
            additionalChargesService.deleteAdditionalCharges(additionalCharges);
        }

         if(bill.getTransaction() != null) {
             partyTransactionService.deleteTransaction(bill.getTransaction().getId());
         }
        List<CustomFields> oldCustomFieldsList = bill.getCustomFields();

        for(CustomFields customFields : oldCustomFieldsList){
            customFieldsService.deleteCustomField(customFields);
        }

        List<ProductTransaction> productTransactionList = new ArrayList<>();
        List<ServiceTransaction> serviceTransactionList = new ArrayList<>();

        for (ProductTransactionForm productTransactionForm : productTransactionFormList) {
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setProduct(productService.getProductById(productTransactionForm.getProductId()));
            productTransaction.setAmount(productTransactionForm.getAmount());
            productTransaction.setUnit(productTransactionForm.getStockQuantity());
            productTransaction.setStockTransactionType((bill.getBillType().toString().equalsIgnoreCase("purchase")) ? StockTransactionType.IN : StockTransactionType.OUT);
            productTransaction.setDescription(productTransactionForm.getDescription());
            productTransaction.setDate(bill.getDate());
            productTransaction.setBill(bill);
            productTransactionService.addProductTransaction(productTransaction);
            productTransactionList.add(productTransaction);
        }

        for (ServiceTransactionForm serviceTransactionForm : serviceTransactionFormList) {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.setService(servicesService.getServiceById(serviceTransactionForm.getServiceId()));
            serviceTransaction.setAmount(serviceTransactionForm.getAmount());
            serviceTransaction.setUnit(serviceTransactionForm.getStockQuantity());
            serviceTransaction.setServiceTransactionType(ServiceTransactionType.SALE);
            serviceTransaction.setDescription(serviceTransactionForm.getDescription());
            serviceTransaction.setDate(bill.getDate());
            serviceTransaction.setBill(bill);
            serviceTransactionService.addServiceTransaction(serviceTransaction);
            serviceTransactionList.add(serviceTransaction);
        }

        List<AdditionalCharges> additionalChargesList = new ArrayList<>();
        for (chargesForm additionalChargesForm : billForm.getAdditionalCharges()) {
            AdditionalCharges additionalCharges = new AdditionalCharges();
            additionalCharges.setAmount(additionalChargesForm.getAmount());
            additionalCharges.setName(additionalChargesForm.getName());
            additionalCharges.setBill(bill);
            additionalCharges.setBusiness(bill.getBusiness());
            additionalChargesService.createAdditionalCharges(additionalCharges);
            additionalChargesList.add(additionalCharges);
        }

        List<CustomFields> customFieldsList = new ArrayList<>();
        for (customFieldsForm customFieldsForm : billForm.getCustomFields()) {
            CustomFields customFields = new CustomFields();
            customFields.setName(customFieldsForm.getName());
            customFields.setValue(customFieldsForm.getValue());
            customFields.setBill(bill);
            customFields.setBusiness(bill.getBusiness());
            customFieldsService.createCustomField(customFields);
            customFieldsList.add(customFields);
        }

        PartyTransaction partyTransaction = new PartyTransaction();
        if(billForm.getDueAmount() > 0){
            partyTransaction.setAmount(billForm.getDueAmount());
            partyTransaction.setParty(partyService.getPartyById(billForm.getPartyId()));
            partyTransaction.setTransactionType((billForm.getBillType().equalsIgnoreCase("purchase")) ? TransactionType.CREDIT : TransactionType.DEBIT);
            partyTransaction.setDate(Helper.convertStringToDate(billForm.getDate()));
            partyTransaction.setDescription("Bill#" + billForm.getId());
            partyTransaction.setBill(bill);
            partyTransactionService.createTransaction(partyTransaction);
        }

    }

    @GetMapping("/add-bill/{billType}")
    public String addBillView(@PathVariable("billType") String billType, Model model, Authentication authentication) throws JsonProcessingException {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        BillForm billForm = new BillForm();


        List<Product> productList = business.getProducts();
        List<Service> serviceList = business.getServices();

        List<AdditionalCharges> additionalChargesList = new ArrayList<>();

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
        if (billType.equalsIgnoreCase("purchase")) {
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
        model.addAttribute("bill", null);
        model.addAttribute("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        model.addAttribute("AdditionalCharges", additionalChargesList);
        model.addAttribute("CustomFields", new ArrayList<>());
        model.addAttribute("partyList", parties);
        model.addAttribute("productTransactions", productTransactionList);
        model.addAttribute("serviceTransactions", serviceTransactionList);
        model.addAttribute("selectedBusiness", business);

        return "user/bill/add_bill";
    }

    @DeleteMapping("/delete-bill/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable("billId") Long id) {
        System.out.println("delete bill" + id);
        try {
            billService.deleteBill(id);
            return ResponseEntity.ok("Bill deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

}
