package com.sml.smartledger.Controller.bill;


import com.sml.smartledger.Forms.ExpenseForm;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.Expenses;
import com.sml.smartledger.Model.bill.ExpensesCategory;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.implementetion.UserServiceImpl;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.BillService;
import com.sml.smartledger.Services.interfaces.bill.ExpenseCategoryService;
import com.sml.smartledger.Services.interfaces.bill.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/transaction/expense")
public class ExpensesController {

    private final ExpenseCategoryService expenseCategoryService;
    ExpensesService expensesService;
    UserService userService;
    BillService billService;

    @Autowired
    public ExpensesController(BillService billService,ExpensesService expensesService, UserService userService, ExpenseCategoryService expenseCategoryService) {
        this.expensesService = expensesService;
        this.userService = userService;
        this.expenseCategoryService = expenseCategoryService;
        this.billService = billService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Expenses>> getAllExpenses(Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Expenses> expensesList = expensesService.getAllExpenses(business.getId());
        return new ResponseEntity<>(expensesList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Expenses> createExpenses(@RequestBody ExpenseForm expenseForm, Authentication authentication) throws ParseException {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        System.out.println("expenses: " + expenseForm);
        Expenses expenses = createExpense(expenseForm, business);
        Expenses saveExpenses = expensesService.createExpenses(expenses);
        return new ResponseEntity<>(saveExpenses, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Expenses> updateExpenses(@PathVariable("id") Long id, @RequestBody ExpenseForm expenseForm) throws ParseException {

        Expenses expenses = createExpense(expenseForm, null);
        Expenses saveExpenses = expensesService.updateExpense(id, expenses);
        return new ResponseEntity<>(saveExpenses, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpenses(@PathVariable("id") Long expensesId) {
        expensesService.deleteExpenses(expensesId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/view/all")
    public String getAllExpenses(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        long totalSales = 0;
        long totalPurchase = 0;
        long totalExpanse = 0;
        List<Bill> billList = billService.getAllBills(business.getId());
        for (Bill bill : billList) {
            if (bill.getBillType().toString().equalsIgnoreCase("sale")) {
                totalSales += bill.getAmount();
            } else if (bill.getBillType().toString().equalsIgnoreCase("purchase")) {
                totalPurchase += bill.getAmount();
            } else if(bill.getBillType().toString().equalsIgnoreCase("sale_return")){
                totalSales -= bill.getAmount();
            } else if(bill.getBillType().toString().equalsIgnoreCase("purchase_return")) {
                totalPurchase -= bill.getAmount();
            }
        }

        List<Expenses> expensesList1 = business.getExpensesList();
        for (Expenses expenses : expensesList1) {
            totalExpanse += expenses.getAmount();
        }
        List<Expenses> expensesList = expensesService.getAllExpenses(business.getId());
        List<ExpensesCategory> expensesCategoryList = expenseCategoryService.getAllExpensesCategory(business.getId());
        model.addAttribute("expensesCategoryList", expensesCategoryList);
        model.addAttribute("billType", "Expense");
        model.addAttribute("expensesList", expensesList);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalPurchase", totalPurchase);
        model.addAttribute("totalExpanse", totalExpanse);
        return "user/bill/expense";
    }

    private Expenses createExpense(ExpenseForm expenseForm, Business business) throws ParseException {
        Expenses expenses = new Expenses();
        expenses.setBusiness(business);
        expenses.setAmount(expenseForm.getAmount());
        expenses.setDate(Helper.combineDate(expenseForm.getDate(), new Date()));
        expenses.setDescription(expenseForm.getDescription());
        expenses.setName(expenseForm.getName());
        ExpensesCategory expensesCategory = expenseCategoryService.getExpensesCategoryById(expenseForm.getExpensesCategoryId());
        expenses.setExpensesCategory(expensesCategory);
        return expenses;
    }
}
