package com.sml.smartledger.Services.implementetion.bill;

import com.sml.smartledger.Model.bill.AdditionalCharges;
import com.sml.smartledger.Repository.bill.AdditionalChargesRepository;
import com.sml.smartledger.Services.interfaces.bill.AdditionalChargesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdditionalChargesImpl implements AdditionalChargesService {
    @Autowired
    AdditionalChargesRepository    additionalChargesRepository;

    @Override
    public List<AdditionalCharges> getAllAdditionalCharges(Long businessId) {
        return additionalChargesRepository.findAllByBusinessId(businessId);
    }

    @Override
    public AdditionalCharges getAdditionalChargesById(Long additionalChargesId) {
        return additionalChargesRepository.findById(additionalChargesId).orElse(null);
    }

    @Override
    public AdditionalCharges createAdditionalCharges(AdditionalCharges additionalCharges) {
        return additionalChargesRepository.save(additionalCharges);
    }

    @Override
    public AdditionalCharges updateAdditionalCharges(AdditionalCharges additionalCharges) {
            return additionalChargesRepository.save(additionalCharges);
    }
}
