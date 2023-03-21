package com.artem.dao;

import com.artem.mapper.TransactionMapper;
import com.artem.mapper.UtilityPaymentMapper;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.UtilityPaymentCreateDto;
import com.artem.model.dto.UtilityPaymentUpdateDto;
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
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_ONE;
import static com.artem.util.ConstantUtil.UTILITY_ACCOUNT_ID_TWO;
import static com.artem.util.ConstantUtil.UTILITY_PAYMENT_ID_ONE;
import static org.assertj.core.api.Assertions.assertThat;

class UtilityPaymentRepositoryTest extends RepositoryTestBase {

    private final TransactionRepository transactionRepository = context.getBean(TransactionRepository.class);
    private final UtilityPaymentRepository utilityPaymentRepository = context.getBean(UtilityPaymentRepository.class);
    private final UtilityAccountRepository utilityAccountRepository = context.getBean(UtilityAccountRepository.class);
    private final UtilityPaymentMapper utilityPaymentMapper = context.getBean(UtilityPaymentMapper.class);
    private final TransactionMapper transactionMapper = context.getBean(TransactionMapper.class);

    @Test
    void checkAccountSave() {
        var toAccount = utilityAccountRepository.findById(UTILITY_ACCOUNT_ID_TWO);
        var transaction = saveTransaction(toAccount);
        var utilityPaymentCreateDto = getUtilityPaymentCreateDto(toAccount, transaction);
        var utilityPayment = utilityPaymentMapper.mapFrom(utilityPaymentCreateDto);
        var expectedPayment = utilityPaymentRepository.save(utilityPayment);
        session.clear();

        assertThat(utilityPaymentRepository.findById(expectedPayment.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var actualPayment = utilityPaymentRepository.findById(UTILITY_PAYMENT_ID_ONE);

        utilityPaymentRepository.delete(actualPayment.get());

        assertThat(utilityPaymentRepository.findById(actualPayment.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var maybePayment = utilityPaymentRepository.findById(UTILITY_PAYMENT_ID_ONE);
        var transferUpdateDto = getUtilityPaymentUpdateDto();
        var expectedPayment = utilityPaymentMapper.mapFrom(maybePayment.get(), transferUpdateDto);

        utilityPaymentRepository.update(expectedPayment);
        session.clear();
        var actualUtilityPayment = utilityPaymentRepository.findById(expectedPayment.getId()).get();

        assertThat(actualUtilityPayment.getAmount()).isEqualTo(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING));
        assertThat(actualUtilityPayment.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void checkAccountFindById() {
        var toAccount = utilityAccountRepository.findById(UTILITY_ACCOUNT_ID_TWO);
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

    private Transaction saveTransaction(Optional<UtilityAccount> toAccount) {
        var transactionCreateDto = getTransactionCreateDto(toAccount);
        var transaction = transactionMapper.mapFrom(transactionCreateDto);
        return transactionRepository.save(transaction);
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }

    private TransactionCreateDto getTransactionCreateDto(Optional<UtilityAccount> toAccount) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(toAccount.get().getNumber())
                .transactionId(getTransactionId())
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccountId(BANK_ACCOUNT_ID_ONE)
                .build();
    }

    private UtilityPaymentCreateDto getUtilityPaymentCreateDto(Optional<UtilityAccount> toAccount, Transaction transaction) {
        return UtilityPaymentCreateDto.builder()
                .account(toAccount.get().getNumber())
                .toUtilityAccountId(UTILITY_ACCOUNT_ID_TWO)
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
