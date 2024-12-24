package com.sml.smartledger.Controller;

import com.sml.smartledger.Controller.business.BusinessController;
import com.sml.smartledger.Forms.BusinessForm;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("/users")
public class UserController {

    Logger logger = Logger.getLogger(UserController.class.getName());

    UserService userService;
    private final BusinessService businessService;


    @Autowired
    public UserController(UserService userService, BusinessService businessService) {
        this.userService = userService;
        this.businessService = businessService;
    }


    // user dashboard page

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("isPublicPage", false);
        String email = getEmailOfLoggedInUser(authentication);
        User saveUser = userService.getUserByEmail(email);
        Business business = saveUser.getSelectedBusiness();
        if (business == null) {
            business = businessService.createBusiness(Business.builder()
                    .name(saveUser.getName())
                    .logo(saveUser.getProfilePic())
                    .user(saveUser)
                    .build());
            saveUser.setSelectedBusiness(business);
            userService.saveUser(saveUser);
        }


        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {

        model.addAttribute("isPublicPage", false);

        return "user/profile";
    }

}
