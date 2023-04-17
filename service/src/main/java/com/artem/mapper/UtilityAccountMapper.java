package com.artem.mapper;

import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountReadDto;
import com.artem.model.dto.UtilityAccountUpdateDto;
import com.artem.model.entity.UtilityAccount;
import com.artem.repository.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilityAccountMapper implements Mapper<UtilityAccountCreateDto, UtilityAccount> {

    private final AccountRepository accountRepository;

    @Override
    public UtilityAccount mapFrom(UtilityAccountCreateDto createDto) {
        var account = accountRepository.findById(createDto.accountId()).get();
        Hibernate.initialize(account);
        return UtilityAccount.builder()
                .account(account)
                .number(createDto.number())
                .providerName(createDto.providerName())
                .build();
    }

    public UtilityAccountReadDto mapFrom(UtilityAccount utilityAccount) {
        return UtilityAccountReadDto.builder()
                .id(utilityAccount.getId())
                .accountId(utilityAccount.getAccount().getId())
                .number(utilityAccount.getNumber())
                .providerName(utilityAccount.getProviderName())
                .build();
    }

    public List<UtilityAccountReadDto> mapFrom(List<UtilityAccount> utilityAccounts) {
        return utilityAccounts.stream()
                .map(it -> UtilityAccountReadDto.builder()
                        .id(it.getId())
                        .accountId(it.getAccount().getId())
                        .number(it.getNumber())
                        .providerName(it.getProviderName())
                        .build())
                .toList();
    }

    public UtilityAccount mapFrom(UtilityAccount utilityAccount, UtilityAccountUpdateDto updateDto) {
        utilityAccount.setProviderName(updateDto.providerName());
        return utilityAccount;
    }
}
