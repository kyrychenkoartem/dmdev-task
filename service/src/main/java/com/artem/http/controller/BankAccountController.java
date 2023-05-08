package com.artem.http.controller;

import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.service.AccountService;
import com.artem.service.BankAccountService;
import com.artem.util.UserDetailsUtil;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final AccountService accountService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("bankAccounts", bankAccountService.findAll());
        return "bank-account/bank-accounts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model, HttpSession session) {
        return bankAccountService.findById(id)
                .map(bankAccount -> {
                    model.addAttribute("bankAccount", bankAccount);
                    model.addAttribute("statuses", AccountStatus.values());
                    model.addAttribute("types", AccountType.values());
                    session.setAttribute("bankAccountId", bankAccount.id());
                    return "bank-account/bank-account";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("bankAccount") BankAccountCreateDto bankAccount) {
        Optional<Long> accountId = Optional.empty();
        var maybeAccount = accountService.findByUserId(UserDetailsUtil.getCurrentUserId());
        if (maybeAccount.isPresent()) {
            accountId = Optional.of(maybeAccount.get().id());
        }
        model.addAttribute("accountId", accountId.get());
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("statuses", AccountStatus.values());
        model.addAttribute("types", AccountType.values());
        return "bank-account/registration";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated BankAccountCreateDto bankAccount) {
        return "redirect:/bank-accounts/" + bankAccountService.create(bankAccount).id();
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated BankAccountUpdateDto bankAccount, @PathVariable("id") Long id) {
        return bankAccountService.update(id, bankAccount)
                .map(it -> "redirect:/bank-accounts/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!bankAccountService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/bank-accounts";
    }
}
