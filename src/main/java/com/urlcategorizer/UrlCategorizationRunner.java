package com.urlcategorizer;

import com.urlcategorizer.dto.UrlCategorizationResultDTO;
import com.urlcategorizer.entity.Category;
import com.urlcategorizer.service.CategoryService;
import com.urlcategorizer.service.UrlCategorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UrlCategorizationRunner implements CommandLineRunner {

    private Set<Category> predefinedCategories;
    private final CategoryService categoryService;
    private final UrlCategorizationService urlCategorizationService;

    @PostConstruct
    public void init() {
        predefinedCategories = initializeModel();
    }

    @Override
    public void run(String... args) {
        if (args.length != 2) {
            System.out.println("Please provide two arguments: list of categories and list of URLs.");
            return;
        }

        List<String> inputCategories = List.of(args[0].split(","));
        List<String> inputUrls = List.of(args[1].split(","));

        Set<String> inputCategoriesSet = Set.copyOf(inputCategories);

        // Filter the categories to only include the ones in the inputCategories list
        Set<Category> filteredCategories = predefinedCategories.stream()
                .filter(category -> inputCategoriesSet.contains(category.getName()))
                .collect(Collectors.toSet());

        inputUrls.forEach(url -> {
            System.out.println("Categorizing URL: " + url);
            UrlCategorizationResultDTO categorizationResult = urlCategorizationService.categorizeUrl(url, filteredCategories);
            System.out.println("Matching Categories (Simple Search): " + categorizationResult.getCategories());
            UrlCategorizationResultDTO categorizationResultV2 = urlCategorizationService.categorizeUrlV2(url, filteredCategories);
            System.out.println("Matching Categories (Regex-based Matching): " + categorizationResultV2.getCategories());
        });
    }

    private Set<Category> initializeModel() {
        return Set.of(
                categoryService.createCategory("Star Wars", Set.of("star war", "starwars", "starwar", "r2d2", "may the force be with you")),
                categoryService.createCategory("Basketball", Set.of("basketball", "nba", "ncaa", "lebron james", "john stockton", "anthony davis"))
        );
    }

    public Set<Category> getPredefinedCategories() {
        return predefinedCategories;
    }
}
