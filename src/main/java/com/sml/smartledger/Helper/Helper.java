package com.sml.smartledger.Helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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



    public static Date convertStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //genarate 8 character short code for party through uuid
    public static String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
