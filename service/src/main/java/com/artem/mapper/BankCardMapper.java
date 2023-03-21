package com.artem.mapper;

import com.artem.dao.BankAccountRepository;
import com.artem.dao.UserRepository;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.BankCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankCardMapper implements Mapper<BankCardCreateDto, BankCard> {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Override
    public BankCard mapFrom(BankCardCreateDto createDto) {
        var bankAccount = bankAccountRepository.findById(createDto.bankAccountId()).get();
        var user = userRepository.findById(createDto.userId()).get();
        return BankCard.builder()
                .user(user)
                .bankAccount(bankAccount)
                .cardNumber(createDto.cardNumber())
                .expiryDate(createDto.expiryDate())
                .bank(createDto.bank())
                .cvv(createDto.cvv())
                .cardType(createDto.cardType())
                .build();
    }

    public BankCard mapFrom(BankCard bankCard, BankCardUpdateDto updateDto) {
        bankCard.setBankAccount(updateDto.bankAccount());
        bankCard.setExpiryDate(updateDto.expiryDate());
        return bankCard;
    }
}
