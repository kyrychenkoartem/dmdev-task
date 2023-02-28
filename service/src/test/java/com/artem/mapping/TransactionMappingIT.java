package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionType;
import com.artem.util.HibernateTestUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionMappingIT {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void close() {
        sessionFactory.close();
    }

    @Test
    void checkTransactionGet() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var expectedTransaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(expectedTransaction);
        session.save(account);
        session.clear();

        var actualTransaction = session.get(Transaction.class, expectedTransaction.getId());

        assertThat(actualTransaction).isEqualTo(expectedTransaction);
    }

    @Test
    void checkTransactionInsert() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var expectedTransaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(expectedTransaction);
        session.save(account);
        session.clear();

        var actualTransaction = session.get(Transaction.class, expectedTransaction.getId());

        assertThat(actualTransaction.getId()).isNotNull();
    }

    @Test
    void checkTransactionUpdate() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var expectedTransaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(expectedTransaction);
        session.save(account);
        session.clear();
        expectedTransaction.setTransactionType(TransactionType.REFUND);
        session.update(expectedTransaction);
        session.flush();
        session.clear();

        var actualTransaction = session.get(Transaction.class, expectedTransaction.getId());

        assertThat(actualTransaction.getTransactionType()).isEqualTo(TransactionType.REFUND);
    }

    @Test
    void checkTransactionDelete() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var bankAccount = getBankAccount(account, "123456786543");
        account.addBankAccount(bankAccount);
        var expectedTransaction = getTransaction("1234567890098765432", bankAccount);
        bankAccount.addTransaction(expectedTransaction);
        session.save(account);
        session.clear();
        session.delete(expectedTransaction);
        session.flush();
        session.clear();

        var actualTransaction = session.get(Transaction.class, expectedTransaction.getId());

        assertThat(actualTransaction).isNull();
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
}
