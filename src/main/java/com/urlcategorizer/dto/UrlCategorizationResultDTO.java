package com.urlcategorizer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Builder
public class UrlCategorizationResultDTO {
    private String url;
    private Set<String> categories;
}
