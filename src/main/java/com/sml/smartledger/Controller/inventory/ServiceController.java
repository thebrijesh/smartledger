package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Model.bill.BillService;
import com.sml.smartledger.Services.interfaces.bill.BillServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    BillServiceService serviceService;
    @PostMapping("/add")
    public ResponseEntity<BillService> addService(@RequestBody BillService service) {
        BillService billService = serviceService.addService(service);
        return new ResponseEntity<>(billService, HttpStatus.CREATED);
    }
    @GetMapping("/{billServiceId}")
    public ResponseEntity<List<BillService>> getAllBillServicesById(@PathVariable("billServiceId") Long billServiceId){
        List<BillService> billServices = serviceService.getAllServiceByBusinessId(billServiceId);
        return new ResponseEntity<>(billServices,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServices(@PathVariable("id") Long id){
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
