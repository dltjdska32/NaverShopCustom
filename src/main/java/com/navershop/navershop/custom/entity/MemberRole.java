package com.navershop.navershop.custom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    USER("ROLE_USER", "일반 사용자"),
    SELLER("ROLE_SELLER", "판매자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String description;

    public static MemberRole fromCode(String roleCode) {
        return Optional.ofNullable(roleCode)
                .map(code -> {
                    try {
                        return MemberRole.valueOf(code.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }).orElse(null);
    }

}