package com.artem.http.controller;


import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import com.artem.service.BankCardService;
import com.artem.util.UserDetailsUtil;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/bank-cards")
public class BankCardController {

    private final BankCardService bankCardService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("bankCards", bankCardService.findAll());
        return "bank-card/bank-cards";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return bankCardService.findById(id)
                .map(bankCard -> {
                    model.addAttribute("bankCard", bankCard);
                    return "bank-card/bank-card";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model,
                               @ModelAttribute("bankCard") BankCardCreateDto bankCard,
                               HttpSession session,
                               HttpServletRequest request) {
        model.addAttribute("userId", UserDetailsUtil.getCurrentUserId());
        model.addAttribute("bankAccountId", Optional.ofNullable(session.getAttribute("bankAccountId")));
        model.addAttribute("card", bankCard);
        model.addAttribute("banks", BankType.values());
        model.addAttribute("types", CardType.values());
        String previousUrl = request.getHeader("Referer");
        if (session.getAttribute("bankAccountId") == null) {
            return "redirect:" + previousUrl;
        } else {
            model.addAttribute("bankAccountId", session.getAttribute("bankAccountId"));
        }
        return "bank-card/registration";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated BankCardCreateDto bankCard) {
        return "redirect:/bank-cards/" + bankCardService.create(bankCard).id();
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated BankCardUpdateDto bankCard, @PathVariable("id") Long id) {
        return bankCardService.update(id, bankCard)
                .map(it -> "redirect:/bank-cards/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!bankCardService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/bank-cards";
    }
}
