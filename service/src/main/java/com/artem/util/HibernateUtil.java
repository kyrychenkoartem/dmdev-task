package com.artem.util;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.BankCard;
import com.artem.model.entity.FundTransfer;
import com.artem.model.entity.TransactionEntity;
import com.artem.model.entity.User;
import com.artem.model.entity.UtilityAccount;
import com.artem.model.entity.UtilityPayment;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        var configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Account.class);
        configuration.addAnnotatedClass(BankAccount.class);
        configuration.addAnnotatedClass(BankCard.class);
        configuration.addAnnotatedClass(FundTransfer.class);
        configuration.addAnnotatedClass(TransactionEntity.class);
        configuration.addAnnotatedClass(UtilityAccount.class);
        configuration.addAnnotatedClass(UtilityPayment.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        return configuration;
    }
}
