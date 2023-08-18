package com.example.crawlbase.requests;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScrapeUrlRequest {
	
	private List<String> urls;

}
