package com.artem.dao;

import com.artem.mapper.FundTransferMapper;
import com.artem.mapper.TransactionMapper;
import com.artem.model.dto.FundTransferCreateDto;
import com.artem.model.dto.FundTransferUpdateDto;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import com.artem.util.DateTimeGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_FUND_TRANSFERS;
import static org.assertj.core.api.Assertions.assertThat;


public class FundTransferRepositoryTestIT extends RepositoryTestBase {

    private final FundTransferRepository fundTransferRepository = new FundTransferRepository(session);
    private final TransactionRepository transactionRepository = new TransactionRepository(session);
    private final BankAccountRepository bankAccountRepository = new BankAccountRepository(session);
    private final FundTransferMapper fundTransferMapper = new FundTransferMapper();
    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    void checkAccountSave() {
        var fromAccount = bankAccountRepository.findById(1L);
        var toAccount = bankAccountRepository.findById(2L);
        var transaction = saveTransaction(fromAccount, toAccount);
        var fundTransferCreateDto = getFundTransferCreateDto(fromAccount, toAccount, transaction);
        var fundTransfer = fundTransferMapper.mapFrom(fundTransferCreateDto);
        var expectedFundTransfer = fundTransferRepository.save(fundTransfer);

        assertThat(expectedFundTransfer.getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var actualFundTransfer = fundTransferRepository.findById(1L);

        fundTransferRepository.delete(actualFundTransfer.get());

        assertThat(fundTransferRepository.findById(actualFundTransfer.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var actualFundTransfer = fundTransferRepository.findById(1L);
        var transferUpdateDto = getTransferUpdateDto();
        var expectedFundTransfer = fundTransferMapper.mapFrom(actualFundTransfer.get(), transferUpdateDto);

        fundTransferRepository.update(expectedFundTransfer);
        session.clear();

        assertThat(fundTransferRepository.findById(expectedFundTransfer.getId()).get().getAmount())
                .isEqualTo(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING));
        assertThat(fundTransferRepository.findById(expectedFundTransfer.getId()).get().getStatus())
                .isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void checkAccountFindById() {
        var fromAccount = bankAccountRepository.findById(1L);
        var toAccount = bankAccountRepository.findById(2L);
        var transaction = saveTransaction(fromAccount, toAccount);
        var fundTransferCreateDto = getFundTransferCreateDto(fromAccount, toAccount, transaction);
        var fundTransfer = fundTransferMapper.mapFrom(fundTransferCreateDto);
        var expectedFundTransfer = fundTransferRepository.save(fundTransfer);
        session.clear();

        var actualUtilityAccount = fundTransferRepository.findById(expectedFundTransfer.getId());

        assertThat(expectedFundTransfer).isEqualTo(actualUtilityAccount.get());
    }

    @Test
    void checkFindAllAccounts() {
        var fundTransferList = fundTransferRepository.findAll();

        assertThat(fundTransferList.size()).isEqualTo(ALL_FUND_TRANSFERS);
    }

    private Transaction saveTransaction(Optional<BankAccount> fromAccount, Optional<BankAccount> toAccount) {
        var transactionCreateDto = getTransactionCreateDto(fromAccount, toAccount);
        var transaction = transactionMapper.mapFrom(transactionCreateDto);
        return transactionRepository.save(transaction);
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }

    private TransactionCreateDto getTransactionCreateDto(Optional<BankAccount> fromAccount, Optional<BankAccount> toAccount) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(toAccount.get().getNumber())
                .transactionId(getTransactionId())
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccount(fromAccount.get())
                .build();
    }

    private FundTransferCreateDto getFundTransferCreateDto(Optional<BankAccount> fromAccount, Optional<BankAccount> toAccount, Transaction transaction) {
        return FundTransferCreateDto.builder()
                .fromAccount(fromAccount.get().getNumber())
                .toAccount(toAccount.get().getNumber())
                .amount(transaction.getAmount())
                .status(TransactionStatus.PROCESSING)
                .transactionId(transaction.getTransactionId())
                .build();
    }

    private FundTransferUpdateDto getTransferUpdateDto() {
        return FundTransferUpdateDto.builder()
                .amount(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING))
                .status(TransactionStatus.SUCCESS)
                .build();
    }
}
