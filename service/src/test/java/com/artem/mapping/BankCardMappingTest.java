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

public class BankCardMappingTest extends MappingTestBase {

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
    void checkBankCardGet() {
        var user = getUser("Artem", "Artemov", "artem@gmail.com");
        session.save(user);
        var account = getAccount(user.getId());
        session.save(account);
        var bankAccount = getBankAccount(account.getId(), "123456789907654567");
        session.save(bankAccount);
        var expectedBankCard = getBankCard(session.get(Account.class, bankAccount.getAccount()).getUser(),
                bankAccount.getId(), "1234565432345");
        session.save(expectedBankCard);
        session.clear();

        var actualBankCard = session.get(BankCard.class, expectedBankCard.getId());

        assertThat(actualBankCard).isEqualTo(expectedBankCard);
    }

    @Test
    void checkBankCardInsert() {
        var user = getUser("John", "John", "john@gmail.com");
        session.save(user);
        var account = getAccount(user.getId());
        session.save(account);
        var bankAccount = getBankAccount(account.getId(), "12345678990765f45454567");
        session.save(bankAccount);
        var expectedBankCard = getBankCard(session.get(Account.class, bankAccount.getAccount()).getUser(),
                bankAccount.getId(), "12345654323465456565");
        session.save(expectedBankCard);
        session.clear();

        var actualBankCard = session.get(BankCard.class, expectedBankCard.getId());

        assertThat(actualBankCard.getId()).isNotNull();
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

    private static BankCard getBankCard(Long userId, Long bankAccountId, String cardNumber) {
        return BankCard.builder()
                .user(userId)
                .bankAccounts(bankAccountId)
                .cardNumber(cardNumber)
                .expiryDate("01/25")
                .bank(BankType.NOVA_SCOTIA_BANK)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .build();
    }
}
