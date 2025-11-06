package com.navershop.navershop.custom.entity;

import com.navershop.navershop.custom.entity.enums.ProductCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductCategoryConverter implements AttributeConverter<ProductCategory, String> {

    @Override
    public String convertToDatabaseColumn(ProductCategory attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public ProductCategory convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return ProductCategory.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
