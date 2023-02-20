package com.artem.model.entity;

import com.artem.model.type.TransactionStatus;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "utility_payment", schema = "public")
public class UtilityPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    /**
     * Utility account number to which the transaction is made
     **/
    @Column(name = "reference_number")
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    /**
     * Utility account associated with payment
     **/
//    OneToOne UtilityAccount
    @Column(name = "utility_account") // to delete
    private Long utilityAccount;

    /**
     * Transaction id associated with payment
     **/
//    OneToOne Transaction
    @Column(name = "banking_transaction_id") // to delete
    private Long transaction;
}
