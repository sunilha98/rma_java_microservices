package com.resourcemgmt.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
@EnableFeignClients
public class ReportingIntegrationApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReportingIntegrationApplication.class, args);
	}
}
