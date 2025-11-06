package com.navershop.navershop.custom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ProductCategory {

    MEN(null, "남성", 1, 1L, null),
    WOMEN(null, "여성", 1, 2L, null),
    CHILDREN(null, "아동", 1, 3L, null),
    ALL_PERSON(null, "남여공용", 1, 4L, null),

    // --- 남성용 (MEN: M_) ---
    M_SLIPPERS(MEN, "슬리퍼", 2, 5L, 1L),
    M_SANDALS(MEN, "샌들", 2, 6L, 1L),
    M_SNEAKERS(MEN, "스니커즈", 2, 7L, 1L),
    M_RUNNING_SHOES(MEN, "운동화", 2, 8L, 1L),
    M_LOAFERS(MEN, "로퍼", 2, 9L, 1L),
    M_BOOTS(MEN, "부츠", 2, 10L, 1L),
    M_WALKERS(MEN, "워커", 2, 11L, 1L),
    M_SLIP_ON(MEN, "슬립온", 2, 12L, 1L),
    M_CHELSEA_BOOTS(MEN, "첼시부츠", 2, 13L, 1L),
    M_OXFORD_SHOES(MEN, "옥스퍼드 슈즈", 2, 14L, 1L),
    M_WINTER_BOOTS(MEN, "방한화", 2, 15L, 1L),
    M_RAIN_BOOTS(MEN, "레인부츠", 2, 16L, 1L),
    M_AQUA_SHOES(MEN, "아쿠아슈즈", 2, 17L, 1L),
    M_DRESS_SHOES(MEN, "드레스 신발", 2, 18L, 1L),

    // --- 여성용 (WOMEN: W_) ---
    W_HIGH_HEELS(WOMEN, "하이힐", 2, 19L, 2L),
    W_FLAT_SHOES(WOMEN, "플랫슈즈", 2, 20L, 2L),
    W_SLIPPERS(WOMEN, "슬리퍼", 2, 21L, 2L),
    W_SANDALS(WOMEN, "샌들", 2, 22L, 2L),
    W_SNEAKERS(WOMEN, "스니커즈", 2, 23L, 2L),
    W_RUNNING_SHOES(WOMEN, "운동화", 2, 24L, 2L),
    W_LOAFERS(WOMEN, "로퍼", 2, 25L, 2L),
    W_BOOTS(WOMEN, "부츠", 2, 26L, 2L),
    W_WALKERS(WOMEN, "워커", 2, 27L, 2L),
    W_SLIP_ON(WOMEN, "슬립온", 2, 28L, 2L),
    W_CHELSEA_BOOTS(WOMEN, "첼시부츠", 2, 29L, 2L),
    W_OXFORD_SHOES(WOMEN, "옥스퍼드 슈즈", 2, 30L, 2L),
    W_WINTER_BOOTS(WOMEN, "방한화", 2, 31L, 2L),
    W_RAIN_BOOTS(WOMEN, "레인부츠", 2, 32L, 2L),
    W_AQUA_SHOES(WOMEN, "아쿠아슈즈", 2, 33L, 2L),
    W_DRESS_SHOES(WOMEN, "드레스 신발", 2, 34L, 2L),

    // --- 아동용 (CHILDREN: C_) ---
    C_SLIPPERS(CHILDREN, "슬리퍼", 2, 35L, 3L),
    C_SANDALS(CHILDREN, "샌들", 2, 36L, 3L),
    C_SNEAKERS(CHILDREN, "스니커즈", 2, 37L, 3L),
    C_RUNNING_SHOES(CHILDREN, "운동화", 2, 38L, 3L),
    C_LOAFERS(CHILDREN, "로퍼", 2, 39L, 3L),
    C_BOOTS(CHILDREN, "부츠", 2, 40L, 3L),
    C_WALKERS(CHILDREN, "워커", 2, 41L, 3L),
    C_SLIP_ON(CHILDREN, "슬립온", 2, 42L, 3L),
    C_WINTER_BOOTS(CHILDREN, "방한화", 2, 43L, 3L),
    C_RAIN_BOOTS(CHILDREN, "레인부츠", 2, 44L, 3L),
    C_AQUA_SHOES(CHILDREN, "아쿠아슈즈", 2, 45L, 3L),

    // --- 남여공용 (ALL_PERSON: A_) ---
    A_SLIPPERS(ALL_PERSON, "슬리퍼", 2, 46L, 4L),
    A_SANDALS(ALL_PERSON, "샌들", 2, 47L, 4L),
    A_SNEAKERS(ALL_PERSON, "스니커즈", 2, 48L, 4L),
    A_RUNNING_SHOES(ALL_PERSON, "운동화", 2, 49L, 4L),
    A_LOAFERS(ALL_PERSON, "로퍼", 2, 50L, 4L),
    A_BOOTS(ALL_PERSON, "부츠", 2, 51L, 4L),
    A_WALKERS(ALL_PERSON, "워커", 2, 52L, 4L),
    A_SLIP_ON(ALL_PERSON, "슬립온", 2, 53L, 4L),
    A_WINTER_BOOTS(ALL_PERSON, "방한화", 2, 54L, 4L),
    A_RAIN_BOOTS(ALL_PERSON, "레인부츠", 2, 55L, 4L),
    A_AQUA_SHOES(ALL_PERSON, "아쿠아슈즈", 2, 56L, 4L);


    private final ProductCategory parentCategory;
    private final String description;
    private final int level;
    private final Long productCategoryId; // 추가된 필드
    private final Long refProductCategoryId; // 추가된 필드

    /**
     * 특정 레벨의 카테고리 목록을 찾습니다. (예: 1레벨 카테고리 목록)
     */
    public static List<ProductCategory> findByLevel(int level) {
        return Arrays.stream(ProductCategory.values())
                .filter(c -> c.getLevel() == level)
                .collect(Collectors.toList());
    }

    /**
     * 특정 부모 카테고리의 자식 목록을 찾습니다.
     */
    public static List<ProductCategory> findChildrenOf(ProductCategory parent) {
        if (parent == null) {
            return List.of();
        }
        return Arrays.stream(ProductCategory.values())
                .filter(c -> parent.equals(c.getParentCategory()))
                .collect(Collectors.toList());
    }

}