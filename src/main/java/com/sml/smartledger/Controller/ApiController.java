package com.sml.smartledger.Controller;

import com.sml.smartledger.Services.implementetion.party.PartyTransactionServiceImpl;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class ApiController {
    final PartyTransactionService partyTransactionService;
    @Autowired
    public ApiController(PartyTransactionService partyTransactionService) {
        this.partyTransactionService = partyTransactionService;
    }

    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id){
        partyTransactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
