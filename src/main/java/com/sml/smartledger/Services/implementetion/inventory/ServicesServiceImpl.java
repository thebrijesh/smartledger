package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.bill.BillServiceRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicesServiceImpl implements ServicesService {
    BillServiceRepository serviceRepository;
    BusinessRepository businessRepository;

    @Autowired
    public ServicesServiceImpl(BillServiceRepository serviceRepository, BusinessRepository businessRepository) {
        this.serviceRepository = serviceRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public Service addService(Service service) {
        Optional<Business> businessOptional = businessRepository.findById(service.getBusiness().getId());
        if (businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        Service savedBillService = serviceRepository.save(service);
        business.getServices().add(savedBillService);
        businessRepository.save(business);
        return savedBillService;
    }

    @Override
    public List<Service> getAllServiceByBusinessId(Long businessId) {
        return serviceRepository.findAllServiceByBusinessId(businessId);
    }

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
