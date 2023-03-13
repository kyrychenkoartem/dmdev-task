package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.BankCard;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BankCardMappingIT extends MappingBaseEntity {

    @Test
    void checkBankCardGet() {
        var user = getUser("Artem", "Artemov", "artem@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456789907654567");
        account.addBankAccount(bankAccount);
        var expectedBankCard = getBankCard(user, bankAccount, "1234565432345");
        bankAccount.addBankCard(expectedBankCard);
        session.save(account);
        session.flush();
        session.clear();

        var actualBankCard = session.get(BankCard.class, expectedBankCard.getId());

        assertThat(actualBankCard).isEqualTo(expectedBankCard);
    }

    @Test
    void checkBankCardInsert() {
        var user = getUser("John", "John", "john@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "12345678990765f45454567");
        account.addBankAccount(bankAccount);
        var expectedBankCard = getBankCard(user, bankAccount, "12345654323465456565");
        bankAccount.addBankCard(expectedBankCard);
        session.save(account);
        session.flush();
        session.clear();

        var actualBankCard = session.get(BankCard.class, expectedBankCard.getId());

        assertThat(actualBankCard.getId()).isNotNull();
    }

    @Test
    void checkBankCardUpdate() {
        var user = getUser("John", "John", "john@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "12345678990765f45454567");
        account.addBankAccount(bankAccount);
        var expectedBankCard = getBankCard(user, bankAccount, "12345654323465456565");
        bankAccount.addBankCard(expectedBankCard);
        session.save(account);
        session.flush();
        session.clear();
        expectedBankCard.setBank(BankType.CIBC);
        session.update(expectedBankCard);
        session.flush();
        session.clear();

        var actualBankCard = session.get(BankCard.class, expectedBankCard.getId());

        assertThat(actualBankCard.getBank()).isEqualTo(BankType.CIBC);
    }

    @Test
    void checkBankCardDelete() {
        var user = getUser("John", "John", "john@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "12345678990765f45454567");
        account.addBankAccount(bankAccount);
        var expectedBankCard = getBankCard(user, bankAccount, "12345654323465456565");
        bankAccount.addBankCard(expectedBankCard);
        session.save(account);
        session.flush();
        session.clear();
        session.delete(expectedBankCard);
        session.flush();
        session.clear();

        var actualBankCard = session.get(BankCard.class, expectedBankCard.getId());

        assertThat(actualBankCard).isNull();
    }


    private static User getUser(String firstname, String lastname, String email) {
        return User.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .password("123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private static Account getAccount(User user) {
        return Account.builder()
                .user(user)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(user.getEmail())
                .build();
    }

    private static BankAccount getBankAccount(Account account, String number) {
        return BankAccount.builder()
                .account(account)
                .number(number)
                .type(AccountType.CHECKING_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private static BankCard getBankCard(User user, BankAccount bankAccount, String cardNumber) {
        return BankCard.builder()
                .user(user)
                .bankAccount(bankAccount)
                .cardNumber(cardNumber)
                .expiryDate("01/25")
                .bank(BankType.NOVA_SCOTIA_BANK)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .build();
    }
}
