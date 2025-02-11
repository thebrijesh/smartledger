package com.sml.smartledger.Services.interfaces.bill;


import com.sml.smartledger.Model.bill.ExpensesItem;

import java.util.List;

public interface ExpenseItemService {

    public List<ExpensesItem> getAllExpensesItem(Long businessId);
    public ExpensesItem createExpensesItem(ExpensesItem expensesItem);
    public void deleteExpensesItem(Long expensesItemId);
}
