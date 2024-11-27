package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.ExpansesItem;
import com.sml.smartledger.Repository.bill.ExpanceItemRepository;
import com.sml.smartledger.Services.interfaces.bill.ExpanceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpanceItemServiceImpl implements ExpanceItemService {
    @Autowired
    ExpanceItemRepository expanceItemRepository;
    @Override
    public List<ExpansesItem> getAllExpansesItem(Long businessId) {
        return expanceItemRepository.findByBusinessId(businessId);
    }

    @Override
    public ExpansesItem createExpansesItem(ExpansesItem expansesItem) {
        return expanceItemRepository.save(expansesItem);
    }

    @Override
    public void deleteExpansesItem(Long expansesItemId) {
        expanceItemRepository.deleteById(expansesItemId);
    }
}
