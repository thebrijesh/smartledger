package com.sml.smartledger.Controller.business;

import com.sml.smartledger.Model.bill.Bill;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    BusinessService businessService;
    @GetMapping("/businesses/{userId}")
    public ResponseEntity<List<Business>> getAllBusiness(@PathVariable("userId") String userId){
           List<Business> businessList = businessService.getAllBusiness(userId);
           return new ResponseEntity<>(businessList, HttpStatus.OK);
    }
    @PostMapping("/businesses")
    public ResponseEntity<Business> createBusiness(@RequestBody Business business){
        Business saveBusiness = businessService.createBusiness(business);
        return new ResponseEntity<>(saveBusiness, HttpStatus.CREATED);
    }
    @DeleteMapping("/businesses/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable("id") Long businessId){
         businessService.deleteBusiness(businessId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
