package com.artem.mapper;

import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountUpdateDto;
import com.artem.model.entity.UtilityAccount;
import org.springframework.stereotype.Component;

@Component
public class UtilityAccountMapper implements Mapper<UtilityAccountCreateDto, UtilityAccount> {

    @Override
    public UtilityAccount mapFrom(UtilityAccountCreateDto createDto) {
        return UtilityAccount.builder()
                .number(createDto.number())
                .providerName(createDto.providerName())
                .build();
    }

    public UtilityAccount mapFrom(UtilityAccount utilityAccount, UtilityAccountUpdateDto updateDto) {
        utilityAccount.setProviderName(updateDto.providerName());
        return utilityAccount;
    }
}
