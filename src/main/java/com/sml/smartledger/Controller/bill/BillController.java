package com.sml.smartledger.Controller.bill;



import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Services.interfaces.bill.MyBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bill")
public class BillController {


    MyBillService billService;
    @Autowired
    public BillController(MyBillService billService) {
        this.billService = billService;
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
}
