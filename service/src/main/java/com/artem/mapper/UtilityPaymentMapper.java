package com.artem.mapper;

import com.artem.model.dto.UtilityPaymentCreateDto;
import com.artem.model.dto.UtilityPaymentUpdateDto;
import com.artem.model.entity.UtilityPayment;

public class UtilityPaymentMapper implements Mapper<UtilityPaymentCreateDto, UtilityPayment> {
    @Override
    public UtilityPayment mapFrom(UtilityPaymentCreateDto createDto) {
        return UtilityPayment.builder()
                .amount(createDto.amount())
                .referenceNumber(createDto.account())
                .status(createDto.status())
                .utilityAccount(createDto.toAccount())
                .transaction(createDto.transactionId())
                .build();
    }

    public UtilityPayment mapFrom(UtilityPayment utilityPayment, UtilityPaymentUpdateDto updateDto) {
        utilityPayment.setAmount(updateDto.amount());
        utilityPayment.setStatus(updateDto.status());
        return utilityPayment;
    }
}
