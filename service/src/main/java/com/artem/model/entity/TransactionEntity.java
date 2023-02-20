package com.artem.model.entity;

import com.artem.model.type.TransactionType;
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
@Table(name = "banking_transaction", schema = "public")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType transactionType;

    /**
     * Bank/Utility account number to which the transaction is made
     **/
    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * The bank account to/from which the transaction is made
     **/
    // OneToOne BankAccount
    @Column(name = "bank_account_id") // to delete
    private Long bankAccount;
}
