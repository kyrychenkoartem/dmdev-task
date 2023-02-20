package com.artem.model.entity;

import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
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
@Table(name = "bank_card", schema = "public")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Enumerated(EnumType.STRING)
    private BankType bank;

    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

//     User OneToOne
    @Column(name = "user_id")
    private Long user;

//    List<BankAccounts>
//    OneToMany
//    private Long bankAccounts;
}
