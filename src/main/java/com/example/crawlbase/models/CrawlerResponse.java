package com.example.crawlbase.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class CrawlerResponse {

	@Id
	@GeneratedValue
	private Long id;
	
	private Integer pcStatus;
	private Integer originalStatus;
	
	@Column(columnDefinition = "LONGTEXT")
	private String pageHtml;
	
	@OneToOne
    @JoinColumn(name = "request_id")
	private CrawlerRequest crawlerRequest;
	
}
