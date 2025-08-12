package com.resourcemgmt.projectsowservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
public class ProjectSowMgmtApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjectSowMgmtApplication.class, args);
	}
}
