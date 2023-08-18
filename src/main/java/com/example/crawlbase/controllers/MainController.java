package com.example.crawlbase.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crawlbase.requests.ScrapeUrlRequest;
import com.example.crawlbase.services.MainService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/scrape")
@Slf4j
public class MainController {
	
	@Autowired
	private MainService mainService;

	@PostMapping("/push-urls")
	public ResponseEntity<Void> pushUrlsToCawler(@RequestBody ScrapeUrlRequest request) {
		try {
			if(!request.getUrls().isEmpty()) {
				// Asynchronously Process The Request
				mainService.pushUrlsToCrawler(request.getUrls(), "parent");
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error("Error in pushUrlsToCrawler function: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
