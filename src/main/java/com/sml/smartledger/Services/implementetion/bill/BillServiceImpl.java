package com.sml.smartledger.Services.implementetion.bill;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.BillProduct;
import com.sml.smartledger.Model.bill.BillService;
import com.sml.smartledger.Model.bill.BillType;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Repository.bill.BillRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.bill.MyBillService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements MyBillService {


    BillRepository billRepository;
    BusinessRepository businessRepository;
    ProductTransactionService productTransactionService;
    ServiceTransactionService serviceTransactionService;
    @Autowired
    public BillServiceImpl(BillRepository billRepository, BusinessRepository businessRepository, ProductTransactionService productTransactionService, ServiceTransactionService serviceTransactionService) {
        this.billRepository = billRepository;
        this.businessRepository = businessRepository;
        this.productTransactionService = productTransactionService;
        this.serviceTransactionService = serviceTransactionService;
    }

    @Override
    public List<Bill> getAllBills(Long businessId) {
        return billRepository.findAllBillByBusinessId(businessId);
    }

    @Override
    public Bill createBill(Bill bill) {
        Optional<Business> businessOptional = businessRepository.findById(bill.getBusiness().getId());
        if (businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        if (bill.getBillType() == BillType.SALE) {
            processBillProducts(bill, StockTransactionType.OUT);
            processBillServices(bill, StockTransactionType.OUT);
        } else if (bill.getBillType() == BillType.SALE_RETURN) {
            processBillProducts(bill, StockTransactionType.IN);
            processBillServices(bill, StockTransactionType.IN);
        } else if (bill.getBillType() == BillType.PURCHASE_RETURN) {
            processBillProducts(bill, StockTransactionType.IN);
        } else {
            processBillProducts(bill, StockTransactionType.OUT);
        }

        Bill savedBill = billRepository.save(bill);
        business.getBills().add(savedBill);
        businessRepository.save(business);
        return savedBill;
    }

    @Override
    public Bill updateBill(Bill bill) {
        return null;
    }

    private void processBillProducts(Bill bill, StockTransactionType transactionType) {
        for (BillProduct billProduct : bill.getProducts().keySet()) {
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setBillProduct(billProduct);
            productTransaction.setDate(new Date());
            productTransaction.setUnit(bill.getProducts().get(billProduct));
            productTransaction.setAmount(billProduct.getSalePrice() * bill.getProducts().get(billProduct));
            productTransaction.setDescription("Stock Balance" + billProduct.getStockQuantity());
            productTransaction.setStockTransactionType(transactionType);
            productTransactionService.addTransaction(productTransaction);
        }
    }

    private void processBillServices(Bill bill, StockTransactionType transactionType) {
        for (BillService billService : bill.getServices()) {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.setBillService(billService);
            serviceTransaction.setDate(new Date());
            serviceTransaction.setUnit(bill.getProducts().get(billService));
            serviceTransaction.setAmount(billService.getSalePrice() * bill.getProducts().get(billService));
            serviceTransactionService.addServiceTransaction(serviceTransaction);
        }
    }
}