package com.sml.smartledger.Controller.bill;



import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.Expanses;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.implementetion.UserServiceImpl;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.bill.BillService;
import com.sml.smartledger.Services.interfaces.bill.ExpansesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/transaction")
public class ExpansesController {

    ExpansesService expansesService;
     UserService userService;

    @Autowired
    public ExpansesController(ExpansesService expansesService, UserService userService) {
        this.expansesService = expansesService;
        this.userService = userService;
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<Expanses>> getAllExpanses(@PathVariable("businessId") Long businessId){
        List<Expanses> expansesList = expansesService.getAllExpanses(businessId);
        return new ResponseEntity<>(expansesList, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Expanses> createExpanses(@RequestBody Expanses expanses){
        Expanses saveExpanses = expansesService.createExpanses(expanses);
        return new ResponseEntity<>(saveExpanses, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpanses(@PathVariable("id") Long expansesId){
        expansesService.deleteExpanses(expansesId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/expanse")
    public String getAllExpanses(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();

        List<Expanses> expansesList = expansesService.getAllExpanses(business.getId());
        model.addAttribute("expanses", expansesList);
        model.addAttribute("billType", "Expanse");
        return "/user/bill/sales";
    }
}
