package com.sml.smartledger.Controller;

import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Services.implementetion.party.PartyTransactionServiceImpl;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController()
@RequestMapping("/api")
public class ApiController {
    final PartyTransactionService partyTransactionService;
    PartyService partyService;
    @Autowired
    public ApiController(PartyTransactionService partyTransactionService , PartyService partyService) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
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
}
