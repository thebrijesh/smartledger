package com.sml.smartledger.Controller;


import com.sml.smartledger.Forms.UserForm;
import com.sml.smartledger.Helper.Message;
import com.sml.smartledger.Helper.MessageType;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
public class PageController {

    private final UserService userService;
    private final BusinessService businessService;
    private final PartyService partyService;

    private final PasswordEncoder passwordEncoder;

    Logger logger = Logger.getLogger(PageController.class.getName());
    @Autowired
    public PageController(PartyService partyService, UserService userService, BusinessService businessService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.businessService = businessService;
        this.partyService = partyService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String homes(Model model) {
        model.addAttribute("isPublicPage", true);
        return "redirect:/login";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("isPublicPage", true);
        return "home";
    }







    @RequestMapping("/signup")
    public String register(Model model,Authentication authentication) {
        if (authentication != null) {
            return "redirect:/users/dashboard";
        }
        model.addAttribute("isPublicPage", true);
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @RequestMapping("/login")
    public String login(Authentication authentication) {

        if (authentication != null) {
            User user = userService.getUserByEmail(getEmailOfLoggedInUser(authentication));
            logger.info("User password: " + user.getPassword());
            return "redirect:/users/dashboard";
        }
        return "login";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            return "register";
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(true);
        user.setProfilePic(
                "https://www.learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Fdurgesh_sir.35c6cb78.webp&w=1920&q=75");


        User savedUser = userService.saveUser(user);

        Message message = new Message("User Registered Successfully !!", MessageType.green);
        session.setAttribute("message", message);
        return "redirect:/users/dashboard";
    }

    @RequestMapping("/{shortCode}")
    public String shortCode(@PathVariable("shortCode") String shortCode ,  Model model) {

        Party party = partyService.getPartyByShortCode(shortCode);
        model.addAttribute("party", party);
        return "transaction_view";
    }

}
