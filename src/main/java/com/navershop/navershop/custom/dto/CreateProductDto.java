package com.navershop.navershop.custom.dto;

import com.navershop.navershop.custom.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 생성 시 사용하는 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {
    private Product product;
    private String mainImg;
}
