package com.artem.dao;

import com.artem.mapper.TransactionMapper;
import com.artem.mapper.UtilityPaymentMapper;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.UtilityPaymentCreateDto;
import com.artem.model.dto.UtilityPaymentUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import com.artem.util.DateTimeGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_UTILITY_PAYMENTS;
import static org.assertj.core.api.Assertions.assertThat;


public class UtilityPaymentRepositoryTestIT extends RepositoryTestBase {

    private final TransactionRepository transactionRepository = new TransactionRepository(session);
    private final UtilityPaymentRepository utilityPaymentRepository = new UtilityPaymentRepository(session);
    private final BankAccountRepository bankAccountRepository = new BankAccountRepository(session);
    private final UtilityAccountRepository utilityAccountRepository = new UtilityAccountRepository(session);
    private final UtilityPaymentMapper utilityPaymentMapper = new UtilityPaymentMapper();
    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    void checkAccountSave() {
        var fromAccount = bankAccountRepository.findById(1L);
        var toAccount = utilityAccountRepository.findById(2L);
        var transaction = saveTransaction(fromAccount, toAccount);
        var utilityPaymentCreateDto = getUtilityPaymentCreateDto(toAccount, transaction);
        var utilityPayment = utilityPaymentMapper.mapFrom(utilityPaymentCreateDto);
        var expectedPayment = utilityPaymentRepository.save(utilityPayment);

        assertThat(expectedPayment.getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var actualPayment = utilityPaymentRepository.findById(1L);

        utilityPaymentRepository.delete(actualPayment.get());

        assertThat(utilityPaymentRepository.findById(actualPayment.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var actualPayment = utilityPaymentRepository.findById(1L);
        var transferUpdateDto = getUtilityPaymentUpdateDto();
        var expectedPayment = utilityPaymentMapper.mapFrom(actualPayment.get(), transferUpdateDto);

        utilityPaymentRepository.update(expectedPayment);
        session.clear();

        assertThat(utilityPaymentRepository.findById(expectedPayment.getId()).get().getAmount())
                .isEqualTo(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING));
        assertThat(utilityPaymentRepository.findById(expectedPayment.getId()).get().getStatus())
                .isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void checkAccountFindById() {
        var fromAccount = bankAccountRepository.findById(1L);
        var toAccount = utilityAccountRepository.findById(2L);
        var transaction = saveTransaction(fromAccount, toAccount);
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

    private Transaction saveTransaction(Optional<BankAccount> fromAccount, Optional<UtilityAccount> toAccount) {
        var transactionCreateDto = getTransactionCreateDto(fromAccount, toAccount);
        var transaction = transactionMapper.mapFrom(transactionCreateDto);
        return transactionRepository.save(transaction);
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }

    private TransactionCreateDto getTransactionCreateDto(Optional<BankAccount> fromAccount, Optional<UtilityAccount> toAccount) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(toAccount.get().getNumber())
                .transactionId(getTransactionId())
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccount(fromAccount.get())
                .build();
    }

    private UtilityPaymentCreateDto getUtilityPaymentCreateDto(Optional<UtilityAccount> toAccount, Transaction transaction) {
        return UtilityPaymentCreateDto.builder()
                .account(toAccount.get().getNumber())
                .toAccount(toAccount.get())
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
