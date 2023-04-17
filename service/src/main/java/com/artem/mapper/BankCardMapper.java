package com.artem.mapper;

import com.artem.model.dto.BankCardReadDto;
import com.artem.repository.BankAccountRepository;
import com.artem.repository.UserRepository;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.BankCard;
import java.util.List;
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
                .cardType(createDto.type())
                .build();
    }

    public BankCardReadDto mapFrom(BankCard bankCard) {
        return BankCardReadDto.builder()
                .id(bankCard.getId())
                .userId(bankCard.getUser().getId())
                .bankAccountId(bankCard.getBankAccount().getId())
                .cardNumber(bankCard.getCardNumber())
                .expiryDate(bankCard.getExpiryDate())
                .bank(bankCard.getBank())
                .cvv(bankCard.getCvv())
                .cardType(bankCard.getCardType())
                .build();
    }

    public List<BankCardReadDto> mapFrom(List<BankCard> bankCards) {
        return bankCards.stream()
                .map(it -> BankCardReadDto.builder()
                        .id(it.getId())
                        .userId(it.getUser().getId())
                        .bankAccountId(it.getBankAccount().getId())
                        .cardNumber(it.getCardNumber())
                        .expiryDate(it.getExpiryDate())
                        .bank(it.getBank())
                        .cvv(it.getCvv())
                        .cardType(it.getCardType())
                        .build())
                .toList();
    }

    public BankCard mapFrom(BankCard bankCard, BankCardUpdateDto updateDto) {
        bankAccountRepository.findById(updateDto.bankAccountId()).ifPresent(bankCard::setBankAccount);
        bankCard.setExpiryDate(updateDto.expiryDate());
        return bankCard;
    }
}
