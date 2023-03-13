package com.artem.mapper;

import com.artem.model.dto.FundTransferCreateDto;
import com.artem.model.dto.FundTransferUpdateDto;
import com.artem.model.entity.FundTransfer;

public class FundTransferMapper implements Mapper<FundTransferCreateDto, FundTransfer> {
    @Override
    public FundTransfer mapFrom(FundTransferCreateDto createDto) {
        return FundTransfer.builder()
                .fromAccount(createDto.fromAccount())
                .toAccount(createDto.toAccount())
                .amount(createDto.amount())
                .status(createDto.status())
                .transaction(createDto.transactionId())
                .build();
    }

    public FundTransfer mapFrom(FundTransfer fundTransfer, FundTransferUpdateDto updateDto) {
        fundTransfer.setAmount(updateDto.amount());
        fundTransfer.setStatus(updateDto.status());
        return fundTransfer;
    }
}
