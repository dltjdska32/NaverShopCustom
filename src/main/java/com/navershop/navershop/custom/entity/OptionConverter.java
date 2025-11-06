package com.navershop.navershop.custom.entity;

import com.navershop.navershop.custom.entity.enums.OptionCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OptionConverter implements AttributeConverter<OptionCategory, String> {

    @Override
    public String convertToDatabaseColumn(OptionCategory attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public OptionCategory convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return OptionCategory.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
