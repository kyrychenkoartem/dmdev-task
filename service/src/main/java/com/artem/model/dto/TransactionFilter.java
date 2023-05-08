package com.artem.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record TransactionFilter(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                LocalDateTime time,
                                String referenceNumber) {
}
