package com.urlcategorizer.service;

import com.urlcategorizer.datastructure.Trie;
import com.urlcategorizer.dto.UrlCategorizationResultDTO;
import com.urlcategorizer.entity.Category;
import com.urlcategorizer.entity.CategoryKeyword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This service is responsible for taking the URL content and categorizing it based on predefined categories and keywords.
 * It checks for keyword matches in the content to classify the page.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UrlCategorizationService {
    private static final String WORD_SPLIT_REGEX = "\\W+";
    private final UrlContentService urlContentService;

    /**
     * Method which responsible for classifying URL for matching categories.
     *
     * @param url
     * @param categories
     * @return
     */
    public UrlCategorizationResultDTO categorizeUrl(String url, Set<Category> categories) {
        String pageContent = urlContentService.retrieveTextFromUrl(url).getText();
        Set<String> matchingCategories = findMatchingCategories(pageContent, categories);
        return UrlCategorizationResultDTO.builder()
                .url(url)
                .categories(matchingCategories)  // Set of matching categories
                .build();
    }

    public UrlCategorizationResultDTO categorizeUrlV2(String url, Set<Category> categories) {
        String pageContent = urlContentService.retrieveTextFromUrl(url).getText();
        Set<String> matchingCategories = findMatchingCategoriesWithRegex(pageContent, categories);
        return UrlCategorizationResultDTO.builder()
                .url(url)
                .categories(matchingCategories)  // Set of matching categories
                .build();
    }

    public boolean hasCategories(String url, Set<Category> categories) {
        return !categorizeUrl(url, categories).getCategories().isEmpty();
    }

    public Map<String, Boolean> hasCategories(List<String> urls, Set<Category> categories) {
        return urls.stream()
                .collect(Collectors.toMap(
                        url -> url,
                        url -> hasCategories(url, categories)
                ));
    }

    /**
     * Finds matching categories based on keywords or phrases using regular expressions.
     * <p>
     * This method uses regular expressions to match multiple keywords or phrases within the page content.
     * It first compiles a regex pattern for each category based on its keywords and then checks if any of these patterns
     * match the content of the URL page.
     * <p>
     * Time Complexity:
     * - Compiling the regex pattern for each category: O(M * K), where:
     * - M = number of categories
     * - K = number of keywords per category
     * - Matching the content against the regex pattern: O(N * P), where:
     * - N = length of the page content (in terms of the number of characters)
     * - P = number of regex patterns (one per category)
     * <p>
     * The overall time complexity is therefore approximately O(M * K + N * P), where:
     * - M is the number of categories
     * - K is the number of keywords per category
     * - N is the length of the page content
     * - P is the number of categories (each having a compiled regex pattern)
     *
     * @param pageContent The content of the web page (text extracted from the URL).
     * @param categories  The set of predefined categories, each containing multiple keywords.
     * @return A set of category names that match any of the keywords or phrases in the page content.
     */
    public Set<String> findMatchingCategoriesWithRegex(String pageContent, Set<Category> categories) {
        Set<String> matchingCategories = new HashSet<>();

        // Compile regex patterns for each category based on its keywords
        Map<String, Pattern> categoryPatterns = buildCategoryPatterns(categories);

        // For each category, check if the page content matches any of the category's keywords/phrases
        for (Map.Entry<String, Pattern> entry : categoryPatterns.entrySet()) {
            Matcher matcher = entry.getValue().matcher(pageContent);
            if (matcher.find()) {
                matchingCategories.add(entry.getKey()); // Add category to matching categories
            }
        }

        return matchingCategories;
    }

    /**
     * Builds a mapping of category names to their corresponding compiled regex patterns.
     * Combines the category keywords into one regex pattern per category.
     *
     * @param categories The set of predefined categories.
     * @return A map where the key is the category name and the value is the compiled regex pattern.
     */
    private Map<String, Pattern> buildCategoryPatterns(Set<Category> categories) {
        Map<String, Pattern> categoryPatterns = new HashMap<>();

        categories.forEach(category -> {
            StringBuilder regex = new StringBuilder();

            // Add each keyword/phrase to the regex pattern
            for (CategoryKeyword keyword : category.getKeywords()) {
                regex.append("(?i)").append(Pattern.quote(keyword.getKeyword())).append("|");
            }

            // Remove the last "|" from the regex pattern
            if (regex.length() > 0) regex.setLength(regex.length() - 1);

            // Compile the pattern for the category
            categoryPatterns.put(category.getName(), Pattern.compile(regex.toString()));
        });

        return categoryPatterns;
    }

    /**
     * Find matching categories based on keywords
     * This is the simplest form of searching for keywords in the text extracted from a URL.
     * It just checks if any of the keywords in a category appear in the text.
     * Time Complexity**: O(N * M * K), where:
     * - N = Length of page content.
     * - M = Number of categories.
     * - K = Number of keywords.
     */
    private Set<String> findMatchingCategories(String pageContent, Set<Category> categories) {
        String lowerCaseContent = pageContent.toLowerCase();

        return categories.stream()
                .filter(category -> category.getKeywords()
                        .stream()
                        .anyMatch(keyword -> lowerCaseContent.contains(keyword.getKeyword())))
                .map(Category::getName)
                .collect(Collectors.toSet());
    }
}
