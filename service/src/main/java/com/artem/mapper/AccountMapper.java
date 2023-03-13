package com.artem.mapper;

import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.type.AccountStatus;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.hibernate.Hibernate;

public class AccountMapper implements Mapper<AccountCreateDto, Account> {
    @Override
    public Account mapFrom(AccountCreateDto createDto) {
        return Account.builder()
                .user(createDto.user())
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(createDto.user().getEmail())
                .build();
    }

    public Account mapFrom(Account account, AccountUpdateDto updateDto) {
        var user = account.getUser();
        Hibernate.initialize(user);
        account.setStatus(updateDto.status());
        account.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        account.setUpdatedBy(account.getUser().getEmail());
        return account;
    }
}
