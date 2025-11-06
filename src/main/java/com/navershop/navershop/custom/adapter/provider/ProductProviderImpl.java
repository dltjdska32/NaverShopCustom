package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.Product;
import com.navershop.navershop.custom.entity.repository.ProductRepository;
import com.navershop.navershop.template.adapter.provider.product.ProductProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 제품 관련 구현해야 하는 코드
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductProviderImpl implements ProductProvider<Product> {

    private final ProductRepository productRepository;

    @Override
    public boolean isDuplicate(Product product) {
        return productRepository.existsById(product.getId());
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public int saveAll(List<Product> products) {
        if (products.isEmpty()) {
            return 0;
        }
        
        // 저장 전 ID 상태 확인
        long productsWithNullId = products.stream().filter(p -> p.getId() == null).count();
        log.debug("saveAll 호출: 총 {}개, ID null인 것 {}개", products.size(), productsWithNullId);
        
        // 실제로 저장된 상품들 (ID가 할당된 것만)
        List<Product> savedProducts = productRepository.saveAll(products);
        
        // 실제 저장된 개수 반환 (ID가 null이 아닌 것들만 카운트)
        long actuallySavedCount = savedProducts.stream()
                .filter(p -> p.getId() != null)
                .count();
        
        // 저장 실패 시 경고 로그
        if (actuallySavedCount < products.size()) {
            log.warn("⚠️ 상품 저장 일부 실패: 요청 {}개, 실제 저장 {}개 (차이: {}개)", 
                    products.size(), actuallySavedCount, products.size() - actuallySavedCount);
        } else {
            log.debug("✅ 상품 저장 성공: 요청 {}개 모두 저장됨", products.size());
        }
        
        return (int) actuallySavedCount;
    }
}
