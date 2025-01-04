package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import com.sml.smartledger.Model.inventory.ServiceTransactionType;
import com.sml.smartledger.Repository.inventory.ServicesRepository;
import com.sml.smartledger.Repository.inventory.ServiceTransactionRepository;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceTransactionImpl implements ServiceTransactionService {
    ServiceTransactionRepository serviceTransactionRepository;
    ServicesRepository servicesRepository;
    @Autowired
    public ServiceTransactionImpl(ServiceTransactionRepository serviceTransactionRepository, ServicesRepository servicesRepository) {
        this.serviceTransactionRepository = serviceTransactionRepository;
        this.servicesRepository = servicesRepository;
    }

    @Override
    public List<ServiceTransaction> getAllServiceTransaction(Long billServiceId) {

        return serviceTransactionRepository.findAllServiceTransactionByServiceId(billServiceId);
    }

    @Override
    public ServiceTransaction addServiceTransaction(ServiceTransaction serviceTransaction) {
        Optional<Service> billServiceOptional = servicesRepository.findById(serviceTransaction.getService().getId());
        if(billServiceOptional.isEmpty()) throw new RuntimeException("Service not found");
        Service billService= billServiceOptional.get();

        ServiceTransaction savedServiceTransaction = serviceTransactionRepository.save(serviceTransaction);
        billService.getServiceTransactions().add(savedServiceTransaction);
        if(serviceTransaction.getServiceTransactionType() == ServiceTransactionType.SALE){
            billService.setTotalSoldUnits(billService.getTotalSoldUnits()+serviceTransaction.getUnit());
            billService.setServicePrice((billService.getServicePrice()+serviceTransaction.getAmount())/billService.getTotalSoldUnits());
        }else{
            billService.setTotalSoldUnits(billService.getTotalSoldUnits()-serviceTransaction.getUnit());
            billService.setServicePrice((billService.getServicePrice()-serviceTransaction.getAmount())/billService.getTotalSoldUnits());
        }
        servicesRepository.save(billService);
        return savedServiceTransaction;
    }
}
