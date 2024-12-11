package com.sml.smartledger.Controller.other;


import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/other/report")
public class ReportController {

      PartyTransactionService partyTransactionService;
      UserService userService;

       @Autowired
        public ReportController(PartyTransactionService partyTransactionService,UserService userService) {
                this.partyTransactionService = partyTransactionService;
              this.userService = userService;
        }

      @GetMapping("/transaction")
      public String transactionReport(Model model, Authentication authentication) {

            String email = getEmailOfLoggedInUser(authentication);
            User user = userService.getUserByEmail(email);

            Business business = user.getSelectedBusiness();
            List<PartyTransaction> partyTransactions = partyTransactionService.getAllByPartyIn(business.getParties());

            model.addAttribute("partyTransactions", partyTransactions);
            model.addAttribute("business", business);

            return "user/reports/transactions_report";
      }



}
