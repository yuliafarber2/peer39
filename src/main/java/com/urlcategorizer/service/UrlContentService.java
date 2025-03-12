package com.urlcategorizer.service;

import com.urlcategorizer.dto.UrlContentResultDTO;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This service is responsible for retrieving and processing the content of a URL.
 * It should handle fetching the HTML content of the page and cleaning it (e.g., removing HTML tags, extracting text).
 */
@Service
@Slf4j
public class UrlContentService {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
    private static final int TIMEOUT = 10000;
    private final Map<String, UrlContentResultDTO> urlCache = new ConcurrentHashMap<>();

    /**
     * Retrieves an HTML content from a given URL.
     *
     * @param url The URL to process.
     * @return A DTO containing the URL and an HTML content.
     */
    public UrlContentResultDTO retrieveHtmlFromUrl(String url) {
        try {
            Document doc = retrieveDocument(url);
            return UrlContentResultDTO.builder()
                    .url(url)
                    .text(doc.html())
                    .build();
        } catch (IOException e) {
            log.error("Failed to retrieve HTML from URL: {}", url, e);
            return UrlContentResultDTO.builder()
                    .url(url)
                    .text(String.format("Error retrieving html content: %s", e.getMessage()))
                    .build();
        }
    }

    /**
     * Retrieves only the text from html. Tags are cleaned but the text between them remains.
     * No scripts included in the content.
     *
     * @param url The URL to process.
     * @return A DTO containing the URL and an HTML content.
     */
    public UrlContentResultDTO retrieveTextFromUrl(String url) {

        String content;
        try {
            Document doc = retrieveDocument(url);
            String bodyText = doc.body().text();
            if (bodyText.isEmpty()) {
                // If the body text is empty, it's likely because JavaScript is responsible for rendering the content.
                return UrlContentResultDTO.builder()
                        .url(url)
                        .text("No content found, this page may require JavaScript rendering")
                        .build();
            }
            content = bodyText;
        } catch (IOException e) {
            log.error("Failed to retrieve content from URL: {}", url, e);
            content = String.format("Error retrieving content: %s", e.getMessage());
        }

        return UrlContentResultDTO.builder()
                .url(url)
                .text(content)
                .build();
    }


    /**
     * Retrieves a content from a given url.
     *
     * @param url The URL to retrieve.
     * @return A parsed Jsoup Document.
     * @throws IOException if an error occurs during fetching.
     */
    private Document retrieveDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .get();
    }
}
