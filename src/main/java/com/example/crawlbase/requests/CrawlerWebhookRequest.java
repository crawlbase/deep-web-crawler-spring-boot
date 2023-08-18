package com.example.crawlbase.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrawlerWebhookRequest {

	private Integer pc_status;
	private Integer original_status;
	private String rid;
	private String url;
	private String body;
}
