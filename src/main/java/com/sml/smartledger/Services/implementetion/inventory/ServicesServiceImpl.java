package com.sml.smartledger.Services.implementetion.inventory;


import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.inventory.ServicesRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicesServiceImpl implements ServicesService {
    ServicesRepository serviceRepository;
    BusinessRepository businessRepository;

    @Autowired
    public ServicesServiceImpl(ServicesRepository serviceRepository, BusinessRepository businessRepository) {
        this.serviceRepository = serviceRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public Service addService(Service service) {

        return serviceRepository.save(service);
    }

    @Override
    public List<Service> getAllServiceByBusinessId(Long businessId) {
        return serviceRepository.findAllServiceByBusinessId(businessId);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        Business business = service.getBusiness();
        business.setTotalServicesSold(business.getTotalServicesSold() - service.getTotalSoldUnits());
        business.setTotalServiceAmount((int) (business.getTotalServiceAmount() - (service.getServicePrice() * service.getTotalSoldUnits())));
        businessRepository.save(business);
        serviceRepository.deleteById(id);
    }

    @Override
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
    }

    @Override
    public void updateService(Service service) {
        Service serviceDB = serviceRepository.findById(service.getId()).orElseThrow(() -> new RuntimeException("Service not found"));
        serviceDB.setDate(service.getDate());
        serviceDB.setUnitType(service.getUnitType());
        serviceDB.setName(service.getName());

        if(service.getImage() != null) {
            serviceDB.setCloudinaryImagePublicId(service.getCloudinaryImagePublicId());
            serviceDB.setImage(service.getImage());
        }
        Business business = serviceDB.getBusiness();
        business.setTotalServicesSold(business.getTotalServicesSold() - serviceDB.getTotalSoldUnits());
        business.setTotalServiceAmount((int) (business.getTotalServiceAmount() - (serviceDB.getServicePrice() * serviceDB.getTotalSoldUnits())));

        serviceDB.setTotalSoldUnits(service.getTotalSoldUnits());
        serviceDB.setServicePrice(service.getServicePrice());

        business.setTotalServiceAmount((int) (business.getTotalServiceAmount() + (service.getServicePrice() * service.getTotalSoldUnits())));
        business.setTotalServicesSold(business.getTotalServicesSold() + service.getTotalSoldUnits());

        businessRepository.save(business);
        serviceRepository.save(serviceDB);
    }
}
