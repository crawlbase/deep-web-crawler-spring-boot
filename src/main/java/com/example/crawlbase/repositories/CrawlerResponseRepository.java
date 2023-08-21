package com.example.crawlbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crawlbase.models.CrawlerResponse;

public interface CrawlerResponseRepository extends JpaRepository<CrawlerResponse, Long> {

}
