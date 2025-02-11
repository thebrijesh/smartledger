package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.ExpensesItem;
import com.sml.smartledger.Repository.bill.ExpenseItemRepository;
import com.sml.smartledger.Services.interfaces.bill.ExpenseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseItemServiceImpl implements ExpenseItemService {
    ExpenseItemRepository expenseItemRepository;

    @Autowired
    public ExpenseItemServiceImpl(ExpenseItemRepository expenseItemRepository) {
        this.expenseItemRepository = expenseItemRepository;
    }
    @Override
    public List<ExpensesItem> getAllExpensesItem(Long businessId) {
        return expenseItemRepository.findByBusinessId(businessId);
    }

    @Override
    public ExpensesItem createExpensesItem(ExpensesItem expensesItem) {
        return expenseItemRepository.save(expensesItem);
    }

    @Override
    public void deleteExpensesItem(Long expensesItemId) {
        expenseItemRepository.deleteById(expensesItemId);
    }
}
