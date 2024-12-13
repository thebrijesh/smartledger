package com.sml.smartledger.Controller;


import com.sml.smartledger.Forms.BusinessForm;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.logging.Logger;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@ControllerAdvice
public class RootController {

    Logger logger = Logger.getLogger(RootController.class.getName());
    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addLoggedInUserToModel(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("USERNAME: " + userName);
        User user = userService.getUserByEmail(userName);

        String email = getEmailOfLoggedInUser(authentication);
        User saveUser = userService.getUserByEmail(email);
        Business business = saveUser.getSelectedBusiness();

        model.addAttribute("loggedInUser", user);
//      model.addAttribute("selectedBusiness", user.getSelectedBusiness());
        model.addAttribute("profilePic", user.getProfilePic());
        model.addAttribute("selectedBusiness", business);
        model.addAttribute("loggedInUser", saveUser);
        model.addAttribute("businessList", saveUser.getBusinessList());
        model.addAttribute("businessForm", new BusinessForm());


    }
}
