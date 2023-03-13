package com.artem.util;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.FundTransfer;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.User;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.entity.UtilityPayment;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import com.artem.model.type.UserStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();

        var user1 = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        var user2 = getUser("Artem", "Artemov", "artem@gmail.com");
        var user3 = getUser("Petr", "Ivanov", "petr@gmail.com");
        var user4 = getUser("John", "Liskov", "john@gmail.com");
        var user5 = getUser("Mike", "Dunk", "mike@gmail.com");

        session.save(user1);
        session.save(user2);
        session.save(user3);
        session.save(user4);
        session.save(user5);

        var account1 = getAccount(user1);
        var account2 = getAccount(user2);
        var account3 = getAccount(user3);
        var account4 = getAccount(user4);
        var account5 = getAccount(user5);

        var bankAccount1 = getCheckingBankAccount(account1, "1234567890");
        var bankAccount2 = getSavingBankAccount(account1, "2345678901");
        var bankAccount3 = getLoanBankAccount(account1, "3456789012");
        var bankAccount4 = getCheckingBankAccount(account2, "4567890123");
        var bankAccount5 = getSavingBankAccount(account2, "5678901234");
        var bankAccount6 = getLoanBankAccount(account2, "67890123456");
        var bankAccount7 = getCheckingBankAccount(account3, "7890123456");
        var bankAccount8 = getSavingBankAccount(account3, "8901234567");
        var bankAccount9 = getLoanBankAccount(account3, "9012345678");
        var bankAccount10 = getFixedBankAccount(account3, "0123456789");
        var bankAccount11 = getCheckingBankAccount(account4, "1134567890");
        var bankAccount12 = getSavingBankAccount(account4, "1224567890");
        var bankAccount13 = getCheckingBankAccount(account5, "1233567890");
        var bankAccount14 = getSavingBankAccount(account5, "1234467890");
        var bankAccount15 = getFixedBankAccount(account5, "1234557890");

        addBankAccount(account1, bankAccount1, bankAccount2, bankAccount3);
        addBankAccount(account2, bankAccount4, bankAccount5, bankAccount6);
        addBankAccount(account3, bankAccount7, bankAccount8, bankAccount9, bankAccount10);
        addBankAccount(account4, bankAccount11, bankAccount12);
        addBankAccount(account5, bankAccount13, bankAccount14, bankAccount15);

        var utilityAccount1 = getUtilityAccount("12345", "Koodo");
        var utilityAccount2 = getUtilityAccount("12346", "Telus");
        var utilityAccount3 = getUtilityAccount("12347", "Hydro");

        var transaction1 = getTransaction(getTransactionId(), bankAccount1, utilityAccount1.getNumber());
        var transaction2 = getTransaction(getTransactionId(), bankAccount1, bankAccount2.getNumber());
        var transaction3 = getTransaction(getTransactionId(), bankAccount1, bankAccount3.getNumber());
        var transaction4 = getTransaction(getTransactionId(), bankAccount1, bankAccount1.getNumber());
        var transaction5 = getTransaction(getTransactionId(), bankAccount1, bankAccount2.getNumber());
        var transaction6 = getTransaction(getTransactionId(), bankAccount1, utilityAccount1.getNumber());
        var transaction7 = getTransaction(getTransactionId(), bankAccount2, bankAccount3.getNumber());
        var transaction8 = getTransaction(getTransactionId(), bankAccount2, bankAccount1.getNumber());
        var transaction9 = getTransaction(getTransactionId(), bankAccount2, bankAccount3.getNumber());
        var transaction10 = getTransaction(getTransactionId(), bankAccount2, bankAccount1.getNumber());
        var transaction11 = getTransaction(getTransactionId(), bankAccount2, utilityAccount1.getNumber());
        var transaction12 = getTransaction(getTransactionId(), bankAccount3, bankAccount2.getNumber());
        var transaction13 = getTransaction(getTransactionId(), bankAccount3, bankAccount2.getNumber());
        var transaction14 = getTransaction(getTransactionId(), bankAccount3, bankAccount1.getNumber());
        var transaction15 = getTransaction(getTransactionId(), bankAccount4, bankAccount5.getNumber());
        var transaction16 = getTransaction(getTransactionId(), bankAccount4, utilityAccount1.getNumber());
        var transaction17 = getTransaction(getTransactionId(), bankAccount4, bankAccount6.getNumber());
        var transaction18 = getTransaction(getTransactionId(), bankAccount5, bankAccount4.getNumber());
        var transaction19 = getTransaction(getTransactionId(), bankAccount5, bankAccount4.getNumber());
        var transaction20 = getTransaction(getTransactionId(), bankAccount5, bankAccount6.getNumber());
        var transaction21 = getTransaction(getTransactionId(), bankAccount6, utilityAccount2.getNumber());
        var transaction22 = getTransaction(getTransactionId(), bankAccount6, bankAccount5.getNumber());
        var transaction23 = getTransaction(getTransactionId(), bankAccount6, bankAccount6.getNumber());
        var transaction24 = getTransaction(getTransactionId(), bankAccount7, bankAccount10.getNumber());
        var transaction25 = getTransaction(getTransactionId(), bankAccount7, bankAccount9.getNumber());
        var transaction26 = getTransaction(getTransactionId(), bankAccount7, utilityAccount2.getNumber());
        var transaction27 = getTransaction(getTransactionId(), bankAccount8, bankAccount10.getNumber());
        var transaction28 = getTransaction(getTransactionId(), bankAccount8, bankAccount7.getNumber());
        var transaction29 = getTransaction(getTransactionId(), bankAccount8, bankAccount9.getNumber());
        var transaction30 = getTransaction(getTransactionId(), bankAccount9, bankAccount10.getNumber());
        var transaction31 = getTransaction(getTransactionId(), bankAccount9, utilityAccount2.getNumber());
        var transaction32 = getTransaction(getTransactionId(), bankAccount9, bankAccount7.getNumber());
        var transaction33 = getTransaction(getTransactionId(), bankAccount10, bankAccount9.getNumber());
        var transaction34 = getTransaction(getTransactionId(), bankAccount10, bankAccount9.getNumber());
        var transaction35 = getTransaction(getTransactionId(), bankAccount10, bankAccount7.getNumber());
        var transaction36 = getTransaction(getTransactionId(), bankAccount10, utilityAccount2.getNumber());
        var transaction37 = getTransaction(getTransactionId(), bankAccount11, bankAccount12.getNumber());
        var transaction38 = getTransaction(getTransactionId(), bankAccount11, bankAccount11.getNumber());
        var transaction39 = getTransaction(getTransactionId(), bankAccount11, bankAccount12.getNumber());
        var transaction40 = getTransaction(getTransactionId(), bankAccount11, bankAccount11.getNumber());
        var transaction41 = getTransaction(getTransactionId(), bankAccount11, utilityAccount3.getNumber());
        var transaction42 = getTransaction(getTransactionId(), bankAccount12, bankAccount12.getNumber());
        var transaction43 = getTransaction(getTransactionId(), bankAccount12, bankAccount11.getNumber());
        var transaction44 = getTransaction(getTransactionId(), bankAccount13, bankAccount15.getNumber());
        var transaction45 = getTransaction(getTransactionId(), bankAccount13, bankAccount13.getNumber());
        var transaction46 = getTransaction(getTransactionId(), bankAccount13, utilityAccount3.getNumber());
        var transaction47 = getTransaction(getTransactionId(), bankAccount13, bankAccount14.getNumber());
        var transaction48 = getTransaction(getTransactionId(), bankAccount14, bankAccount14.getNumber());
        var transaction49 = getTransaction(getTransactionId(), bankAccount14, bankAccount15.getNumber());
        var transaction50 = getTransaction(getTransactionId(), bankAccount14, bankAccount13.getNumber());
        var transaction51 = getTransaction(getTransactionId(), bankAccount15, utilityAccount3.getNumber());
        var transaction52 = getTransaction(getTransactionId(), bankAccount15, bankAccount15.getNumber());
        var transaction53 = getTransaction(getTransactionId(), bankAccount15, utilityAccount3.getNumber());

        addTransaction(bankAccount1, transaction1, transaction2, transaction3, transaction4, transaction5, transaction6);
        addTransaction(bankAccount2, transaction7, transaction8, transaction9, transaction10, transaction11);
        addTransaction(bankAccount3, transaction12, transaction13, transaction14);
        addTransaction(bankAccount4, transaction15, transaction16, transaction17);
        addTransaction(bankAccount5, transaction18, transaction19, transaction20);
        addTransaction(bankAccount6, transaction21, transaction22, transaction23);
        addTransaction(bankAccount7, transaction24, transaction25, transaction26);
        addTransaction(bankAccount8, transaction27, transaction28, transaction29);
        addTransaction(bankAccount9, transaction30, transaction31, transaction32);
        addTransaction(bankAccount10, transaction33, transaction34, transaction35, transaction36);
        addTransaction(bankAccount11, transaction37, transaction38, transaction39, transaction40, transaction41);
        addTransaction(bankAccount12, transaction42, transaction43);
        addTransaction(bankAccount13, transaction44, transaction45, transaction46, transaction47);
        addTransaction(bankAccount14, transaction48, transaction49, transaction50);
        addTransaction(bankAccount15, transaction51, transaction52, transaction53);

        session.save(account1);
        session.save(account2);
        session.save(account3);
        session.save(account4);
        session.save(account5);


        var utilityPayment1 = getUtilityPayment(utilityAccount1.getNumber(), utilityAccount1, transaction1);
        var utilityPayment2 = getUtilityPayment(utilityAccount1.getNumber(), utilityAccount1, transaction6);
        var utilityPayment3 = getUtilityPayment(utilityAccount1.getNumber(), utilityAccount1, transaction11);
        var utilityPayment4 = getUtilityPayment(utilityAccount1.getNumber(), utilityAccount1, transaction16);
        var utilityPayment5 = getUtilityPayment(utilityAccount2.getNumber(), utilityAccount2, transaction21);
        var utilityPayment6 = getUtilityPayment(utilityAccount2.getNumber(), utilityAccount2, transaction26);
        var utilityPayment7 = getUtilityPayment(utilityAccount2.getNumber(), utilityAccount2, transaction31);
        var utilityPayment8 = getUtilityPayment(utilityAccount2.getNumber(), utilityAccount2, transaction36);
        var utilityPayment9 = getUtilityPayment(utilityAccount3.getNumber(), utilityAccount3, transaction41);
        var utilityPayment10 = getUtilityPayment(utilityAccount3.getNumber(), utilityAccount3, transaction46);
        var utilityPayment11 = getUtilityPayment(utilityAccount3.getNumber(), utilityAccount3, transaction51);
        var utilityPayment12 = getUtilityPayment(utilityAccount3.getNumber(), utilityAccount3, transaction53);

        addUtilityPayment(utilityAccount1, utilityPayment1, utilityPayment2, utilityPayment3, utilityPayment4);
        addUtilityPayment(utilityAccount2, utilityPayment5, utilityPayment6, utilityPayment7, utilityPayment8);
        addUtilityPayment(utilityAccount3, utilityPayment9, utilityPayment10, utilityPayment11, utilityPayment12);

        session.save(utilityAccount1);
        session.save(utilityAccount2);
        session.save(utilityAccount3);

        var fundTransfer1 = getFundTransfer(bankAccount1.getNumber(), bankAccount2.getNumber(), transaction2);
        var fundTransfer2 = getFundTransfer(bankAccount2.getNumber(), bankAccount3.getNumber(), transaction3);
        var fundTransfer3 = getFundTransfer(bankAccount3.getNumber(), bankAccount1.getNumber(), transaction4);
        var fundTransfer4 = getFundTransfer(bankAccount1.getNumber(), bankAccount2.getNumber(), transaction5);
        var fundTransfer5 = getFundTransfer(bankAccount2.getNumber(), bankAccount3.getNumber(), transaction7);
        var fundTransfer6 = getFundTransfer(bankAccount3.getNumber(), bankAccount1.getNumber(), transaction8);
        var fundTransfer7 = getFundTransfer(bankAccount1.getNumber(), bankAccount3.getNumber(), transaction9);
        var fundTransfer8 = getFundTransfer(bankAccount2.getNumber(), bankAccount1.getNumber(), transaction10);
        var fundTransfer9 = getFundTransfer(bankAccount3.getNumber(), bankAccount2.getNumber(), transaction12);
        var fundTransfer10 = getFundTransfer(bankAccount1.getNumber(), bankAccount2.getNumber(), transaction13);
        var fundTransfer11 = getFundTransfer(bankAccount2.getNumber(), bankAccount1.getNumber(), transaction14);

        var fundTransfer12 = getFundTransfer(bankAccount4.getNumber(), bankAccount5.getNumber(), transaction15);
        var fundTransfer13 = getFundTransfer(bankAccount5.getNumber(), bankAccount6.getNumber(), transaction17);
        var fundTransfer14 = getFundTransfer(bankAccount6.getNumber(), bankAccount4.getNumber(), transaction18);
        var fundTransfer15 = getFundTransfer(bankAccount5.getNumber(), bankAccount4.getNumber(), transaction19);
        var fundTransfer16 = getFundTransfer(bankAccount4.getNumber(), bankAccount6.getNumber(), transaction20);
        var fundTransfer17 = getFundTransfer(bankAccount6.getNumber(), bankAccount5.getNumber(), transaction22);
        var fundTransfer18 = getFundTransfer(bankAccount5.getNumber(), bankAccount6.getNumber(), transaction23);

        var fundTransfer19 = getFundTransfer(bankAccount7.getNumber(), bankAccount10.getNumber(), transaction24);
        var fundTransfer20 = getFundTransfer(bankAccount8.getNumber(), bankAccount9.getNumber(), transaction25);
        var fundTransfer21 = getFundTransfer(bankAccount9.getNumber(), bankAccount10.getNumber(), transaction27);
        var fundTransfer22 = getFundTransfer(bankAccount10.getNumber(), bankAccount7.getNumber(), transaction28);
        var fundTransfer23 = getFundTransfer(bankAccount7.getNumber(), bankAccount9.getNumber(), transaction29);
        var fundTransfer24 = getFundTransfer(bankAccount8.getNumber(), bankAccount10.getNumber(), transaction30);
        var fundTransfer25 = getFundTransfer(bankAccount9.getNumber(), bankAccount7.getNumber(), transaction32);
        var fundTransfer26 = getFundTransfer(bankAccount10.getNumber(), bankAccount9.getNumber(), transaction33);
        var fundTransfer27 = getFundTransfer(bankAccount7.getNumber(), bankAccount9.getNumber(), transaction34);
        var fundTransfer28 = getFundTransfer(bankAccount8.getNumber(), bankAccount7.getNumber(), transaction35);

        var fundTransfer29 = getFundTransfer(bankAccount11.getNumber(), bankAccount12.getNumber(), transaction37);
        var fundTransfer30 = getFundTransfer(bankAccount12.getNumber(), bankAccount11.getNumber(), transaction38);
        var fundTransfer31 = getFundTransfer(bankAccount11.getNumber(), bankAccount12.getNumber(), transaction39);
        var fundTransfer32 = getFundTransfer(bankAccount12.getNumber(), bankAccount11.getNumber(), transaction40);
        var fundTransfer33 = getFundTransfer(bankAccount11.getNumber(), bankAccount12.getNumber(), transaction42);
        var fundTransfer34 = getFundTransfer(bankAccount12.getNumber(), bankAccount11.getNumber(), transaction43);

        var fundTransfer35 = getFundTransfer(bankAccount13.getNumber(), bankAccount15.getNumber(), transaction44);
        var fundTransfer36 = getFundTransfer(bankAccount14.getNumber(), bankAccount13.getNumber(), transaction45);
        var fundTransfer37 = getFundTransfer(bankAccount15.getNumber(), bankAccount14.getNumber(), transaction47);
        var fundTransfer38 = getFundTransfer(bankAccount13.getNumber(), bankAccount14.getNumber(), transaction48);
        var fundTransfer39 = getFundTransfer(bankAccount14.getNumber(), bankAccount15.getNumber(), transaction49);
        var fundTransfer40 = getFundTransfer(bankAccount15.getNumber(), bankAccount13.getNumber(), transaction50);
        var fundTransfer41 = getFundTransfer(bankAccount13.getNumber(), bankAccount15.getNumber(), transaction52);

        session.save(fundTransfer1);
        session.save(fundTransfer2);
        session.save(fundTransfer3);
        session.save(fundTransfer4);
        session.save(fundTransfer5);
        session.save(fundTransfer6);
        session.save(fundTransfer7);
        session.save(fundTransfer8);
        session.save(fundTransfer9);
        session.save(fundTransfer10);
        session.save(fundTransfer11);
        session.save(fundTransfer12);
        session.save(fundTransfer13);
        session.save(fundTransfer14);
        session.save(fundTransfer15);
        session.save(fundTransfer16);
        session.save(fundTransfer17);
        session.save(fundTransfer18);
        session.save(fundTransfer19);
        session.save(fundTransfer20);
        session.save(fundTransfer21);
        session.save(fundTransfer22);
        session.save(fundTransfer23);
        session.save(fundTransfer24);
        session.save(fundTransfer25);
        session.save(fundTransfer26);
        session.save(fundTransfer27);
        session.save(fundTransfer28);
        session.save(fundTransfer29);
        session.save(fundTransfer30);
        session.save(fundTransfer31);
        session.save(fundTransfer32);
        session.save(fundTransfer33);
        session.save(fundTransfer34);
        session.save(fundTransfer35);
        session.save(fundTransfer36);
        session.save(fundTransfer37);
        session.save(fundTransfer38);
        session.save(fundTransfer39);
        session.save(fundTransfer40);
        session.save(fundTransfer41);

    }


    private User getUser(String firstname, String lastname, String email) {
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

    private Account getAccount(User user) {
        return Account.builder()
                .user(user)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(user.getEmail())
                .build();
    }

    private BankAccount getCheckingBankAccount(Account account, String number) {
        return BankAccount.builder()
                .account(account)
                .number(number)
                .type(AccountType.CHECKING_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(100000).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100100).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount getSavingBankAccount(Account account, String number) {
        return BankAccount.builder()
                .account(account)
                .number(number)
                .type(AccountType.SAVINGS_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(100000).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100100).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount getLoanBankAccount(Account account, String number) {
        return BankAccount.builder()
                .account(account)
                .number(number)
                .type(AccountType.LOAN_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(100000).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100100).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount getFixedBankAccount(Account account, String number) {
        return BankAccount.builder()
                .account(account)
                .number(number)
                .type(AccountType.FIXED_DEPOSIT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(100000).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100100).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private Transaction getTransaction(String transactionId, BankAccount bankAccount, String referenceNumber) {
        return Transaction.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .transactionType(TransactionType.DEPOSIT)
                .referenceNumber(referenceNumber)
                .transactionId(transactionId)
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccount(bankAccount)
                .build();
    }

    private FundTransfer getFundTransfer(String fromAccount, String toAccount, Transaction transaction) {
        return FundTransfer.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .status(TransactionStatus.SUCCESS)
                .transaction(transaction.getTransactionId())
                .build();
    }

    private UtilityAccount getUtilityAccount(String number, String provider) {
        return UtilityAccount.builder()
                .number(number)
                .providerName(provider)
                .build();
    }

    private UtilityPayment getUtilityPayment(String number, UtilityAccount utilityAccount, Transaction transaction) {
        return UtilityPayment.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .referenceNumber(number)
                .status(TransactionStatus.SUCCESS)
                .utilityAccount(utilityAccount)
                .transaction(transaction.getTransactionId())
                .build();
    }

    private void addBankAccount(Account account, BankAccount... bankAccounts) {
        Arrays.stream(bankAccounts).forEach(account::addBankAccount);
    }

    private void addTransaction(BankAccount bankAccount, Transaction... transactions) {
        Arrays.stream(transactions).forEach(bankAccount::addTransaction);
    }

    private void addUtilityPayment(UtilityAccount utilityAccount, UtilityPayment... utilityPayments) {
        Arrays.stream(utilityPayments).forEach(utilityAccount::addUtilityPayment);
    }


    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }
}
