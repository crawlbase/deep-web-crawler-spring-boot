package com.example.crawlbase.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crawlbase.services.WebhookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {
	
	@Autowired
	private WebhookService webhookService;
	
	@PostMapping("/crawlbase")
	public ResponseEntity<Void> crawlbaseCrawlerResponse(@RequestHeader HttpHeaders headers, @RequestBody byte[] compressedBody) {
		try {
			if(!headers.getFirst(HttpHeaders.USER_AGENT).equalsIgnoreCase("Crawlbase Monitoring Bot 1.0") &&
			   "gzip".equalsIgnoreCase(headers.getFirst(HttpHeaders.CONTENT_ENCODING)) &&
			   headers.getFirst("pc_status").equals("200")) {
				// Asynchronously Process The Request
				webhookService.handleWebhookResponse(headers, compressedBody);
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			 log.error("Error in crawlbaseCrawlerResponse function: " + e.getMessage());
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
