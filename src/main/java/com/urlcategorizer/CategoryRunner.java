package com.urlcategorizer;

import com.urlcategorizer.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryRunner implements CommandLineRunner {

    private final CategoryInitialization categoryInitialization;

    @Autowired
    public CategoryRunner(CategoryInitialization categoryInitialization) {
        this.categoryInitialization = categoryInitialization;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 2) {
            System.out.println("Please provide two arguments: list of categories and list of URLs.");
            return;
        }

        // Parse categories and URLs from the command line arguments
        List<String> inputCategories = List.of(args[0].split(","));
        List<String> inputUrls = List.of(args[1].split(","));

        // Initialize predefined categories
        categoryInitialization.initializeModel();

        Set<String> inputCategoriesSet = Set.copyOf(inputCategories);

        // Get the predefined categories
        Set<Category> predefinedCategories = categoryInitialization.getPredefinedCategories();

        // Filter the categories to only include the ones in the inputCategories list
        Set<Category> filteredCategories = predefinedCategories.stream()
                .filter(category -> inputCategoriesSet.contains(category.getName()))
                .collect(Collectors.toSet());

        for (String url : inputUrls) {
            categorizeUrl(url, filteredCategories);
        }
    }

    private void categorizeUrl(String url, Set<Category> categories) {
        String pageContent;
        try {
            pageContent = categoryInitialization.getUrlContentService().getTextFromUrl(url).getText();
        } catch (Exception e) {
            System.out.println("Error fetching content for URL: " + url);
            return;
        }

        // Find matching categories for the URL
        Set<String> matchingCategories = findMatchingCategories(pageContent, categories);
        if (!matchingCategories.isEmpty()) {
            System.out.println("URL: " + url + " matches categories: " + matchingCategories);
        } else {
            System.out.println("URL: " + url + " does not match any category");
        }
    }

    // Find matching categories based on keywords
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
