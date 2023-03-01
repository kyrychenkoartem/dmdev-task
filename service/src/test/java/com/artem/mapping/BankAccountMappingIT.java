package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountMappingIT extends MappingBaseEntity {

    @Test
    void checkBankAccountGet() {
        var user = getUser("Denis", "Denisov", "denis@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var expectedBankAccount = getBankAccount(account, "123454464754753");
        account.addBankAccount(expectedBankAccount);
        session.save(account);
        session.clear();

        var actualBankAccount = session.get(BankAccount.class, expectedBankAccount.getId());

        assertThat(actualBankAccount).isEqualTo(expectedBankAccount);
    }

    @Test
    void checkBankAccountInsert() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var expectedBankAccount = getBankAccount(account, "12345446475477687875653");
        account.addBankAccount(expectedBankAccount);
        session.save(account);
        session.clear();

        var actualBankAccount = session.get(BankAccount.class, expectedBankAccount.getId());

        assertThat(actualBankAccount.getId()).isNotNull();
    }

    @Test
    void checkBankAccountUpdate() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var expectedBankAccount = getBankAccount(account, "12345446475477687875653");
        account.addBankAccount(expectedBankAccount);
        session.save(account);
        session.clear();
        expectedBankAccount.setStatus(AccountStatus.BLOCKED);
        session.update(expectedBankAccount);
        session.flush();
        session.clear();

        var actualBankAccount = session.get(BankAccount.class, expectedBankAccount.getId());

        assertThat(actualBankAccount.getStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    void checkBankAccountDelete() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var expectedBankAccount = getBankAccount(account, "12345446475477687875653");
        account.addBankAccount(expectedBankAccount);
        session.save(account);
        session.clear();
        session.delete(expectedBankAccount);
        session.flush();
        session.clear();

        var actualBankAccount = session.get(BankAccount.class, expectedBankAccount.getId());

        assertThat(actualBankAccount).isNull();
    }

    private static User getUser(String firstname, String lastname, String email) {
        return User.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .password("123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
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
}
