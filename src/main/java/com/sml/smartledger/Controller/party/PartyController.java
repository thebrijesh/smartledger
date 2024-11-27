package com.sml.smartledger.Controller.party;


import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/party")
public class PartyController {
    @Autowired
    PartyService partyService;
    @PostMapping("/creteParty")
    public ResponseEntity<Party> createParty(@RequestBody Party party){
        Party createdParty = partyService.createParty(party);
        return new ResponseEntity<>(createdParty, HttpStatus.CREATED);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity< List<Party>> getAllParty(@PathVariable("businessId") Long businessId){
        List<Party> createdPartyList = partyService.getAllParty(businessId);
        return new ResponseEntity<>(createdPartyList, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Party> deleteParty(@PathVariable Long id){
          partyService.deleteParty(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
