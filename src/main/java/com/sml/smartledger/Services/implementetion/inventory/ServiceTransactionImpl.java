package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.bill.BillService;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import com.sml.smartledger.Model.inventory.ServiceTransactionType;
import com.sml.smartledger.Repository.bill.BillServiceRepository;
import com.sml.smartledger.Repository.inventory.ServiceTransactionRepository;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTransactionImpl implements ServiceTransactionService {
    @Autowired
    ServiceTransactionRepository serviceTransactionRepository;
    @Autowired
    BillServiceRepository billServiceRepository;

    @Override
    public List<ServiceTransaction> getAllServiceTransaction(Long billServiceId) {

        return serviceTransactionRepository.findAllServiceTransactionByBillServiceId(billServiceId);
    }

    @Override
    public ServiceTransaction addServiceTransaction(ServiceTransaction serviceTransaction) {
        Optional<BillService> billServiceOptional = billServiceRepository.findById(serviceTransaction.getBillService().getId());
        if(billServiceOptional.isEmpty()) throw new RuntimeException("Service not found");
        BillService billService= billServiceOptional.get();

        ServiceTransaction savedServiceTransaction = serviceTransactionRepository.save(serviceTransaction);
        billService.getServiceTransactions().add(savedServiceTransaction);
        if(serviceTransaction.getType() == ServiceTransactionType.SALE){
            billService.setTotalSales(billService.getTotalSales()+serviceTransaction.getUnit());
            billService.setSalePrice((billService.getSalePrice()+serviceTransaction.getAmount())/billService.getTotalSales());
        }else{
            billService.setTotalSales(billService.getTotalSales()-serviceTransaction.getUnit());
            billService.setSalePrice((billService.getSalePrice()-serviceTransaction.getAmount())/billService.getTotalSales());
        }
        billServiceRepository.save(billService);
        return savedServiceTransaction;
    }
}
