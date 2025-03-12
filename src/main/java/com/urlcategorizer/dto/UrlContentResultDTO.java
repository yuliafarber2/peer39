package com.urlcategorizer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UrlContentResultDTO {
    private String url;
    private String text;
}
