package com.artem.http.controller;

import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountUpdateDto;
import com.artem.service.UtilityAccountService;
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
@RequestMapping("/utility-accounts")
public class UtilityAccountController {

    private final UtilityAccountService utilityAccountService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("utilityAccounts", utilityAccountService.findAll());
        return "utility-account/utility-accounts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return utilityAccountService.findById(id)
                .map(utilityAccount -> {
                    model.addAttribute("utilityAccount", utilityAccount);
                    return "utility-account/utility-account";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("utilityAccount") UtilityAccountCreateDto utilityAccount) {
        model.addAttribute("utilityAccount", utilityAccount);
        return "utility-account/registration";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated UtilityAccountCreateDto utilityAccount) {
        return "redirect:/utility-accounts/" + utilityAccountService.create(utilityAccount).id();
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated UtilityAccountUpdateDto utilityAccount, @PathVariable("id") Long id) {
        return utilityAccountService.update(id, utilityAccount)
                .map(it -> "redirect:/utility-accounts/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!utilityAccountService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/utility-accounts";
    }
}
