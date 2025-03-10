package com.urlcategorizer;

import com.urlcategorizer.entity.Category;
import com.urlcategorizer.entity.CategoryKeyword;
import com.urlcategorizer.service.UrlContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CategoryInitialization {

    private final Set<Category> predefinedCategories;
    private final UrlContentService urlContentService;

    public CategoryInitialization(UrlContentService urlContentService) {
        this.urlContentService = urlContentService;
        this.predefinedCategories = Set.of(
                createCategory("Star Wars", Set.of("star war", "starwars", "starwar", "r2d2", "may the force be with you")),
                createCategory("Basketball", Set.of("basketball", "nba", "ncaa", "lebron james", "john stockton", "anthony davis"))
        );
    }

    public Set<Category> getPredefinedCategories() {
        return predefinedCategories;
    }

    public UrlContentService getUrlContentService() {
        return urlContentService;
    }

    public void initializeModel() {
        // This can be used if you need to set up more categories dynamically in the future
        log.info("Model Initialized with predefined categories.");
    }

    // Create a Category instance
    private Category createCategory(String name, Set<String> keywords) {
        Set<CategoryKeyword> categoryKeywords = keywords.stream()
                .map(keyword -> CategoryKeyword.builder()
                        .keyword(keyword.toLowerCase())
                        .build())
                .collect(Collectors.toSet());

        return Category.builder()
                .name(name)
                .keywords(categoryKeywords)
                .build();
    }
}
