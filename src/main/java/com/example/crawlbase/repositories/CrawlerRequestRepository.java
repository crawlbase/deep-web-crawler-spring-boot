package com.example.crawlbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.crawlbase.models.CrawlerRequest;

public interface CrawlerRequestRepository extends JpaRepository<CrawlerRequest, Long> {
	
	// Find by column Name and value
	 List<CrawlerRequest> findByRid(String value);
}
