package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.ExpensesCategory;
import com.sml.smartledger.Repository.bill.ExpenseCategoryRepository;
import com.sml.smartledger.Services.interfaces.bill.ExpenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    public ExpenseCategoryServiceImpl(ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    @Override
    public List<ExpensesCategory> getAllExpensesCategory(Long businessId) {
        return expenseCategoryRepository.findByBusinessId(businessId);
    }

    @Override
    public ExpensesCategory createExpensesCategory(ExpensesCategory expensesCategory) {
        if (expenseCategoryRepository.existsByName(expensesCategory.getName())) {
            throw new IllegalArgumentException("Category with name " + expensesCategory.getName() + " already exists");
        }
        return expenseCategoryRepository.save(expensesCategory);
    }

    @Override
    public ExpensesCategory updateExpensesCategory(Long id, ExpensesCategory expensesCategory) {
        ExpensesCategory category = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(expensesCategory.getName());
        category.setDescription(expensesCategory.getDescription());
        return expenseCategoryRepository.save(category);
    }


    @Override
    @Transactional
    public void deleteExpensesCategory(Long expensesCategoryId) {
        expenseCategoryRepository.deleteExpensesByExpensesCategoryId(expensesCategoryId);
        expenseCategoryRepository.deleteByExpensesCategoryId(expensesCategoryId);
    }

    @Override
    public ExpensesCategory getExpensesCategoryById(Long expensesCategoryId) {
        return expenseCategoryRepository.findById(expensesCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
}
