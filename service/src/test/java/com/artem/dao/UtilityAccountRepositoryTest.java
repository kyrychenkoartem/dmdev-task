package com.artem.dao;

import com.artem.mapper.UtilityAccountMapper;
import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountUpdateDto;
import com.artem.model.entity.UtilityAccount;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_UTILITY_ACCOUNTS;
import static com.artem.util.ConstantUtil.PROVIDER_NAME_EXPECTED;
import static org.assertj.core.api.Assertions.assertThat;

public class UtilityAccountRepositoryTest extends RepositoryTestBase {

    private final UtilityAccountRepository utilityAccountRepository = new UtilityAccountRepository(session);
    private final UtilityAccountMapper accountMapper = new UtilityAccountMapper();

    @Test
    void checkUtilityAccountSave() {
        UtilityAccount expectedUtilityAccount = saveUtilityAccount();
        session.clear();

        assertThat(utilityAccountRepository.findById(expectedUtilityAccount.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkUtilityAccountDelete() {
        var utilityAccount = saveUtilityAccount();
        var actualUtilityAccount = utilityAccountRepository.findById(utilityAccount.getId());

        utilityAccountRepository.delete(actualUtilityAccount.get());

        assertThat(utilityAccountRepository.findById(actualUtilityAccount.get().getId())).isEmpty();
    }

    @Test
    void checkUtilityAccountUpdate() {
        var utilityAccount = saveUtilityAccount();
        var actualUtilityAccount = utilityAccountRepository.findById(utilityAccount.getId());
        var updateDto = getUpdateDto();
        var expectedUtilityAccount = accountMapper.mapFrom(actualUtilityAccount.get(), updateDto);

        utilityAccountRepository.update(expectedUtilityAccount);
        session.clear();

        assertThat(utilityAccountRepository.findById(expectedUtilityAccount.getId()).get().getProviderName())
                .isEqualTo(PROVIDER_NAME_EXPECTED);
    }

    @Test
    void checkUtilityAccountFindById() {
        var expectedUtilityAccount = saveUtilityAccount();
        session.clear();

        var actualUtilityAccount = utilityAccountRepository.findById(expectedUtilityAccount.getId());

        assertThat(expectedUtilityAccount).isEqualTo(actualUtilityAccount.get());
    }

    @Test
    void checkFindAllUtilityAccounts() {
        var accountList = utilityAccountRepository.findAll();

        assertThat(accountList.size()).isEqualTo(ALL_UTILITY_ACCOUNTS);
    }

    private UtilityAccountCreateDto getAccountCreateDto() {
        return UtilityAccountCreateDto.builder()
                .number("123454454")
                .providerName("Test")
                .build();
    }

    private UtilityAccount saveUtilityAccount() {
        var accountCreateDto = getAccountCreateDto();
        var utilityAccount = accountMapper.mapFrom(accountCreateDto);
        var expectedUtilityAccount = utilityAccountRepository.save(utilityAccount);
        return expectedUtilityAccount;
    }

    private UtilityAccountUpdateDto getUpdateDto() {
        return UtilityAccountUpdateDto.builder()
                .providerName("Test1")
                .build();
    }
}
