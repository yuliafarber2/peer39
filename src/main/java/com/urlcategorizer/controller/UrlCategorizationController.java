package com.urlcategorizer.controller;

import com.urlcategorizer.UrlCategorizationRunner;
import com.urlcategorizer.dto.UrlCategorizationResultDTO;
import com.urlcategorizer.entity.Category;
import com.urlcategorizer.service.UrlCategorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/url-categorization")
@RequiredArgsConstructor
public class UrlCategorizationController {

    private final UrlCategorizationService urlCategorizationService;
    private final UrlCategorizationRunner urlCategorizationRunner;

    /**
     * This endpoint categorizes a given URL by returning the set of categories associated with it.
     *
     * @param url
     * @return
     */
    @GetMapping("/categorize")
    public ResponseEntity<UrlCategorizationResultDTO> categorizeUrl(@RequestParam String url) {
        Set<Category> categories = urlCategorizationRunner.getPredefinedCategories();
        return ResponseEntity.ok(urlCategorizationService.categorizeUrl(url, categories));
    }

    /**
     * This endpoint checks if any categories exist for the given URL and returns a boolean result for that URL.
     *
     * @param url
     * @return
     */
    @PostMapping("/has-category")
    public ResponseEntity<Map<String, Boolean>> checkIfCategoryExists(@RequestParam String url) {
        Set<Category> categories = urlCategorizationRunner.getPredefinedCategories();
        return ResponseEntity.ok(Map.of(url, urlCategorizationService.hasCategories(url, categories)));
    }

    /**
     * This endpoint checks if any categories exist for list of URLs and returns a boolean result for each URL.
     *
     * @param urls
     * @return
     */
    @PostMapping("/has-categories")
    public ResponseEntity<Map<String, Boolean>> checkIfCategoriesExistForUrls(@RequestBody List<String> urls) {
        Set<Category> categories = urlCategorizationRunner.getPredefinedCategories();
        return ResponseEntity.ok(urlCategorizationService.hasCategories(urls, categories));
    }
}
