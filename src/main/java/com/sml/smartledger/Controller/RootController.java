package com.sml.smartledger.Controller;


import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.logging.Logger;

@ControllerAdvice
public class RootController {

    Logger logger = Logger.getLogger(RootController.class.getName());
    @Autowired
    private UserService userService;
    @ModelAttribute
    public void addLoggedInUserToModel(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("USERNAME: " + userName);
        User user = userService.getUserByEmail(userName);


        model.addAttribute("loggedInUser", user);

    }
}
