package com.bgd.app.entity;

import lombok.Data;

@Data
public class RecommendProDto extends ProductDto{

    private String countryName;

    private String categoryName;
}
