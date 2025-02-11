package com.sml.smartledger.Services.implementetion.bill;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Repository.bill.BillRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.inventory.ProductTransactionRepository;
import com.sml.smartledger.Services.interfaces.bill.AdditionalChargesService;
import com.sml.smartledger.Services.interfaces.bill.BillService;
import com.sml.smartledger.Services.interfaces.bill.CustomFieldsService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BillServiceImpl implements BillService {


    private final ProductTransactionRepository productTransactionRepository;
    BillRepository billRepository;
    BusinessRepository businessRepository;
    ProductTransactionService productTransactionService;
    ServiceTransactionService serviceTransactionService;
    AdditionalChargesService additionalChargesService;
    CustomFieldsService customFieldsService;
    PartyTransactionService partyTransactionService;
    @Autowired
    public BillServiceImpl(PartyTransactionService partyTransactionService ,AdditionalChargesService additionalChargesService , CustomFieldsService customFieldsService ,BillRepository billRepository, BusinessRepository businessRepository, ProductTransactionService productTransactionService, ServiceTransactionService serviceTransactionService, ProductTransactionRepository productTransactionRepository) {
        this.billRepository = billRepository;
        this.businessRepository = businessRepository;
        this.productTransactionService = productTransactionService;
        this.serviceTransactionService = serviceTransactionService;
        this.productTransactionRepository = productTransactionRepository;
        this.customFieldsService = customFieldsService;
        this.additionalChargesService = additionalChargesService;
        this.partyTransactionService = partyTransactionService;
    }

    @Override
    public List<Bill> getAllBills(Long businessId) {
        return billRepository.findAllBillByBusinessId(businessId);
    }

    @Override
    public List<Bill> getPurchaseBills(Long businessId) {
        return billRepository.findAllPurchaseBillByBusinessId(businessId);
    }

    @Override
    public List<Bill> getAllSaleBills(Long businessId) {
        return billRepository.findAllSaleBillByBusinessId(businessId);
    }

    @Override
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Bill bill) {
        System.out.println("billlll" + bill);
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    @Override
    @Transactional
    public void deleteBill(Long id) {
        Bill bill = getBillById(id);
        bill.getProductTransactions().forEach(productTransaction -> productTransactionService.deleteProductTransaction(productTransaction.getId()));
        bill.getServiceTransactions().forEach(serviceTransaction -> serviceTransactionService.deleteServiceTransaction(serviceTransaction.getId()));
        bill.getAdditionalCharges().forEach(additionalCharges -> additionalChargesService.deleteAdditionalCharges(additionalCharges));
        bill.getCustomFields().forEach(customFields -> customFieldsService.deleteCustomField(customFields));
        partyTransactionService.deleteTransaction(bill.getTransaction().getId());
        billRepository.deleteById(id);
    }

    private void processBillProducts(Bill bill, StockTransactionType transactionType) {
//        for (Product billProduct : bill.get().keySet()) {
//            ProductTransaction productTransaction = new ProductTransaction();
//            productTransaction.setProduct(billProduct);
//            productTransaction.setDate(new Date());
//            productTransaction.setUnit(bill.getProducts().get(billProduct));
//            productTransaction.setAmount(billProduct.getSalePrice() * bill.getProducts().get(billProduct));
//            productTransaction.setDescription("Stock Balance" + billProduct.getStockQuantity());
//            productTransaction.setStockTransactionType(transactionType);
//            productTransactionService.addProductTransaction(productTransaction);
//        }
    }

    private void processBillServices(Bill bill, StockTransactionType transactionType) {
//        for (Service billService : bill.getServices()) {
//            ServiceTransaction serviceTransaction = new ServiceTransaction();
//            serviceTransaction.setService(billService);
//            serviceTransaction.setDate(new Date());
//            serviceTransaction.setUnit(bill.getProducts().get(billService));
//            serviceTransaction.setAmount(billService.getServicePrice() * bill.getProducts().get(billService));
//            serviceTransactionService.addServiceTransaction(serviceTransaction);
//        }
    }
}