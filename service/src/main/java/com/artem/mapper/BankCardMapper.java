package com.artem.mapper;

import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.BankCard;

public class BankCardMapper implements Mapper<BankCardCreateDto, BankCard> {
    @Override
    public BankCard mapFrom(BankCardCreateDto createDto) {
        return BankCard.builder()
                .user(createDto.user())
                .bankAccount(createDto.bankAccount())
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
