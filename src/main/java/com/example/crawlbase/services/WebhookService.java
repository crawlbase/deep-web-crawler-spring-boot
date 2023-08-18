package com.example.crawlbase.services;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.crawlbase.models.CrawlerRequest;
import com.example.crawlbase.models.CrawlerResponse;
import com.example.crawlbase.repositories.CrawlerRequestRepository;
import com.example.crawlbase.repositories.CrawlerResponseRepository;
import com.example.crawlbase.requests.CrawlerWebhookRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebhookService {
	
	@Autowired
	private CrawlerRequestRepository crawlerRequestRepository;
	@Autowired
	private CrawlerResponseRepository crawlerResponseRepository;
	@Autowired
	private MainService mainService;
	
	@Async("taskExecutor")
	public void handleWebhookResponse(HttpHeaders headers, byte[] compressedBody) {
		try {
			// Unzip the gziped body
			GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedBody));
			InputStreamReader reader = new InputStreamReader(gzipInputStream);
			
			// Process the uncompressed HTML content
			StringBuilder htmlContent = new StringBuilder();
			char[] buffer = new char[1024];
			int bytesRead;
			while ((bytesRead = reader.read(buffer)) != -1) {
				htmlContent.append(buffer, 0, bytesRead);
			}
			
			// The HTML String
			String htmlString = htmlContent.toString();
	
			// Create the request object
			CrawlerWebhookRequest request = CrawlerWebhookRequest.builder()
					.original_status(Integer.valueOf(headers.getFirst("original_status")))
					.pc_status(Integer.valueOf(headers.getFirst("pc_status")))
					.rid(headers.getFirst("rid"))
					.url(headers.getFirst("url"))
					.body(htmlString).build();
			
			// Save CrawlerResponse Model
			List<CrawlerRequest> results = crawlerRequestRepository.findByRid(request.getRid());
			CrawlerRequest crawlerRequest = !results.isEmpty() ? results.get(0) : null;
			if(crawlerRequest != null) {
				// Build CrawlerResponse Model
				CrawlerResponse crawlerResponse = CrawlerResponse.builder().pcStatus(request.getPc_status())
						.originalStatus(request.getOriginal_status()).pageHtml(request.getBody()).crawlerRequest(crawlerRequest).build();
				crawlerResponseRepository.save(crawlerResponse);
			}
			
			// Only Deep Crawl Parent Url
			if(headers.getFirst("type").equalsIgnoreCase("parent")) {
				deepCrawlParentResponse(request.getBody(), request.getUrl());
			}
		} catch (Exception e) {
			 log.error("Error in handleWebhookResponse function: " + e.getMessage());
		}
		
	}
	
	private void deepCrawlParentResponse(String html, String baseUrl) {
		Document document = Jsoup.parse(html);
	    Elements hyperLinks = document.getElementsByTag("a");
	    List<String> links = new ArrayList<String>();
	    
	    String url = null;
	    for (Element hyperLink : hyperLinks) {
	    	url = processUrl(hyperLink.attr("href"), baseUrl);
	    	if(url != null) {
	    		links.add(url);
	    	}
	    }
	    mainService.pushUrlsToCrawler(links, "child");
	}
	
	private String processUrl(String href, String baseUrl) {
        try {
            if (href != null && !href.isEmpty()) {
            	baseUrl =  normalizeUrl(baseUrl);
                String processedUrl = normalizeUrl(href.startsWith("/") ? baseUrl + href : href);
                if (isValidUrl(processedUrl) &&
                    !processedUrl.replace("http://", "").replace("https://", "").equals(baseUrl.replace("http://", "").replace("https://", "")) &&
                    // Only considering the URLs with same hostname
                    Objects.equals(new URI(processedUrl).getHost(), new URI(baseUrl).getHost())) {
                	
                    return processedUrl;
                }
            }
        } catch (Exception e) {
        	log.error("Error in processUrl function: " + e.getMessage());
        }
        return null;
    }

    private boolean isValidUrl(String string) {
    	String urlRegex = "((http|https)://)(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    private String normalizeUrl(String url) throws URISyntaxException {
        url = url.replace("//www.", "//");
        url = url.split("#")[0];
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        return url;
    }
}
