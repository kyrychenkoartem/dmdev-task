package com.artem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TestConfiguration.class)
public class TestApplicationConfiguration {
}
