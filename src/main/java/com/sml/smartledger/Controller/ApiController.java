package com.sml.smartledger.Controller;

import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.implementetion.party.PartyTransactionServiceImpl;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class ApiController {
    final PartyTransactionService partyTransactionService;
    private final BusinessRepository businessRepository;
    PartyService partyService;
    BusinessService businessService;
    @Autowired
    public ApiController(PartyTransactionService partyTransactionService , PartyService partyService, BusinessRepository businessRepository) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
        this.businessRepository = businessRepository;
    }

    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id){
        partyTransactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("delete-party/{id}")
    public ResponseEntity<Void> deleteParty(@PathVariable("id") Long id) {
        partyService.deleteParty(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/get-all-transaction/{id}")
    public ResponseEntity<List<PartyTransaction>> getAllTransaction(@PathVariable("id") Long id){
        Business business =  businessRepository.findById(id).get();
        return new ResponseEntity<>(partyTransactionService.getAllByPartyIn( business.getParties()), HttpStatus.OK);
    }
}
