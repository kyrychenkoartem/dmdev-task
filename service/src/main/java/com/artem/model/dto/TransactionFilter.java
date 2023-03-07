package com.artem.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionFilter {

    LocalDateTime time;
    String referenceNumber;
}
