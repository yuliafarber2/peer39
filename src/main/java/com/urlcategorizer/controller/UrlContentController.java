package com.urlcategorizer.controller;

import com.urlcategorizer.dto.UrlTextResultDTO;
import com.urlcategorizer.service.UrlContentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/web-text-extract")
@RequiredArgsConstructor
public class UrlContentController {

    private final UrlContentService urlContentService;

    /**
     * Fetches text from a list of URLs.
     *
     * @param urls List of URLs passed as query parameters.
     * @return A list of URLs and their extracted text.
     */
    @GetMapping("/extract-html")
    public List<UrlTextResultDTO> extractHtml(@RequestParam Set<String> urls) {
        return Collections.unmodifiableList(
                urls.stream()
                        .map(urlContentService::getHtmlFromUrl)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/extract-text")
    public List<UrlTextResultDTO> extractCleanText(@RequestParam Set<String> urls) {
        return Collections.unmodifiableList(
                urls.stream()
                        .map(urlContentService::getTextFromUrl)
                        .collect(Collectors.toList())
        );
    }
}
