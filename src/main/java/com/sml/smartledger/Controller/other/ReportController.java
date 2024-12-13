package com.sml.smartledger.Controller.other;


import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
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

      PartyService partyService;



       @Autowired
        public ReportController(PartyTransactionService partyTransactionService,UserService userService,PartyService partyService) {
                this.partyTransactionService = partyTransactionService;
              this.userService = userService;
                this.partyService = partyService;
        }

      @GetMapping("/transaction/customer")
      public String transactionCustomerReport(Model model, Authentication authentication) {

            String email = getEmailOfLoggedInUser(authentication);
            User user = userService.getUserByEmail(email);

            Business business = user.getSelectedBusiness();
            List<Party> parties = partyService.getCustomerParty(business.getId());
            List<PartyTransaction> partyTransactions = partyTransactionService.getAllByPartyIn(parties);

            model.addAttribute("partyTransactions", partyTransactions);
            model.addAttribute("business", business);
            model.addAttribute("partyType", "Customer");

            return "user/reports/customer_transaction";
      }

    @GetMapping("/transaction/supplier")
    public String transactionSupplierReport(Model model, Authentication authentication) {

        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        Business business = user.getSelectedBusiness();
        List<Party> parties = partyService.getSupplierParty(business.getId());

        List<PartyTransaction> partyTransactions = partyTransactionService.getAllByPartyIn(parties);

        model.addAttribute("partyTransactions", partyTransactions);
        model.addAttribute("business", business);
        model.addAttribute("partyType", "Supplier");

        return "user/reports/supplier_transaction";
    }



}
