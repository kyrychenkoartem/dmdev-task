package com.artem.util;

import com.artem.security.UserSecurity;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class UserDetailsUtil {

    public static Long getCurrentUserId() {
        var userDetails = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    public static Long getCurrentUserId(Authentication authentication) {
        var principal = (UserSecurity) authentication.getPrincipal();
        return principal.getId();
    }
}
