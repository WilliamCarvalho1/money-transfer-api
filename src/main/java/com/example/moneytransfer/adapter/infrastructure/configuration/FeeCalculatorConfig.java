package com.example.moneytransfer.adapter.infrastructure.configuration;

import com.example.moneytransfer.domain.fee.FeeCalculationStrategy;
import com.example.moneytransfer.domain.fee.FeeCalculatorService;
import com.example.moneytransfer.domain.fee.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FeeCalculatorConfig {

    @Bean
    public FeeCalculatorService feeCalculatorService() {
        return new FeeCalculatorService(feeStrategies());
    }

    @Bean
    public List<FeeCalculationStrategy> feeStrategies() {
        return List.of(
                new UpToOneThousandSameDayStrategy(),
                new OneToTwoThousandEarlyStrategy(),
                new HighAmount11To20DaysStrategy(),
                new HighAmount21To30DaysStrategy(),
                new HighAmount31To40DaysStrategy(),
                new HighAmountOver40DaysStrategy()
        );
    }
}
