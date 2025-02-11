package com.sml.smartledger.Controller.bill;


import com.sml.smartledger.Model.bill.ExpensesItem;
import com.sml.smartledger.Services.interfaces.bill.ExpenseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenseitem")
public class ExpenseItemController {

    @Autowired
    ExpenseItemService expenseItemService;
    @GetMapping("/{businessId}")
    public ResponseEntity<List<ExpensesItem>> getAllExpensesItem(@PathVariable("businessId") Long businessId){
        List<ExpensesItem> expensesItemsList = expenseItemService.getAllExpensesItem(businessId);
        return new ResponseEntity<>(expensesItemsList, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<ExpensesItem> createExpensesItem(@RequestBody ExpensesItem expensesItem){
        ExpensesItem saveExpensesItem = expenseItemService.createExpensesItem(expensesItem);
        return new ResponseEntity<>(saveExpensesItem, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpensesItem(@PathVariable("id") Long expensesItemId){
        expenseItemService.deleteExpensesItem(expensesItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
