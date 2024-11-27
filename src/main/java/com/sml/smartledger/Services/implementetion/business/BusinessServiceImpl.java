package com.sml.smartledger.Services.implementetion.business;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    BusinessRepository businessRepository;
    @Override
    public List<Business> getAllBusiness(String userId) {
        return businessRepository.findByUserId(userId);
    }

    @Override
    public Business createBusiness(Business business) {
//        Optional<Business> business1 = null;
//        if(business.getId() != 0) business1 =  businessRepository.findById(business.getId());
//        if(!business1.isEmpty()){
//            business.setId(business1.get().getId());
//        }
        if(business.getUser() == null) throw new RuntimeException("User Not Found");
        return businessRepository.save(business);
    }

    @Override
    public void deleteBusiness(Long Id) {
         businessRepository.deleteById(Id);
    }
}
