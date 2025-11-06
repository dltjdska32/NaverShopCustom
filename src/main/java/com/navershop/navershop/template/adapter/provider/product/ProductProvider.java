package com.navershop.navershop.template.adapter.provider.product;

/**
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */

import java.util.List;

/**
 * Product 저장 인터페이스
 *
 *  각 팀이 구현해야 하는 인터페이스
 *
 * @param <PRODUCT> 프로젝트의 Product 엔티티 타입
 */
public interface ProductProvider<PRODUCT> {

    /**
     * 중복 상품 확인
     *
     * @param product Product 엔티티
     * @return 중복 여부
     */
    boolean isDuplicate(PRODUCT product);

    /**
     * Product 저장
     *
     * @param product Product 엔티티
     * @return 저장된 Product
     */
    PRODUCT save(PRODUCT product);

    /**
     * 일괄 저장 (기본 구현 제공)
     *
     * @param products Product 리스트
     * @return 저장된 개수
     */
    default int saveAll(List<PRODUCT> products) {
        int count = 0;
        for (PRODUCT product : products) {
            if (!isDuplicate(product)) {
                save(product);
                count++;
            }
        }
        return count;
    }
}
