package com.artem.http.controller;

import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.type.AccountStatus;
import com.artem.service.AccountService;
import com.artem.service.UserService;
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
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping
    public String findAll(Model model) {
        var accounts = accountService.findAll();
        model.addAttribute("accounts", accounts);
        return "account/accounts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return accountService.findById(id)
                .map(account -> {
                    model.addAttribute("account", account);
                    model.addAttribute("statuses", AccountStatus.values());
                    return "account/account";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("account") AccountCreateDto createDto) {
        model.addAttribute("userId", userService.getId());
        model.addAttribute("account", createDto);
        model.addAttribute("statuses", AccountStatus.values());
        return "account/registration";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated AccountCreateDto account) {
        return "redirect:/accounts/" + accountService.create(account).id();
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated AccountUpdateDto account, @PathVariable("id") Long id) {
        return accountService.update(id, account)
                .map(it -> "redirect:/accounts/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!accountService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/accounts";
    }
}
