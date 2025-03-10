package com.urlcategorizer.service;

import com.urlcategorizer.dto.UrlTextResultDTO;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class UrlContentService {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
    private static final int TIMEOUT = 10000;
    @Value("${useUrlContentCache}")
    private boolean useUrlContentCache;
    private final Map<String, UrlTextResultDTO> urlCache = new ConcurrentHashMap<>();

    /**
     * Fetches and extracts clean text from a given URL.
     *
     * @param url The URL to process.
     * @return A DTO containing the URL and extracted text.
     */
    public UrlTextResultDTO getTextFromUrl(String url) {
        // Check cache first
        if (useUrlContentCache && urlCache.containsKey(url)) {
            return urlCache.get(url);
        }

        String content;
        try {
            Document doc = fetchDocument(url);
            String bodyText = doc.body().text();
            if (bodyText.isEmpty()) {
                // If the body text is empty, it's likely because JavaScript is responsible for rendering the content.
                return UrlTextResultDTO.builder()
                        .url(url)
                        .text("No content found, this page may require JavaScript rendering").build();
            }
            content = bodyText;
        } catch (IOException e) {
            log.error("Failed to fetch content from URL: {}", url, e);
            content = String.format("Error fetching content: %s", e.getMessage());
        }

        UrlTextResultDTO urlTextResultDTO = UrlTextResultDTO.builder()
                .url(url)
                .text(content)
                .build();
        if (useUrlContentCache) {
            urlCache.put(url, urlTextResultDTO);
        }
        return urlTextResultDTO;
    }

    /**
     * Fetches the raw HTML content from a given URL.
     *
     * @param url The URL to process.
     * @return A DTO containing the URL and raw HTML content.
     */
    public UrlTextResultDTO getHtmlFromUrl(String url) {
        try {
            Document doc = fetchDocument(url);
            return UrlTextResultDTO.builder()
                    .url(url)
                    .text(doc.html()).build();
        } catch (IOException e) {
            log.error("Failed to fetch HTML from URL: {}", url, e);
            return UrlTextResultDTO.builder()
                    .url(url)
                    .text(String.format("Error fetching html content: %s", e.getMessage())).build();
        }
    }

    /**
     * Fetches and parses the HTML document from a given URL.
     *
     * @param url The URL to fetch.
     * @return A parsed Jsoup Document.
     * @throws IOException if an error occurs during fetching.
     */
    private Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .get();
    }
}
