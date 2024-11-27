package com.sml.smartledger.Controller.bill;



import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.MyBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/transactions")
public class BillController {


    MyBillService billService;
    UserService userService;
    @Autowired
    public BillController(MyBillService billService, UserService userService) {
        this.billService = billService;
        this.userService = userService;
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<Bill>> getAllBills(@PathVariable("businessId") Long businessId) {
        List<Bill> billList = billService.getAllBills(businessId);
        return new ResponseEntity<>(billList, HttpStatus.OK);
    }

    @PostMapping("/create-bill")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill savedBill = billService.createBill(bill);
        return new ResponseEntity<>(savedBill, HttpStatus.CREATED);
    }

    @GetMapping("/sales")
    public String getAllSellBill(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();

        List<Bill> billList = billService.getAllSaleBills(business.getId());
        model.addAttribute("bills", billList);
        return "user/bill/sales";
    }
}
