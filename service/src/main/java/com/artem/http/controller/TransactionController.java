package com.artem.http.controller;

import com.artem.model.dto.PageResponse;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionFilter;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.model.type.TransactionType;
import com.artem.service.BankAccountService;
import com.artem.service.TransactionService;
import com.artem.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;

    @GetMapping
    public String findAll(Model model, TransactionFilter filter, Pageable pageable) {
        var page = transactionService.findAll(filter, pageable);
        model.addAttribute("transactions", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "transaction/transactions";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return transactionService.findById(id)
                .map(transaction -> {
                    model.addAttribute("bankAccount", bankAccountService.findById(transaction.bankAccountId()).get());
                    model.addAttribute("transaction", transaction);
                    return "transaction/transaction";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("transaction") TransactionCreateDto transaction) {
        model.addAttribute("bankAccounts", bankAccountService.findAllIdsByUserId(UserDetailsUtil.getCurrentUserId()));
        model.addAttribute("transaction", transaction);
        model.addAttribute("types", TransactionType.values());
        return "transaction/registration";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated TransactionCreateDto transaction) {
        return "redirect:/transactions/" + transactionService.create(transaction).id();
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated TransactionUpdateDto transactionUpdateDto, @PathVariable("id") Long id) {
        return transactionService.update(id, transactionUpdateDto)
                .map(it -> "redirect:/transactions/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!transactionService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/transactions";
    }
}
