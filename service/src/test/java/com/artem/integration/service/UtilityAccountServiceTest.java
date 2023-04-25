package com.artem.integration.service;

import com.artem.mapper.UtilityAccountMapper;
import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountReadDto;
import com.artem.model.dto.UtilityAccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.entity.User;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import com.artem.repository.UtilityAccountRepository;
import com.artem.service.UtilityAccountService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.artem.util.ConstantUtil.ACCOUNT_1;
import static com.artem.util.ConstantUtil.ALL_3_UTILITY_ACCOUNTS;
import static com.artem.util.ConstantUtil.USER_1;
import static com.artem.util.ConstantUtil.UTILITY_ACCOUNT_1;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UtilityAccountServiceTest {

    @Mock
    private UtilityAccountRepository utilityAccountRepository;

    @Mock
    private UtilityAccountMapper utilityAccountMapper;

    @InjectMocks
    private UtilityAccountService utilityAccountService;

    @Test
    void checkFindAll() {
        var utilityAccountList = List.of(getUtilityAccount(), getUtilityAccount(), getUtilityAccount());
        var expectedResponse = List.of
                (getUtilityAccountReadDto(), getUtilityAccountReadDto(), getUtilityAccountReadDto());
        doReturn(utilityAccountList).when(utilityAccountRepository).findAll();
        doReturn(getUtilityAccountReadDto(), getUtilityAccountReadDto(), getUtilityAccountReadDto())
                .when(utilityAccountMapper).mapFrom(any(UtilityAccount.class));

        var actualResponse = utilityAccountService.findAll();

        assertThat(actualResponse).hasSize(ALL_3_UTILITY_ACCOUNTS);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkFindById() {
        var utilityAccount = getUtilityAccount();
        var expectedResponse = getUtilityAccountReadDto();
        doReturn(Optional.of(utilityAccount)).when(utilityAccountRepository).findById(UTILITY_ACCOUNT_1);
        doReturn(expectedResponse).when(utilityAccountMapper).mapFrom(utilityAccount);

        var actualResponse = utilityAccountService.findById(UTILITY_ACCOUNT_1);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkCreate() {
        var utilityAccountCreateDto = getUtilityAccountCreateDto();
        var utilityAccount = getUtilityAccount();
        var expectedResponse = getUtilityAccountReadDto();
        doReturn(utilityAccount).when(utilityAccountMapper).mapFrom(utilityAccountCreateDto);
        doReturn(utilityAccount).when(utilityAccountRepository).save(utilityAccount);
        doReturn(expectedResponse).when(utilityAccountMapper).mapFrom(utilityAccount);

        var actualResponse = utilityAccountService.create(utilityAccountCreateDto);

        assertThat(actualResponse.id()).isNotNull();
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkUpdate() {
        var utilityAccount = getUtilityAccount();
        var utilityAccountUpdateDto = getUtilityAccountUpdateDto();
        var updatedUtilityAccount = getUpdatedUtilityAccount();
        var expectedResponse = getUpdatedUtilityAccountReadDto();
        doReturn(Optional.of(utilityAccount)).when(utilityAccountRepository).findById(UTILITY_ACCOUNT_1);
        doReturn(updatedUtilityAccount).when(utilityAccountMapper).mapFrom(utilityAccount, utilityAccountUpdateDto);
        doReturn(updatedUtilityAccount).when(utilityAccountRepository).saveAndFlush(updatedUtilityAccount);
        doReturn(expectedResponse).when(utilityAccountMapper).mapFrom(updatedUtilityAccount);

        var actualResponse = utilityAccountService.update(UTILITY_ACCOUNT_1, utilityAccountUpdateDto);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkDelete() {
        var utilityAccount = getUtilityAccount();
        doReturn(Optional.of(utilityAccount)).when(utilityAccountRepository).findById(UTILITY_ACCOUNT_1);

        var expectedResult = utilityAccountService.delete(UTILITY_ACCOUNT_1);

        assertTrue(expectedResult);
    }

    private User getUser() {
        return User.builder()
                .id(USER_1)
                .firstName("Test")
                .lastName("Test")
                .email("test@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private Account getAccount() {
        return Account.builder()
                .id(ACCOUNT_1)
                .user(getUser())
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2000, 1, 1, 0, 0).truncatedTo(DAYS))
                .createdBy(getUser().getEmail())
                .build();
    }

    private UtilityAccountCreateDto getUtilityAccountCreateDto() {
        return UtilityAccountCreateDto.builder()
                .accountId(getAccount().getId())
                .number("1234")
                .providerName("TestProvider")
                .build();
    }

    private UtilityAccount getUtilityAccount() {
        return UtilityAccount.builder()
                .id(UTILITY_ACCOUNT_1)
                .number("1234")
                .providerName("TestProvider")
                .account(getAccount())
                .build();
    }

    private UtilityAccountReadDto getUtilityAccountReadDto() {
        return UtilityAccountReadDto.builder()
                .id(UTILITY_ACCOUNT_1)
                .accountId(ACCOUNT_1)
                .number("1234")
                .providerName("TestProvider")
                .build();
    }

    private UtilityAccountUpdateDto getUtilityAccountUpdateDto() {
        return UtilityAccountUpdateDto.builder()
                .providerName("UpdatedProvider")
                .build();
    }

    private UtilityAccount getUpdatedUtilityAccount() {
        return UtilityAccount.builder()
                .id(UTILITY_ACCOUNT_1)
                .number("1234")
                .providerName("UpdatedProvider")
                .account(getAccount())
                .build();
    }

    private UtilityAccountReadDto getUpdatedUtilityAccountReadDto() {
        return UtilityAccountReadDto.builder()
                .id(UTILITY_ACCOUNT_1)
                .accountId(ACCOUNT_1)
                .number("1234")
                .providerName("UpdatedProvider")
                .build();
    }
}
