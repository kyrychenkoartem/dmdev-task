package com.artem.mapper;

import com.artem.dao.UtilityAccountRepository;
import com.artem.model.dto.UtilityPaymentCreateDto;
import com.artem.model.dto.UtilityPaymentUpdateDto;
import com.artem.model.entity.UtilityPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilityPaymentMapper implements Mapper<UtilityPaymentCreateDto, UtilityPayment> {

    private final UtilityAccountRepository utilityAccountRepository;

    @Override
    public UtilityPayment mapFrom(UtilityPaymentCreateDto createDto) {
        return UtilityPayment.builder()
                .amount(createDto.amount())
                .referenceNumber(createDto.account())
                .status(createDto.status())
                .utilityAccount(utilityAccountRepository.findById(createDto.toUtilityAccountId()).get())
                .transaction(createDto.transactionId())
                .build();
    }

    public UtilityPayment mapFrom(UtilityPayment utilityPayment, UtilityPaymentUpdateDto updateDto) {
        utilityPayment.setAmount(updateDto.amount());
        utilityPayment.setStatus(updateDto.status());
        return utilityPayment;
    }
}
