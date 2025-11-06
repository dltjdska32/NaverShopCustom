package com.navershop.navershop.template.adapter.mapper;


import com.navershop.navershop.core.dto.NaverShoppingResponse;

/**
 * Product 엔티티 매퍼 인터페이스
 *
 * 각 팀이 구현해야 하는 인터페이스
 *
 * @param <PRODUCT> 프로젝트의 Product 엔티티 타입
 * @param <CATEGORY> 프로젝트의 Category 엔티티 타입
 * @param <USER> 프로젝트의 User 엔티티 타입
 */
public interface ProductMapper<PRODUCT, CATEGORY, USER> {

    /**
     * 네이버 쇼핑 아이템을 Product 엔티티로 변환
     *
     * @param item 네이버 API 응답 아이템
     * @param category 카테고리 엔티티
     * @param seller 판매자 엔티티
     * @return 변환된 Product 엔티티
     */
    PRODUCT map(NaverShoppingResponse.NaverShoppingItem item, CATEGORY category, USER seller);

}
