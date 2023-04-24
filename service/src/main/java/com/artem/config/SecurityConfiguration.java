package com.artem.config;

import com.artem.service.AccountService;
import com.artem.service.BankAccountService;
import com.artem.service.BankCardService;
import com.artem.service.UserService;
import com.artem.service.UtilityAccountService;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import static com.artem.model.type.Role.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final AccountService accountService;
    private final BankAccountService bankAccountService;
    private final UtilityAccountService utilityAccountService;
    private final BankCardService bankCardService;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/login", "/users/registration", "/v3/api-docs", "/swagger-ui/**").permitAll()
                        .antMatchers("/users/", "/accounts/", "/bank-accounts/", "/accounts/", "/utility-accounts/",
                                "/bank-cards/").hasAuthority(ADMIN.getAuthority())
                        .antMatchers("/accounts/registration**", "/bank-accounts/registration**",
                                "/utility-accounts/registration**", "/bank-cards/registration**").authenticated()
                        .antMatchers(HttpMethod.POST, "/users").permitAll()
                        .antMatchers(HttpMethod.POST, "/accounts", "/bank-accounts", "/utility-accounts",
                                "/bank-cards").authenticated()
                        .antMatchers("/users/{\\d+}/delete").hasAuthority(ADMIN.getAuthority())
                        .antMatchers("/users/{\\d+}", "/accounts/{\\d+}", "/bank-accounts/{\\d+}", "/utility-accounts/{\\d+}",
                                "bank-cards/{\\d+}").access(authorizationManager())
                        .antMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler))
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService())));
        return http.build();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
        return new CustomAuthorizationManager(userService, accountService, bankAccountService,
                bankCardService, utilityAccountService);
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
            // TODO: 24.04.2022 create user userService.create
            UserDetails userDetails = userService.loadUserByUsername(email);
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());

            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(userDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }

}
