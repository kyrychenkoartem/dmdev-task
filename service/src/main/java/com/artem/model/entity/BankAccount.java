package com.artem.model.entity;

import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
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
@Table(name = "bank_account", schema = "public")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private BigDecimal availableBalance;

    private BigDecimal actualBalance;

    //    ManyToOne
    @Column(name = "account_id") // to delete
    private Long account;

//    ManyToMany
//    Set<BankCard>
//    private Long bankCard;
}
