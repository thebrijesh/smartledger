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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    private final UserService userService;
    private final BusinessService businessService;
    private final PartyService partyService;

    @Autowired
    public PageController(PartyService partyService,UserService userService, BusinessService businessService) {
        this.userService = userService;
        this.businessService = businessService;
        this.partyService = partyService;
    }

    @GetMapping("/")
    public String homes(Model model) {
        model.addAttribute("isPublicPage", true);
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("isPublicPage", true);
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("isPublicPage", true);
        return "about";
    }

    @RequestMapping("/services")
    public String services(Model model) {
        model.addAttribute("isPublicPage", true);
        return "services";
    }

    @RequestMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("isPublicPage", true);
        return "contact";
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
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(true);
        user.setProfilePic(
                "https://www.learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Fdurgesh_sir.35c6cb78.webp&w=1920&q=75");

        Business business = new Business();
        business.setName(userForm.getName());
        business.setLogo(user.getProfilePic());
        business = businessService.createBusiness(business);
        user.setSelectedBusiness(business);
//        user.getBusinessList().add(business);

        User savedUser = userService.saveUser(user);

        Message message = new Message("User Registered Successfully !!", MessageType.green);
        session.setAttribute("message", message);
        return "redirect:/signup";
    }

    @RequestMapping("/{shortCode}")
    public String shortCode(@PathVariable("shortCode") String shortCode ,  Model model) {

        Party party = partyService.getPartyByShortCode(shortCode);
        model.addAttribute("party", party);

        return "transaction_view";
    }

}
