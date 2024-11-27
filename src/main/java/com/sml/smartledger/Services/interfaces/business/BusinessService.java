package com.sml.smartledger.Services.interfaces.business;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.business.Business;

import java.util.List;

public interface BusinessService {

    public List<Business> getAllBusiness(String userId);
    public Business createBusiness(Business business);
    public void deleteBusiness(Long Id);
}
