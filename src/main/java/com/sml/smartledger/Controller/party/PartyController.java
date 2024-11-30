package com.sml.smartledger.Controller.party;


import com.sml.smartledger.Forms.PartyForm;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Model.party.TransactionType;
import com.sml.smartledger.Services.implementetion.UserServiceImpl;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("/users/party")
public class PartyController {
    final
    PartyService partyService;
    PartyTransactionService partyTransactionService;
    private UserService userService;

    @Autowired
    public PartyController(PartyService partyService, PartyTransactionService partyTransactionService, UserService userService) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
        this.userService = userService;
    }

    @PostMapping("/creteParty")
    public String createParty( @ModelAttribute PartyForm partyForm, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User saveUser = userService.getUserByEmail(email);
        Business business = saveUser.getSelectedBusiness();
        Party party = Party.builder()
                .name(partyForm.getName())
                .mobile(partyForm.getNumber())
                .partyType(PartyType.valueOf(partyForm.getPartyType()))
                .houseNumber(partyForm.getHouseNumber())
                .area(partyForm.getArea())
                .city(partyForm.getCity())
                .state(partyForm.getState())
                .pincode(partyForm.getPincode())
                .gstIN(partyForm.getGstIN())
                .business(business)
                .build();

        party = partyService.createParty(party);
        if (partyForm.getBalance()!=0) {
            PartyTransaction transaction = PartyTransaction.builder()
                    .amount(partyForm.getBalance())
                    .party(party)
                    .transactionType(partyForm.getTransectionType().equals("you-gave") ? TransactionType.DEBIT : TransactionType.CREDIT)
                    .description("Opening Balance")
                    .build();
            partyTransactionService.createTransaction(transaction);
        }
        return "redirect:/users/party/customer";
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

    @GetMapping("/customer")
    public String customer(Model model){
        PartyForm partyForm = new PartyForm();
        partyForm.setBalance(0d);
        partyForm.setPartyType("CUSTOMER");
        model.addAttribute("partyForm", partyForm);
        return "user/party/customers";
    }

    @GetMapping("/supplier")
    public String supplier(Model model){

        PartyForm partyForm = new PartyForm();
        partyForm.setBalance(0d);
        partyForm.setPartyType("SUPPLIER");
        model.addAttribute("partyForm", partyForm);
        return "user/party/suppliers";
    }



}
