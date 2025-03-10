package com.urlcategorizer.controller;

import com.urlcategorizer.dto.UrlTextResultDTO;
import com.urlcategorizer.service.UrlContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UrlContentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UrlContentService urlContentService;

    @Test
    public void testGetTextFromUrls() throws Exception {
        // Sample URLs
        String url1 = "http://example1.com";
        String url2 = "http://example2.com";

        // Sample expected response DTOs
        UrlTextResultDTO response1 = new UrlTextResultDTO(url1, "Text content from example1.com");
        UrlTextResultDTO response2 = new UrlTextResultDTO(url2, "Text content from example2.com");

        // Mock the service behavior
        when(urlContentService.getHtmlFromUrl(url1)).thenReturn(response1);
        when(urlContentService.getHtmlFromUrl(url2)).thenReturn(response2);

        // Perform the HTTP GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/extract-text")
                        .param("urls", url1, url2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(url1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("Text content from example1.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].url").value(url2))  // Assert URL in response
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("Text content from example2.com"));
    }

    @Test
    public void testGetCleanTextFromUrls() throws Exception {
        // Sample URLs
        String url1 = "http://example1.com";
        String url2 = "http://example2.com";

        // Sample expected clean response DTOs
        UrlTextResultDTO response1 = new UrlTextResultDTO(url1, "Clean text content from example1.com");
        UrlTextResultDTO response2 = new UrlTextResultDTO(url2, "Clean text content from example2.com");

        // Mock the service behavior
        when(urlContentService.getTextFromUrl(url1)).thenReturn(response1);
        when(urlContentService.getTextFromUrl(url2)).thenReturn(response2);

        // Perform the HTTP GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/extract-clean-text")
                        .param("urls", url1, url2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(url1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("Clean text content from example1.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].url").value(url2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("Clean text content from example2.com"));
    }
}
