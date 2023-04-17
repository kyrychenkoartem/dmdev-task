package com.artem.mapper;

import com.artem.model.dto.BankAccountReadDto;
import com.artem.repository.AccountRepository;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.BankAccount;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountMapper implements Mapper<BankAccountCreateDto, BankAccount> {

    private final AccountRepository accountRepository;
    private final BankCardMapper bankCardMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public BankAccount mapFrom(BankAccountCreateDto createDto) {
        var account = accountRepository.findById(createDto.accountId()).get();
        var user = account.getUser();
        Hibernate.initialize(account);
        Hibernate.initialize(user);
        return BankAccount.builder()
                .account(account)
                .number(createDto.number())
                .type(createDto.type())
                .status(createDto.status())
                .availableBalance(createDto.availableBalance())
                .actualBalance(createDto.actualBalance())
                .build();
    }

    public BankAccountReadDto mapFrom(BankAccount bankAccount) {
        return BankAccountReadDto.builder()
                .id(bankAccount.getId())
                .accountId(bankAccount.getAccount().getId())
                .number(bankAccount.getNumber())
                .type(bankAccount.getType())
                .status(bankAccount.getStatus())
                .availableBalance(bankAccount.getAvailableBalance())
                .actualBalance(bankAccount.getActualBalance())
                .bankCards(bankCardMapper.mapFrom(bankAccount.getBankCards()))
                .transactions(transactionMapper.mapFrom(bankAccount.getTransactions()))
                .build();
    }

    public List<BankAccountReadDto> mapFrom(List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .map(it -> BankAccountReadDto.builder()
                        .id(it.getId())
                        .accountId(it.getAccount().getId())
                        .number(it.getNumber())
                        .type(it.getType())
                        .status(it.getStatus())
                        .availableBalance(it.getAvailableBalance())
                        .actualBalance(it.getActualBalance())
                        .build())
                .toList();
    }

    public BankAccount mapFrom(BankAccount bankAccount, BankAccountUpdateDto updateDto) {
        bankAccount.setType(updateDto.accountType());
        bankAccount.setStatus(updateDto.accountStatus());
        bankAccount.setAvailableBalance(updateDto.availableBalance());
        bankAccount.setActualBalance(updateDto.actualBalance());
        return bankAccount;
    }
}
