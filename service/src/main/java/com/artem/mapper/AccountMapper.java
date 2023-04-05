package com.artem.mapper;

import com.artem.repository.UserRepository;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.type.AccountStatus;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper implements Mapper<AccountCreateDto, Account> {

    private final UserRepository userRepository;

    @Override
    public Account mapFrom(AccountCreateDto createDto) {
        var user = userRepository.findById(createDto.userId()).get();
        return Account.builder()
                .user(user)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(user.getEmail())
                .build();
    }

    public Account mapFrom(Account account, AccountUpdateDto updateDto) {
        account.setStatus(updateDto.status());
        account.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        account.setUpdatedBy(account.getUser().getEmail());
        return account;
    }
}
