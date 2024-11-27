package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.BillService;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.bill.BillServiceRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.bill.BillServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceServiceImpl implements BillServiceService {
@Autowired
BillServiceRepository serviceRepository;
    @Autowired
    BusinessRepository businessRepository;

    @Override
    public BillService addService(BillService service) {
        Optional<Business> businessOptional = businessRepository.findById(service.getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        BillService savedBillService = serviceRepository.save(service);
        business.getServices().add(savedBillService);
        businessRepository.save(business);
        return  savedBillService;
    }

    @Override
    public List<BillService> getAllServiceByBusinessId(Long businessId) {
        return serviceRepository.findAllServiceByBusinessId(businessId);
    }

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
