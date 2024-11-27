package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.ExpansesCategory;
import com.sml.smartledger.Repository.bill.ExpanceCatagoryRepository;
import com.sml.smartledger.Services.interfaces.bill.ExpanceCatagoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpanceCatagoryServiceImpl implements ExpanceCatagoryService {

    @Autowired
    ExpanceCatagoryRepository expanceCatagoryRepository;
    @Override
    public List<ExpansesCategory> getAllExpansesCategory(Long businessId) {
        return expanceCatagoryRepository.findByBusinessId(businessId);
    }

    @Override
    public ExpansesCategory createExpansesCategory(ExpansesCategory expansesCategory) {
        return expanceCatagoryRepository.save(expansesCategory);
    }

    @Override
    public void deleteExpansesCategory(Long expansesCategoryId) {
         expanceCatagoryRepository.deleteById(expansesCategoryId);
    }
}
