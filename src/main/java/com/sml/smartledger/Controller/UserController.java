package com.sml.smartledger.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/users")
public class UserController {

    Logger logger = Logger.getLogger(UserController.class.getName());





    // user dashboard page

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("isPublicPage", false);
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {

        model.addAttribute("isPublicPage", false);

        return "user/profile";
    }

}
