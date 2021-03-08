package com.zup.proposal.financialproposal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FinancialproposalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialproposalApplication.class, args);
	}

}
