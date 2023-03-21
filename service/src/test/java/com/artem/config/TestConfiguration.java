package com.artem.config;

import com.artem.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApplicationConfiguration.class)
public class TestConfiguration {

    @Bean
    SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
