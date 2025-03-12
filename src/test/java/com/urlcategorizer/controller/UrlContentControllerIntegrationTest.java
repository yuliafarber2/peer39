package com.urlcategorizer.controller;

import com.urlcategorizer.dto.UrlContentResultDTO;
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
    public void testRetrieveHTMNFromURL() throws Exception {
        String url1 = "http://example1.com";
        String url2 = "http://example2.com";

        UrlContentResultDTO response1 = new UrlContentResultDTO(url1, "HTML content from example1.com");
        UrlContentResultDTO response2 = new UrlContentResultDTO(url2, "HTML content from example2.com");

        when(urlContentService.retrieveHtmlFromUrl(url1)).thenReturn(response1);
        when(urlContentService.retrieveHtmlFromUrl(url2)).thenReturn(response2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/url-content/retrieve-html")
                        .param("urls", url1, url2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Check if the expected URL and text are present in the response, without assuming order
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.url == '" + url1 + "')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.url == '" + url2 + "')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.text == 'HTML content from example1.com')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.text == 'HTML content from example2.com')]").exists());
    }

    @Test
    public void testRetrieveTextFromURL() throws Exception {
        String url1 = "http://example1.com";
        String url2 = "http://example2.com";

        UrlContentResultDTO response1 = new UrlContentResultDTO(url1, "Content from example1.com");
        UrlContentResultDTO response2 = new UrlContentResultDTO(url2, "Content from example2.com");

        when(urlContentService.retrieveTextFromUrl(url1)).thenReturn(response1);
        when(urlContentService.retrieveTextFromUrl(url2)).thenReturn(response2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/url-content/retrieve-text")
                        .param("urls", url1, url2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Check if the expected URL and text are present in the response, without assuming order
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.url == '" + url1 + "')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.url == '" + url2 + "')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.text == 'Content from example1.com')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.text == 'Content from example2.com')]").exists());
    }
}
