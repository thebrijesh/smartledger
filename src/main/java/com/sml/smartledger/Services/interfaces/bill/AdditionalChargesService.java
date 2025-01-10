package com.sml.smartledger.Services.interfaces.bill;

import com.sml.smartledger.Model.bill.AdditionalCharges;
import com.sml.smartledger.Model.bill.CustomFields;

import java.util.List;

public interface AdditionalChargesService {

    List<AdditionalCharges> getAllAdditionalCharges(Long businessId);
    AdditionalCharges getAdditionalChargesById(Long additionalChargesId);
    AdditionalCharges createAdditionalCharges(AdditionalCharges additionalCharges);
    AdditionalCharges updateAdditionalCharges(AdditionalCharges additionalCharges);
}
