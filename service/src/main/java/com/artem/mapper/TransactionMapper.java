package com.artem.mapper;

import com.artem.model.dto.TransactionReadDto;
import com.artem.model.type.TransactionStatus;
import com.artem.repository.BankAccountRepository;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.model.entity.Transaction;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper implements Mapper<TransactionCreateDto, Transaction> {

    private final BankAccountRepository bankAccountRepository;

    @Override
    public Transaction mapFrom(TransactionCreateDto createDto) {
        return Transaction.builder()
                .amount(createDto.amount())
                .transactionType(createDto.type())
                .status(TransactionStatus.SUCCESS)
                .referenceNumber(createDto.referenceNumber())
                .transactionId(createDto.transactionId())
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .bankAccount(bankAccountRepository.findById(createDto.bankAccountId()).get())
                .build();
    }

    public TransactionReadDto mapFrom(Transaction transaction) {
        return TransactionReadDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getTransactionType())
                .status(transaction.getStatus())
                .referenceNumber(transaction.getReferenceNumber())
                .transactionId(transaction.getTransactionId())
                .time(transaction.getTime())
                .bankAccountId(transaction.getBankAccount().getId())
                .build();
    }

    public List<TransactionReadDto> mapFrom(List<Transaction> transactions) {
        return transactions.stream()
                .map(it -> TransactionReadDto.builder()
                        .id(it.getId())
                        .amount(it.getAmount())
                        .type(it.getTransactionType())
                        .status(it.getStatus())
                        .referenceNumber(it.getReferenceNumber())
                        .transactionId(it.getTransactionId())
                        .time(it.getTime())
                        .bankAccountId(it.getBankAccount().getId())
                        .build())
                .toList();
    }

    public Transaction mapFrom(Transaction transaction, TransactionUpdateDto updateDto) {
        transaction.setAmount(updateDto.amount());
        transaction.setTransactionType(updateDto.type());
        transaction.setStatus(updateDto.status());
        return transaction;
    }
}
