package com.zup.proposal.financialproposal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class FinancialproposalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialproposalApplication.class, args);
	}

}
