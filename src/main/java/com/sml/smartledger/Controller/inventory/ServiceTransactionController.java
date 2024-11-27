package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Model.inventory.ServiceTransaction;
import com.sml.smartledger.Services.interfaces.inventory.ServiceTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/serviceTransaction")
public class ServiceTransactionController {
    @Autowired
    ServiceTransactionService service;

    @GetMapping("/{billServiceId}")
    public ResponseEntity<List<ServiceTransaction>> getAllServiceTransactions(@PathVariable("billServiceId") Long billServiceId) {
        List<ServiceTransaction> serviceTransactionList = service.getAllServiceTransaction(billServiceId);
        return new ResponseEntity<>(serviceTransactionList, HttpStatus.OK);
    }

    @PostMapping("/addServiceTransaction")
    public ResponseEntity<ServiceTransaction> addServiceTransaction(@RequestBody ServiceTransaction serviceTransaction) {
        ServiceTransaction savedServiceTransaction = service.addServiceTransaction(serviceTransaction);
        return new ResponseEntity<>(savedServiceTransaction, HttpStatus.CREATED);
    }
}
