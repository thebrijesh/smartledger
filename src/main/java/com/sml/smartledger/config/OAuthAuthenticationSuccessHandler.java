package com.sml.smartledger.config;


import com.sml.smartledger.Controller.business.BusinessController;
import com.sml.smartledger.Helper.AppConstants;
import com.sml.smartledger.Model.Providers;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.UserRepository;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    private UserRepository userRepo;
    private BusinessService businessService;


    @Autowired
    public OAuthAuthenticationSuccessHandler(UserRepository userRepo, BusinessService businessService) {
        this.userRepo = userRepo;
        this.businessService = businessService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        logger.info("OAuth Authentication Success Handler");
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        var auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = auth2AuthenticationToken.getAuthorizedClientRegistrationId();
        User user = new User();
        List<String> roles = new ArrayList<>();
        roles.add(AppConstants.DEFAULT_ROLE);
        user.setRoleList(roles);
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // google
            // google attributes

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using google.");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            // github
            // github attributes
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderId(providerUserId);
            user.setProvider(Providers.GITHUB);

            user.setAbout("This account is created using github");
        }

        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {

            User saveUser = userRepo.save(user);

            System.out.println("user saved:" + saveUser.getEmail());
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/users/party/customer");
    }
}
