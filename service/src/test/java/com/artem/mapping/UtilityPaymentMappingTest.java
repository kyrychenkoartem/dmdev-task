package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.TransactionEntity;
import com.artem.model.entity.User;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.entity.UtilityPayment;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import com.artem.util.HibernateUtil;
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

public class UtilityPaymentMappingTest extends MappingTestBase {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateUtil.buildSessionFactory();
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
    void checkUtilityPaymentGet() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user.getId());
        session.save(account);
        var bankAccount = getBankAccount(account.getId(), "123456786543");
        session.save(bankAccount);
        var transaction = getTransaction("1234567890098765432", bankAccount.getId());
        session.save(transaction);
        var utilityAccount = getUtilityAccount("112345654356", "Telus");
        session.save(utilityAccount);
        var expectedUtilityPayment = getUtilityPayment(utilityAccount.getNumber(),
                utilityAccount.getId(), transaction.getId());
        session.save(expectedUtilityPayment);
        session.clear();

        var actualUtilityPayment = session.get(UtilityPayment.class, expectedUtilityPayment.getId());

        assertThat(actualUtilityPayment).isEqualTo(expectedUtilityPayment);
    }

    @Test
    void checkUtilityPaymentInsert() {
        var user = getUser("Mike", "Richards", "mike@gmail.com");
        session.save(user);
        var account = getAccount(user.getId());
        session.save(account);
        var bankAccount = getBankAccount(account.getId(), "123456786543");
        session.save(bankAccount);
        var transaction = getTransaction("1234567890098765432", bankAccount.getId());
        session.save(transaction);
        var utilityAccount = getUtilityAccount("112345654356", "Telus");
        session.save(utilityAccount);
        var expectedUtilityPayment = getUtilityPayment(utilityAccount.getNumber(),
                utilityAccount.getId(), transaction.getId());
        session.save(expectedUtilityPayment);
        session.clear();

        var actualUtilityPayment = session.get(UtilityPayment.class, expectedUtilityPayment.getId());

        assertThat(actualUtilityPayment.getId()).isNotNull();
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

    private static Account getAccount(Long userId) {
        return Account.builder()
                .user(userId)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(userId.toString())
                .build();
    }

    private static BankAccount getBankAccount(Long accountId, String number) {
        return BankAccount.builder()
                .account(accountId)
                .number(number)
                .type(AccountType.CHECKING_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private static TransactionEntity getTransaction(String transactionId, Long bankAccountId) {
        return TransactionEntity.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .transactionType(TransactionType.DEPOSIT)
                .referenceNumber("123453")
                .transactionId(transactionId)
                .bankAccount(bankAccountId)
                .build();
    }

    private static UtilityAccount getUtilityAccount(String number, String provider) {
        return UtilityAccount.builder()
                .number(number)
                .providerName(provider)
                .build();
    }

    private static UtilityPayment getUtilityPayment(String number, Long utilityAccountId, Long transactionId) {
        return UtilityPayment.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .referenceNumber(number)
                .status(TransactionStatus.SUCCESS)
                .utilityAccount(utilityAccountId)
                .transaction(transactionId)
                .build();
    }
}
