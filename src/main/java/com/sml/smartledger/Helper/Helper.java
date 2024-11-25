package com.sml.smartledger.Helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {
        if(authentication instanceof OAuth2AuthenticationToken){

            var oauth2Principal = (OAuth2AuthenticationToken) authentication;
            var clientid = oauth2Principal.getAuthorizedClientRegistrationId();

            var principal = (OAuth2User)authentication.getPrincipal();
            if(clientid.equalsIgnoreCase("google")){
                return principal.getAttribute("email");
            }else if(clientid.equalsIgnoreCase("github")){
                return principal.getAttribute("email") != null ? principal.getAttribute("email").toString()
                        : principal.getAttribute("login").toString() + "@gmail.com";
            }
        }else{
            return authentication.getName();
        }
        return "";
    }
}
