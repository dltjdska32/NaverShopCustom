package com.navershop.navershop.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 네이버 쇼핑 API 응답 DTO (Core - 수정 금지)
 */
@Data
public class NaverShoppingResponse {

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("display")
    private Integer display;

    @JsonProperty("items")
    private List<NaverShoppingItem> items;

    @Data
    public static class NaverShoppingItem {
        @JsonProperty("title")
        private String title;

        @JsonProperty("image")
        private String image;

        @JsonProperty("link")
        private String link;

        @JsonProperty("lprice")
        private String lprice;

        @JsonProperty("hprice")
        private String hprice;

        @JsonProperty("brand")
        private String brand;

        @JsonProperty("maker")
        private String maker;

        @JsonProperty("category1")
        private String category1;

        @JsonProperty("category2")
        private String category2;

        @JsonProperty("category3")
        private String category3;

        @JsonProperty("category4")
        private String category4;

        @JsonProperty("productId")
        private String productId;

        @JsonProperty("productType")
        private String productType;

        @JsonProperty("mallName")
        private String mallName;
    }
}
