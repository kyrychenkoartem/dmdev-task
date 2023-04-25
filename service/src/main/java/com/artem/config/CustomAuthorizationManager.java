package com.artem.config;

import com.artem.service.AccountService;
import com.artem.service.BankAccountService;
import com.artem.service.BankCardService;
import com.artem.service.UserService;
import com.artem.service.UtilityAccountService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final String REQUIRED_ROLE = "ADMIN";
    private static final String PATH_SEPARATOR = "/";
    private static final String USERS = "users";
    private static final String ACCOUNTS = "accounts";
    private static final String BANK_ACCOUNTS = "bank-accounts";
    private static final String BANK_CARDS = "bank-cards";
    private static final String UTILITY_ACCOUNTS = "utility-accounts";
    private static final Integer ONE = 1;
    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final List<String> urlPaths =
            List.of(USERS, ACCOUNTS, BANK_ACCOUNTS, BANK_CARDS, UTILITY_ACCOUNTS);

    private final UserService userService;
    private final AccountService accountService;
    private final BankAccountService bankAccountService;
    private final BankCardService bankCardService;
    private final UtilityAccountService utilityAccountService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        if (authentication == null || !authentication.get().isAuthenticated()
                || authentication.get().getPrincipal().equals(ANONYMOUS_USER)) {
            return new AuthorizationDecision(false);
        }

        // Get the requested path ID from the URL
        HttpServletRequest request = object.getRequest();
        String[] pathParts = request.getRequestURI().split(PATH_SEPARATOR);
        Long pathId;
        Long authenticatedId = null;
        List<String> urlPath = new ArrayList<>();
        try {
            pathId = Arrays.stream(pathParts)
                    .filter(urlPaths::contains)
                    .findFirst()
                    .map(i -> Long.parseLong(pathParts[pathParts.length - ONE]))
                    .orElse(null);
            if (pathId != null) {
                urlPath.add(Arrays.stream(pathParts)
                        .filter(urlPaths::contains)
                        .findFirst().get());
            }
        } catch (NumberFormatException exception) {
            return new AuthorizationDecision(false);
        }

        // Get the authenticated ID
        if (urlPath.isEmpty()) {
            return new AuthorizationDecision(false);
        }
        String path = urlPath.get(0);
        switch (path) {
            case USERS -> authenticatedId = userService.getId();
            case ACCOUNTS -> authenticatedId = accountService.getId();
            case BANK_ACCOUNTS -> {
                var maybeId = bankAccountService.getId().stream()
                        .filter(it -> it.equals(pathId))
                        .findFirst();
                if (maybeId.isPresent()) {
                    authenticatedId = maybeId.get();
                }
            }
            case BANK_CARDS -> {
                var maybeId = bankCardService.getId().stream()
                        .filter(it -> it.equals(pathId))
                        .findFirst();
                if (maybeId.isPresent()) {
                    authenticatedId = maybeId.get();
                }
            }
            case UTILITY_ACCOUNTS -> {
                var maybeId = utilityAccountService.getId().stream()
                        .filter(it -> it.equals(pathId))
                        .findFirst();
                if (maybeId.isPresent()) {
                    authenticatedId = maybeId.get();
                }
            }
        }

        boolean hasRequiredRole = authentication.get().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(REQUIRED_ROLE));

        if (hasRequiredRole || Objects.equals(pathId, authenticatedId)) {
            return new AuthorizationDecision(true);
        } else {
            return new AuthorizationDecision(false);
        }
    }
}
