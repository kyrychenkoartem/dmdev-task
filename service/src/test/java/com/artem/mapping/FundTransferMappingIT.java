package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.FundTransfer;
import com.artem.model.entity.TransactionEntity;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionStatus;
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

public class FundTransferMappingIT {

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
    void checkFundTransferGet() {
        var user = getUser("Glen", "Johnson", "glen@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var fromAccount = getBankAccount(account, "1234567865456765675673");
        account.addBankAccount(fromAccount);
        var toAccount = getBankAccount(account, "1234565435676543567654");
        account.addBankAccount(toAccount);
        var transaction = getTransaction("45678654567", fromAccount);
        fromAccount.addTransaction(transaction);
        session.save(account);
        var expectedFundTransfer = getFundTransfer(fromAccount.getNumber(), toAccount.getNumber(), transaction);
        session.save(expectedFundTransfer);
        session.clear();

        var actualFundTransfer = session.get(FundTransfer.class, expectedFundTransfer.getId());

        assertThat(actualFundTransfer).isEqualTo(expectedFundTransfer);
    }

    @Test
    void checkFundTransferInsert() {
        var user = getUser("Glen", "Johnson", "glen@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var fromAccount = getBankAccount(account, "1234567865456765675673");
        account.addBankAccount(fromAccount);
        var toAccount = getBankAccount(account, "1234565435676543567654");
        account.addBankAccount(toAccount);
        var transaction = getTransaction("45678654567", fromAccount);
        fromAccount.addTransaction(transaction);
        session.save(account);
        var expectedFundTransfer = getFundTransfer(fromAccount.getNumber(), toAccount.getNumber(), transaction);
        session.save(expectedFundTransfer);
        session.clear();

        var actualFundTransfer = session.get(FundTransfer.class, expectedFundTransfer.getId());

        assertThat(actualFundTransfer.getId()).isNotNull();
    }

    @Test
    void checkFundTransferUpdate() {
        var user = getUser("Glen", "Johnson", "glen@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var fromAccount = getBankAccount(account, "1234567865456765675673");
        account.addBankAccount(fromAccount);
        var toAccount = getBankAccount(account, "1234565435676543567654");
        account.addBankAccount(toAccount);
        var transaction = getTransaction("45678654567", fromAccount);
        fromAccount.addTransaction(transaction);
        session.save(account);
        var expectedFundTransfer = getFundTransfer(fromAccount.getNumber(), toAccount.getNumber(), transaction);
        session.save(expectedFundTransfer);
        session.clear();
        expectedFundTransfer.setStatus(TransactionStatus.PROCESSING);
        session.update(expectedFundTransfer);
        session.flush();
        session.clear();

        var actualFundTransfer = session.get(FundTransfer.class, expectedFundTransfer.getId());

        assertThat(actualFundTransfer.getStatus()).isEqualTo(TransactionStatus.PROCESSING);
    }

    @Test
    void checkFundTransferDelete() {
        var user = getUser("Glen", "Johnson", "glen@gmail.com");
        session.save(user);
        var account = getAccount(user);
        var fromAccount = getBankAccount(account, "1234567865456765675673");
        account.addBankAccount(fromAccount);
        var toAccount = getBankAccount(account, "1234565435676543567654");
        account.addBankAccount(toAccount);
        var transaction = getTransaction("45678654567", fromAccount);
        fromAccount.addTransaction(transaction);
        session.save(account);
        var expectedFundTransfer = getFundTransfer(fromAccount.getNumber(), toAccount.getNumber(), transaction);
        session.save(expectedFundTransfer);
        session.clear();
        session.delete(expectedFundTransfer);
        session.flush();
        session.clear();

        var actualFundTransfer = session.get(FundTransfer.class, expectedFundTransfer.getId());

        assertThat(actualFundTransfer).isNull();
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

    private static TransactionEntity getTransaction(String transactionId, BankAccount bankAccount) {
        return TransactionEntity.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .transactionType(TransactionType.DEPOSIT)
                .referenceNumber("123453")
                .transactionId(transactionId)
                .bankAccount(bankAccount)
                .build();
    }

    private static FundTransfer getFundTransfer(String fromAccount, String toAccount, TransactionEntity transaction) {
        return FundTransfer.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .status(TransactionStatus.SUCCESS)
                .transaction(transaction)
                .build();
    }
}
