package com.example.crawlbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CrawlbaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrawlbaseApplication.class, args);
	}

}
