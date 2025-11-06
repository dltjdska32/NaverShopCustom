package com.navershop.navershop.template.adapter.option;

/**
 * 상품 옵션 생성기 인터페이스
 *
 * 옵션이 필요한 사람만 구현
 *
 * @param <PRODUCT> 프로젝트의 Product 엔티티 타입
 */
public interface OptionGenerator<PRODUCT> {

    /**
     * 상품에 옵션 추가
     *
     * @param product Product 엔티티
     * @param categoryName 카테고리명
     */
    void generateAndAddOptions(PRODUCT product, String categoryName);

    /**
     * 해당 카테고리가 옵션이 필요한지 확인
     *
     * @param categoryName 카테고리명
     * @return 옵션 필요 여부
     */
    default boolean needsOptions(String categoryName) {
        return true;
    }
}
