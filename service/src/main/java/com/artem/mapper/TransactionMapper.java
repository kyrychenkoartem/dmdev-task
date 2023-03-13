package com.artem.mapper;

import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.model.entity.Transaction;

public class TransactionMapper implements Mapper<TransactionCreateDto, Transaction> {
    @Override
    public Transaction mapFrom(TransactionCreateDto createDto) {
        return Transaction.builder()
                .amount(createDto.amount())
                .transactionType(createDto.type())
                .referenceNumber(createDto.referenceNumber())
                .transactionId(createDto.transactionId())
                .time(createDto.time())
                .bankAccount(createDto.bankAccount())
                .build();
    }

    public Transaction mapFrom(Transaction transaction, TransactionUpdateDto updateDto) {
        transaction.setAmount(updateDto.amount());
        transaction.setTransactionType(updateDto.type());
        return transaction;
    }
}
