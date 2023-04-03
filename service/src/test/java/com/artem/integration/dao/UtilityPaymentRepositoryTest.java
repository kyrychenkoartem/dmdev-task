package com.artem.integration.dao;

import com.artem.dao.AccountRepository;
import com.artem.dao.BankAccountRepository;
import com.artem.dao.TransactionRepository;
import com.artem.dao.UserRepository;
import com.artem.dao.UtilityAccountRepository;
import com.artem.dao.UtilityPaymentRepository;
import com.artem.mapper.AccountMapper;
import com.artem.mapper.BankAccountMapper;
import com.artem.mapper.TransactionMapper;
import com.artem.mapper.UserMapper;
import com.artem.mapper.UtilityAccountMapper;
import com.artem.mapper.UtilityPaymentMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityPaymentCreateDto;
import com.artem.model.dto.UtilityPaymentUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import com.artem.util.DateTimeGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_UTILITY_PAYMENTS;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UtilityPaymentRepositoryTest extends RepositoryTestBase {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final UtilityPaymentRepository utilityPaymentRepository;
    private final AccountRepository accountRepository;
    private final UtilityAccountRepository utilityAccountRepository;
    private final AccountMapper accountMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;
    private final UtilityPaymentMapper utilityPaymentMapper;
    private final UtilityAccountMapper utilityAccountMapper;
    private final EntityManager session;

    @Test
    void checkAccountSave() {
        var toAccount = getUtilityAccount();
        var transaction = saveTransaction(toAccount);
        var utilityPaymentCreateDto = getUtilityPaymentCreateDto(toAccount, transaction);
        var utilityPayment = utilityPaymentMapper.mapFrom(utilityPaymentCreateDto);
        var expectedPayment = utilityPaymentRepository.save(utilityPayment);
        session.clear();

        assertThat(utilityPaymentRepository.findById(expectedPayment.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var toAccount = getUtilityAccount();
        var transaction = saveTransaction(toAccount);
        var utilityPaymentCreateDto = getUtilityPaymentCreateDto(toAccount, transaction);
        var utilityPayment = utilityPaymentMapper.mapFrom(utilityPaymentCreateDto);
        var expectedPayment = utilityPaymentRepository.save(utilityPayment);
        session.clear();
        var actualPayment = utilityPaymentRepository.findById(expectedPayment.getId());

        utilityPaymentRepository.delete(actualPayment.get());

        assertThat(utilityPaymentRepository.findById(actualPayment.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var toAccount = getUtilityAccount();
        var transaction = saveTransaction(toAccount);
        var utilityPaymentCreateDto = getUtilityPaymentCreateDto(toAccount, transaction);
        var utilityPaymentToSave = utilityPaymentMapper.mapFrom(utilityPaymentCreateDto);
        var payment = utilityPaymentRepository.save(utilityPaymentToSave);
        session.clear();
        var maybePayment = utilityPaymentRepository.findById(payment.getId());
        var transferUpdateDto = getUtilityPaymentUpdateDto();
        var expectedPayment = utilityPaymentMapper.mapFrom(maybePayment.get(), transferUpdateDto);

        utilityPaymentRepository.saveAndFlush(expectedPayment);
        session.clear();
        var actualUtilityPayment = utilityPaymentRepository.findById(expectedPayment.getId()).get();

        assertThat(actualUtilityPayment.getAmount()).isEqualTo(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING));
        assertThat(actualUtilityPayment.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void checkAccountFindById() {
        var toAccount = getUtilityAccount();
        var transaction = saveTransaction(toAccount);
        var utilityPaymentCreateDto = getUtilityPaymentCreateDto(toAccount, transaction);
        var utilityPayment = utilityPaymentMapper.mapFrom(utilityPaymentCreateDto);
        var expectedPayment = utilityPaymentRepository.save(utilityPayment);
        session.clear();

        var actualPayment = utilityPaymentRepository.findById(expectedPayment.getId());

        assertThat(expectedPayment).isEqualTo(actualPayment.get());
    }

    @Test
    void checkFindAllAccounts() {
        var fundTransferList = utilityPaymentRepository.findAll();

        assertThat(fundTransferList.size()).isEqualTo(ALL_UTILITY_PAYMENTS);
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
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

    private UtilityAccount getUtilityAccount() {
        var utilityAccountCreateDto = UtilityAccountCreateDto.builder()
                .number("1")
                .providerName("Giga")
                .build();
        var utilityAccount = utilityAccountMapper.mapFrom(utilityAccountCreateDto);
        return utilityAccountRepository.save(utilityAccount);
    }

    private Transaction saveTransaction(UtilityAccount utilityAccount) {
        var bankAccount = saveBankAccount();
        var transactionCreateDto = getTransactionCreateDto(utilityAccount, bankAccount);
        var transaction = transactionMapper.mapFrom(transactionCreateDto);
        return transactionRepository.save(transaction);
    }

    private TransactionCreateDto getTransactionCreateDto(UtilityAccount toAccount, BankAccount fromAccount) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(toAccount.getNumber())
                .transactionId(getTransactionId())
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccountId(fromAccount.getId())
                .build();
    }

    private UtilityPaymentCreateDto getUtilityPaymentCreateDto(UtilityAccount toAccount, Transaction transaction) {
        return UtilityPaymentCreateDto.builder()
                .account(toAccount.getNumber())
                .toUtilityAccountId(toAccount.getId())
                .amount(transaction.getAmount())
                .status(TransactionStatus.PROCESSING)
                .transactionId(transaction.getTransactionId())
                .build();
    }

    private UtilityPaymentUpdateDto getUtilityPaymentUpdateDto() {
        return UtilityPaymentUpdateDto.builder()
                .amount(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING))
                .status(TransactionStatus.SUCCESS)
                .build();
    }
}
