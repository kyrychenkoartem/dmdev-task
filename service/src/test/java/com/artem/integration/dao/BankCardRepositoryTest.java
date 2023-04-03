package com.artem.integration.dao;

import com.artem.dao.AccountRepository;
import com.artem.dao.BankAccountRepository;
import com.artem.dao.BankCardRepository;
import com.artem.dao.UserRepository;
import com.artem.mapper.AccountMapper;
import com.artem.mapper.BankAccountMapper;
import com.artem.mapper.BankCardMapper;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.BankCard;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;

import com.artem.model.type.Role;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_BANK_CARDS;
import static com.artem.util.ConstantUtil.EXPIRY_DATE_EXPECTED;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class BankCardRepositoryTest extends RepositoryTestBase {

    private final BankCardRepository cardRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final BankCardMapper cardMapper;
    private final EntityManager session;

    @Test
    void checkSaveBankCard() {
        BankCard expectedBankCard = saveBankCard();
        session.clear();

        assertThat(cardRepository.findById(expectedBankCard.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkBankCardDelete() {
        BankCard expectedBankCard = saveBankCard();
        var actualBankCard = cardRepository.findById(expectedBankCard.getId());

        cardRepository.delete(actualBankCard.get());

        assertThat(cardRepository.findById(actualBankCard.get().getId())).isEmpty();
    }

    @Test
    void checkUpdateBankCard() {
        BankCard expectedBankCard = saveBankCard();
        var maybeBankCard = cardRepository.findById(expectedBankCard.getId());
        var cardUpdateDto = getBankCardUpdateDto(maybeBankCard.get().getBankAccount());
        var bankCardToUpdate = cardMapper.mapFrom(maybeBankCard.get(), cardUpdateDto);

        cardRepository.saveAndFlush(bankCardToUpdate);
        session.clear();
        var actualBankCard = cardRepository.findById(bankCardToUpdate.getId()).get();

        assertThat(actualBankCard)
                .isEqualTo(cardRepository.findById(expectedBankCard.getId()).get());
        assertThat(actualBankCard.getExpiryDate()).isEqualTo(EXPIRY_DATE_EXPECTED);
    }

    @Test
    void checkBankCardFindById() {
        var expectedBankCard = saveBankCard();

        var actualBankCard = cardRepository.findById(expectedBankCard.getId());

        assertThat(expectedBankCard).isEqualTo(actualBankCard.get());
    }

    @Test
    void checkFindAllBankCards() {
        saveBankCard();

        var bankCardList = cardRepository.findAll();

        assertThat(bankCardList.size()).isEqualTo(ALL_BANK_CARDS);
    }

    private UserCreateDto getUserCreateDto() {
        return UserCreateDto.builder()
                .firstname("Test")
                .lastname("Test")
                .email("test@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2023, 3, 11))
                .role(Role.USER)
                .build();
    }

    private BankAccountCreateDto getBankAccountCreateDto() {
        var userCreateDto = getUserCreateDto();
        var user = userMapper.mapFrom(userCreateDto);
        var actualUser = userRepository.save(user);
        var accountCreateDto = AccountCreateDto.builder()
                .userId(actualUser.getId())
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        var expectedAccount = accountRepository.save(account);
        return BankAccountCreateDto.builder()
                .accountId(expectedAccount.getId())
                .number("234554356765646586")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount saveBankAccount() {
        var accountCreateDto = getBankAccountCreateDto();
        var bankAccount = bankAccountMapper.mapFrom(accountCreateDto);
        return bankAccountRepository.save(bankAccount);
    }

    private BankCardCreateDto getBankCardCreateDto() {
        var bankAccount = saveBankAccount();
        return BankCardCreateDto.builder()
                .userId(bankAccount.getAccount().getUser().getId())
                .bankAccountId(bankAccount.getId())
                .cardNumber("4567906543456789")
                .expiryDate("12/28")
                .bank(BankType.CIBC)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .build();
    }

    private BankCardUpdateDto getBankCardUpdateDto(BankAccount bankAccount) {
        return BankCardUpdateDto.builder()
                .bankAccount(bankAccount)
                .expiryDate("11/30")
                .build();
    }

    private BankCard saveBankCard() {
        var bankCardCreateDto = getBankCardCreateDto();
        var bankCard = cardMapper.mapFrom(bankCardCreateDto);
        return cardRepository.save(bankCard);
    }
}
