package com.urlcategorizer.controller;

import com.urlcategorizer.dto.UrlContentResultDTO;
import com.urlcategorizer.service.UrlContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/url-content")
@RequiredArgsConstructor
public class UrlContentController {

    private final UrlContentService urlContentService;

    /**
     * Retrieves an HTML content from a list of URLs.
     *
     * @param urls List of URLs passed as query parameters.
     * @return A set of URLs with a html content for each.
     */
    @GetMapping("/retrieve-html")
    public Set<UrlContentResultDTO> retrieveHtml(@RequestParam Set<String> urls) {
        return Collections.unmodifiableSet(
                urls.stream()
                        .map(urlContentService::retrieveHtmlFromUrl)
                        .collect(Collectors.toSet()));
    }

    /**
     * Retrieves only the text from html. Tags are cleaned but the text between them remains.
     * No scripts included in the content.
     *
     * @param urls List of URLs passed as query parameters.
     * @return A set of URLs with a html content for each.
     */
    @GetMapping("/retrieve-text")
    public Set<UrlContentResultDTO> retrieveText(@RequestParam Set<String> urls) {
        return Collections.unmodifiableSet(
                urls.stream()
                        .map(urlContentService::retrieveTextFromUrl)
                        .collect(Collectors.toSet()));
    }
}
