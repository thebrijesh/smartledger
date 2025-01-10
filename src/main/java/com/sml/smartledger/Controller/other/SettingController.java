package com.sml.smartledger.Controller.other;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users/setting")
public class SettingController {
    @GetMapping("view")
    public String settingsPage(Authentication authentication) {
        return "user/setting/setting";
    }
}
