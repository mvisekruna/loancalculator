package com.leanpay.loancalculator.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanCalculatorOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Loan Calculator API")
                .description("REST API for calculating loan installment plans using Spring Boot")
                .version("1.0.0"));
    }
}