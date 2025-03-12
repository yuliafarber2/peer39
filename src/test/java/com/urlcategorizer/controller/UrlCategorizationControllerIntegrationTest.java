package com.urlcategorizer.controller;

import com.urlcategorizer.UrlCategorizationRunner;
import com.urlcategorizer.dto.UrlCategorizationResultDTO;
import com.urlcategorizer.entity.Category;
import com.urlcategorizer.entity.CategoryKeyword;
import com.urlcategorizer.service.UrlCategorizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UrlCategorizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlCategorizationService urlCategorizationService;

    @MockBean
    private UrlCategorizationRunner urlCategorizationRunner;

    @Test
    public void testCategorizeUrl() throws Exception {
        String url = "http://example.com";
        Category category1 = new Category("Star Wars", Set.of(new CategoryKeyword("star war")));
        Category category2 = new Category("Basketball", Set.of(new CategoryKeyword("nba")));

        UrlCategorizationResultDTO resultDTO = UrlCategorizationResultDTO.builder()
                .url(url)
                .categories(Set.of("Star Wars", "Basketball"))
                .build();

        // Mock the categorizeUrl method and the predefined categories retrieval
        when(urlCategorizationService.categorizeUrl(url, Set.of(category1, category2)))
                .thenReturn(resultDTO);
        when(urlCategorizationRunner.getPredefinedCategories())
                .thenReturn(Set.of(category1, category2));

        // Perform the request and assert the response
        mockMvc.perform(get("/api/url-categorization/categorize")
                        .param("url", url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(url))  // Expect the URL field
                .andExpect(jsonPath("$.categories", containsInAnyOrder("Star Wars", "Basketball")));  // Expect categories as a set
    }

    @Test
    public void testCheckIfCategoryDoesNotExist() throws Exception {
        String url = "http://example.com";

        when(urlCategorizationService.hasCategories(url, Set.of()))
                .thenReturn(false);

        mockMvc.perform(post("/api/url-categorization/has-category")
                        .param("url", url))  // Pass URL as query param
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['http://example.com']").value(false));
    }

    @Test
    public void testCheckIfCategoryExists() throws Exception {
        String url = "http://example.com";

        when(urlCategorizationService.hasCategories(url, Set.of()))
                .thenReturn(true);

        mockMvc.perform(post("/api/url-categorization/has-category")
                        .param("url", url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['http://example.com']").value(true));
    }

    @Test
    public void testCheckIfCategoriesExistForUrls() throws Exception {
        // Sample URLs
        List<String> urls = List.of("http://example1.com", "http://example2.com");

        // Sample expected results
        Map<String, Boolean> expectedResults = Map.of(
                "http://example1.com", true,
                "http://example2.com", false
        );

        when(urlCategorizationService.hasCategories(urls, Set.of())).thenReturn(expectedResults);

        mockMvc.perform(post("/api/url-categorization/has-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"http://example1.com\", \"http://example2.com\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['http://example1.com']").value(true))
                .andExpect(jsonPath("$.['http://example2.com']").value(false));
    }
}
