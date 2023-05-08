package com.artem.security;

import com.artem.util.UserDetailsUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        var currentUserId = UserDetailsUtil.getCurrentUserId(authentication);
        String successUrl = "/users/" + currentUserId;
        setDefaultTargetUrl(successUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}