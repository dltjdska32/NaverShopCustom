package com.navershop.navershop.custom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionCategory {
    SIZE ("사이즈"),
    COLOR ("색상");

    private String description;
}
