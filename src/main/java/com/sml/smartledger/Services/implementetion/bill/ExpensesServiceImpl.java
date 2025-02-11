package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.Expenses;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.bill.ExpensesRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.bill.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExpensesServiceImpl implements ExpensesService {
    ExpensesRepository expensesRepository;
    BusinessRepository businessRepository;

    @Autowired
    public ExpensesServiceImpl(ExpensesRepository expensesRepository, BusinessRepository businessRepository) {
        this.expensesRepository = expensesRepository;
        this.businessRepository = businessRepository;
    }
    @Override
    public List<Expenses> getAllExpenses(Long businessId) {
        return expensesRepository.findByBusinessId(businessId);
    }

    @Override
    public Expenses createExpenses(Expenses expenses) {
        Optional<Business> businessOptional = businessRepository.findById(expenses.getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        Expenses savedExpense = expensesRepository.save(expenses);
        business.getExpensesList().add(savedExpense);
        businessRepository.save(business);
        return savedExpense;
    }

    @Override
    @Transactional
    public void deleteExpenses(Long expensesId) {
         expensesRepository.deleteByExpensesId(expensesId);
    }

    @Override
    public Expenses updateExpense(Long id, Expenses updatedExpense) {
        Expenses expense = expensesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        expense.setName(updatedExpense.getName());
        expense.setDescription(updatedExpense.getDescription());
        expense.setAmount(updatedExpense.getAmount());
        expense.setExpensesCategory(updatedExpense.getExpensesCategory());
        expense.setDate(updatedExpense.getDate());
        return expensesRepository.save(expense);
    }
}
