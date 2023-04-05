package com.artem.mapper;

import com.artem.repository.AccountRepository;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.BankAccount;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountMapper implements Mapper<BankAccountCreateDto, BankAccount> {

    private final AccountRepository accountRepository;

    @Override
    public BankAccount mapFrom(BankAccountCreateDto createDto) {
        var account = accountRepository.findById(createDto.accountId()).get();
        var user = account.getUser();
        Hibernate.initialize(account);
        Hibernate.initialize(user);
        return BankAccount.builder()
                .account(account)
                .number(createDto.number())
                .type(createDto.accountType())
                .status(createDto.accountStatus())
                .availableBalance(createDto.availableBalance())
                .actualBalance(createDto.actualBalance())
                .build();
    }

    public BankAccount mapFrom(BankAccount bankAccount, BankAccountUpdateDto updateDto) {
        bankAccount.setType(updateDto.accountType());
        bankAccount.setStatus(updateDto.accountStatus());
        bankAccount.setAvailableBalance(updateDto.availableBalance());
        bankAccount.setActualBalance(updateDto.actualBalance());
        return bankAccount;
    }
}
