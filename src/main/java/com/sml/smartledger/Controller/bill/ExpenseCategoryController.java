package com.sml.smartledger.Controller.bill;


import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.ExpensesCategory;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.ExpenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/expense-category")
public class ExpenseCategoryController {

    ExpenseCategoryService expenseCategoryService;
    UserService userService;


    @Autowired
    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService, UserService userService) {
        this.expenseCategoryService = expenseCategoryService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<ExpensesCategory>> getAllExpensesCategory(Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<ExpensesCategory> expensesCategoryList = expenseCategoryService.getAllExpensesCategory(business.getId());
        return new ResponseEntity<>(expensesCategoryList, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<ExpensesCategory> createExpensesCategory(@RequestBody ExpensesCategory expensesCategory, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        expensesCategory.setBusiness(business);

        ExpensesCategory saveExpensesCategory = expenseCategoryService.createExpensesCategory(expensesCategory);
        return new ResponseEntity<>(saveExpensesCategory, HttpStatus.CREATED);
    }
    @PutMapping("update/{id}")
    public ResponseEntity<ExpensesCategory> updateExpensesCategory(@PathVariable("id") Long id, @RequestBody ExpensesCategory expensesCategory){
        ExpensesCategory saveExpensesCategory = expenseCategoryService.updateExpensesCategory(id, expensesCategory);
        return new ResponseEntity<>(saveExpensesCategory, HttpStatus.OK);
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteExpensesCategory(@PathVariable("id") Long expensesCategoryId){
        expenseCategoryService.deleteExpensesCategory(expensesCategoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
