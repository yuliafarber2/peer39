package com.urlcategorizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Builder
public class Category {
    private String name;
    private Set<CategoryKeyword> keywords;
}
