package com.urlcategorizer.controller;

import com.urlcategorizer.CategoryInitialization;
import com.urlcategorizer.entity.Category;
import com.urlcategorizer.service.UrlContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryInitialization categoryInitialization;
    private final UrlContentService urlContentService;

    @PostMapping("/match")
    public Set<String> matchCategories(@RequestParam Set<String> urls) {
        return urls.stream()
                .flatMap(url -> {
                    try {
                        String pageContent = urlContentService.getTextFromUrl(url).getText();
                        return findMatchingCategories(pageContent).stream();
                    } catch (Exception e) {
                        return Set.<String>of().stream();
                    }
                })
                .collect(Collectors.toSet());
    }

    private Set<String> findMatchingCategories(String pageContent) {
        String lowerCaseContent = pageContent.toLowerCase();

        return categoryInitialization.getPredefinedCategories().stream()
                .filter(category -> category.getKeywords()
                        .stream()
                        .anyMatch(keyword -> lowerCaseContent.contains(keyword.getKeyword())))
                .map(Category::getName)
                .collect(Collectors.toSet());
    }
}
