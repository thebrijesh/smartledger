package com.sml.smartledger.Services.interfaces.bill;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.CustomFields;

import java.util.List;

public interface CustomFieldsService {

    List<CustomFields> getAllCustomFields(Long businessId);
   CustomFields getCustomFieldsById(Long customFieldsId);
    CustomFields createCustomField(CustomFields customFields);
    CustomFields updateCustomField(CustomFields customFields);
}
