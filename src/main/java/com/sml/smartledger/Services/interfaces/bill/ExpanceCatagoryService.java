package com.sml.smartledger.Services.interfaces.bill;


import com.sml.smartledger.Model.bill.ExpansesCategory;

import java.util.List;

public interface ExpanceCatagoryService {

    public List<ExpansesCategory> getAllExpansesCategory(Long businessId);
    public ExpansesCategory createExpansesCategory(ExpansesCategory expansesItem);
    public void deleteExpansesCategory(Long expansesItemId);
}
