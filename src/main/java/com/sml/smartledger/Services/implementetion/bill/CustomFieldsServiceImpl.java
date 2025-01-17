package com.sml.smartledger.Services.implementetion.bill;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.CustomFields;
import com.sml.smartledger.Repository.bill.CustomFieldsRepository;
import com.sml.smartledger.Services.interfaces.bill.CustomFieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CustomFieldsServiceImpl implements CustomFieldsService {

    CustomFieldsRepository customFieldsRepository;
    @Autowired
    public CustomFieldsServiceImpl(CustomFieldsRepository customFieldsRepository) {
        this.customFieldsRepository = customFieldsRepository;
    }
    @Override
    public List<CustomFields> getAllCustomFields(Long businessId) {
        return customFieldsRepository.findAllByBusinessId(businessId);
    }

    @Override
    public CustomFields getCustomFieldsById(Long customFieldsId) {
        return customFieldsRepository.findById(customFieldsId).orElse(null);
    }

    @Override
    public CustomFields createCustomField(CustomFields customFields) {
        return customFieldsRepository.save(customFields);
    }

    @Override
    public CustomFields updateCustomField(CustomFields customFields) {
        return customFieldsRepository.save(customFields);
    }

    @Override
    @Transactional
    public void deleteCustomField(CustomFields customFields) {
        customFieldsRepository.deleteById(customFields.getId());
    }
}
