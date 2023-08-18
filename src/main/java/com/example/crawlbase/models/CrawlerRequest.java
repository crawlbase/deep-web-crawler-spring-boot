package com.example.crawlbase.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CrawlerRequest {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String url;
	private String type;
	private Integer status;
	private String rid;
	
	@OneToOne(mappedBy = "crawlerRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CrawlerResponse crawlerResponse;

}
