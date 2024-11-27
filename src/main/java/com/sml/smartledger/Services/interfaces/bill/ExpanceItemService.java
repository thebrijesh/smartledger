package com.sml.smartledger.Services.interfaces.bill;


import com.sml.smartledger.Model.bill.ExpansesItem;

import java.util.List;

public interface ExpanceItemService {

    public List<ExpansesItem> getAllExpansesItem(Long businessId);
    public ExpansesItem createExpansesItem(ExpansesItem expansesItem);
    public void deleteExpansesItem(Long expansesItemId);
}
