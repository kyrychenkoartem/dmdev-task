package com.artem.model.entity;

import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import java.util.Objects;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bank_card", schema = "public")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    private String expiryDate;

    @Enumerated(EnumType.STRING)
    private BankType bank;

    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CardType cardType;


    //    @ManyToOne
//    private User user;
    @Column(name = "user_id")
    private Long user;

    //    Set<BankAccount>
//    ManyToMany
    @Column(name = "bank_account_id")
    private Long bankAccounts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BankCard bankCard = (BankCard) o;
        return id != null && Objects.equals(id, bankCard.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}