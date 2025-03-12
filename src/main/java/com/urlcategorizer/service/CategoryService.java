package com.urlcategorizer.service;

import com.urlcategorizer.entity.Category;
import com.urlcategorizer.entity.CategoryKeyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service is responsible for managing the predefined categories and their associated keywords.
 */
@Service
@Slf4j
public class CategoryService {

    /**
     * Create a Category instance
     * @param name
     * @param keywords
     * @return
     */
    public Category createCategory(String name, Set<String> keywords) {
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
