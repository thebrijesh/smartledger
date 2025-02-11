package com.sml.smartledger.Services.interfaces.bill;


import com.sml.smartledger.Model.bill.Expenses;

import java.util.List;

public interface ExpensesService {

     List<Expenses> getAllExpenses(Long businessId);
     Expenses createExpenses( Expenses expenses);
     void deleteExpenses(Long expensesId);
    Expenses updateExpense(Long id, Expenses updatedExpense);
}
