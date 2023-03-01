package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.User;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.entity.UtilityPayment;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilityPaymentMappingIT extends MappingBaseEntity {

    @Test
    void checkUtilityPaymentGet() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var transaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(transaction);
        var utilityAccount = getUtilityAccount("112345654356", "Telus");
        var expectedUtilityPayment = getUtilityPayment(
                utilityAccount.getNumber(), utilityAccount, transaction);
        utilityAccount.addUtilityPayment(expectedUtilityPayment);
        session.save(account);
        session.save(utilityAccount);
        session.clear();

        var actualUtilityPayment = session.get(UtilityPayment.class, expectedUtilityPayment.getId());

        assertThat(actualUtilityPayment).isEqualTo(expectedUtilityPayment);
    }

    @Test
    void checkUtilityPaymentInsert() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var transaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(transaction);
        var utilityAccount = getUtilityAccount("112345654356", "Telus");
        var expectedUtilityPayment = getUtilityPayment(
                utilityAccount.getNumber(), utilityAccount, transaction);
        utilityAccount.addUtilityPayment(expectedUtilityPayment);
        session.save(account);
        session.save(utilityAccount);
        session.clear();

        var actualUtilityPayment = session.get(UtilityPayment.class, expectedUtilityPayment.getId());

        assertThat(actualUtilityPayment.getId()).isNotNull();
    }

    @Test
    void checkUtilityPaymentUpdate() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var transaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(transaction);
        var utilityAccount = getUtilityAccount("112345654356", "Telus");
        var expectedUtilityPayment = getUtilityPayment(
                utilityAccount.getNumber(), utilityAccount, transaction);
        utilityAccount.addUtilityPayment(expectedUtilityPayment);
        session.save(account);
        session.save(utilityAccount);
        session.clear();
        expectedUtilityPayment.setStatus(TransactionStatus.PROCESSING);
        session.update(expectedUtilityPayment);
        session.flush();
        session.clear();

        var actualUtilityPayment = session.get(UtilityPayment.class, expectedUtilityPayment.getId());

        assertThat(actualUtilityPayment.getStatus()).isEqualTo(TransactionStatus.PROCESSING);
    }

    @Test
    void checkUtilityPaymentDelete() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var transaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(transaction);
        var utilityAccount = getUtilityAccount("112345654356", "Telus");
        var expectedUtilityPayment = getUtilityPayment(
                utilityAccount.getNumber(), utilityAccount, transaction);
        utilityAccount.addUtilityPayment(expectedUtilityPayment);
        session.save(account);
        session.save(utilityAccount);
        session.clear();
        session.delete(expectedUtilityPayment);
        session.flush();
        session.clear();

        var actualUtilityPayment = session.get(UtilityPayment.class, expectedUtilityPayment.getId());

        assertThat(actualUtilityPayment).isNull();
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

    private static Transaction getTransaction(String transactionId, BankAccount bankAccount) {
        return Transaction.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .transactionType(TransactionType.DEPOSIT)
                .referenceNumber("123453")
                .transactionId(transactionId)
                .bankAccount(bankAccount)
                .build();
    }

    private static UtilityAccount getUtilityAccount(String number, String provider) {
        return UtilityAccount.builder()
                .number(number)
                .providerName(provider)
                .build();
    }

    private static UtilityPayment getUtilityPayment(String number, UtilityAccount utilityAccount, Transaction transaction) {
        return UtilityPayment.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .referenceNumber(number)
                .status(TransactionStatus.SUCCESS)
                .utilityAccount(utilityAccount)
                .transaction(transaction)
                .build();
    }
}
