package com.example.moneytransfer.infrastructure.config;

import com.example.moneytransfer.domain.shared.FeeCalculatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeeCalculatorConfig {

    @Bean
    public FeeCalculatorService feeCalculatorService() {
        return new FeeCalculatorService();
    }
}
