package com.artem.mapper;

import com.artem.dao.BankAccountRepository;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.model.entity.Transaction;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionMapper implements Mapper<TransactionCreateDto, Transaction> {

    private final BankAccountRepository bankAccountRepository;
    @Override
    public Transaction mapFrom(TransactionCreateDto createDto) {
        return Transaction.builder()
                .amount(createDto.amount())
                .transactionType(createDto.type())
                .referenceNumber(createDto.referenceNumber())
                .transactionId(createDto.transactionId())
                .time(createDto.time())
                .bankAccount(bankAccountRepository.findById(createDto.bankAccountId()).get())
                .build();
    }

    public Transaction mapFrom(Transaction transaction, TransactionUpdateDto updateDto) {
        transaction.setAmount(updateDto.amount());
        transaction.setTransactionType(updateDto.type());
        return transaction;
    }
}
