package com.sml.smartledger.Controller.business;


import com.sml.smartledger.Forms.BusinessForm;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/business")
public class BusinessController {

    BusinessService businessService;
    UserService userService;



    Logger logger = LoggerFactory.getLogger(BusinessController.class);
    @Autowired
    public BusinessController(BusinessService businessService, UserService userService) {
        this.businessService = businessService;
        this.userService = userService;
    }

    @GetMapping("/businesses")
    public String getAllBusiness( Model model, Authentication authentication){
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);
           List<Business> businessList = businessService.getAllBusiness(user.getId());
              model.addAttribute("businessList", businessList);
           return "redirect:/users/dashboard";
    }
    @PostMapping("/create-business")
    public String createBusiness(@ModelAttribute BusinessForm businessForm, Authentication authentication) {
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);

        Business business = Business.builder()
                .name(businessForm.getName())
                .logo(user.getProfilePic())
                .user(user)
                .build();

        // Save business first
        Business savedBusiness = businessService.createBusiness(business);

        // Update user's business list and selected business
        user.setSelectedBusiness(savedBusiness);
        userService.saveUser(user);

        return "redirect:/users/party/customer";
    }
    @DeleteMapping("/businesses/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable("id") Long businessId){
         businessService.deleteBusiness(businessId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/select")
    public String selectBusiness(@RequestParam("selectedBusinessId") Long selectedBusinessId, Authentication authentication,Model model) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business selectedBusiness = businessService.getBusinessById(selectedBusinessId);
        logger.info("Selected business: " + selectedBusiness.getName());
        // Update user's selected business
        user.setSelectedBusiness(selectedBusiness);
        userService.saveUser(user);

        // Return to dashboard with updated business
        return "redirect:/users/dashboard";
    }
}
