package com.sml.smartledger.Services.interfaces.bill;


import com.sml.smartledger.Model.bill.ExpensesCategory;

import java.util.List;

public interface ExpenseCategoryService {

    public List<ExpensesCategory> getAllExpensesCategory(Long businessId);
    public ExpensesCategory createExpensesCategory(ExpensesCategory expensesItem);
    ExpensesCategory updateExpensesCategory(Long id,ExpensesCategory expensesCategory);
     void deleteExpensesCategory(Long expensesItemId);

    ExpensesCategory getExpensesCategoryById(Long expensesCategoryId);
}
