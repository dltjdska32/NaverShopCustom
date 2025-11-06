package com.navershop.navershop.template.adapter.provider.ProductDetail;

import java.util.List;

public interface DetailProvider <PRODUCTDETAIL>{

    /**
     * 중복 상품 확인
     *
     * @param product Product 엔티티
     * @return 중복 여부
     */
    boolean isDuplicate(PRODUCTDETAIL product);

    /**
     * Product 저장
     *
     * @param product Product 엔티티
     * @return 저장된 Product
     */
    PRODUCTDETAIL save(PRODUCTDETAIL product);

    /**
     * 일괄 저장 (기본 구현 제공)
     *
     * @param products Product 리스트
     * @return 저장된 개수
     */
    default int saveAll(List<PRODUCTDETAIL> products) {
        int count = 0;
        for (PRODUCTDETAIL product : products) {
            if (!isDuplicate(product)) {
                save(product);
                count++;
            }
        }
        return count;
    }
}
