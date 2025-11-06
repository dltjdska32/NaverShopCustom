package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.Product;
import com.navershop.navershop.custom.entity.repository.ProductRepository;
import com.navershop.navershop.template.adapter.provider.product.ProductProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        // 새로 생성된 Product는 ID가 null이므로 name과 member로 중복 체크
        if (product.getId() == null) {
            return productRepository.existsByNameAndMember(product.getName(), product.getMember());
        }
        // 이미 저장된 Product는 ID로 체크
        return productRepository.existsById(product.getId());
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * 저장된 상품 리스트를 반환하는 메서드 (내부 사용)
     */
    public List<Product> saveAllAndReturn(List<Product> products) {
        if (products.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 중복 제거 후 저장
        List<Product> nonDuplicates = products.stream()
                .filter(product -> !isDuplicate(product))
                .toList();
        
        if (nonDuplicates.isEmpty()) {
            log.warn("⚠️ 모든 상품이 중복입니다. 저장하지 않습니다.");
            return new ArrayList<>();
        }
        
        // 실제로 저장된 상품들 (ID가 자동으로 할당됨)
        log.info("💾 ========== DB INSERT 시작 ==========");
        log.info("💾 저장 시도할 상품 개수: {}개", nonDuplicates.size());
        log.info("💾 SQL INSERT 쿼리가 아래에 출력됩니다 (show-sql: true 설정됨)");
        
        List<Product> savedProducts;
        try {
            savedProducts = productRepository.saveAll(nonDuplicates);
            
            log.info("✅ ========== DB INSERT 완료 ==========");
            log.info("✅ 저장된 상품 개수: {}개", savedProducts.size());
            
            if (savedProducts.isEmpty()) {
                log.error("❌❌❌ 저장된 상품이 0개입니다! INSERT가 실행되지 않았을 수 있습니다! ❌❌❌");
            } else {
                log.info("✅✅✅ INSERT 성공! {}개 상품이 DB에 저장되었습니다! ✅✅✅", savedProducts.size());
            }
        } catch (Exception e) {
            log.error("❌❌❌ INSERT 실패! 에러 발생: {}", e.getMessage(), e);
            throw e;
        }
        
        return savedProducts;
    }
    
    @Override
    public int saveAll(List<Product> products) {
        if (products.isEmpty()) {
            return 0;
        }
        
        // 저장 전 ID 상태 확인
        long productsWithNullId = products.stream().filter(p -> p.getId() == null).count();
        log.debug("saveAll 호출: 총 {}개, ID null인 것 {}개", products.size(), productsWithNullId);
        
        // 중복 제거 후 저장
        List<Product> nonDuplicates = products.stream()
                .filter(product -> !isDuplicate(product))
                .toList();
        
        log.info("🔍 저장 전 상태: 총 {}개, 중복 제거 후 {}개 (중복: {}개)", 
                products.size(), nonDuplicates.size(), products.size() - nonDuplicates.size());
        
        if (nonDuplicates.isEmpty()) {
            log.warn("⚠️ 모든 상품이 중복입니다. 저장하지 않습니다.");
            return 0;
        }
        
        // 저장 전 상품 정보 로그 (처음 3개만)
        log.info("📝 저장할 상품 정보 (처음 3개):");
        for (int i = 0; i < Math.min(3, nonDuplicates.size()); i++) {
            Product p = nonDuplicates.get(i);
            log.info("  - 이름: {}, 가격: {}, 브랜드: {}, 멤버ID: {}, ID: {}", 
                    p.getName(), p.getPrice(), 
                    p.getBrand() != null ? p.getBrand().getBrandName() : "null",
                    p.getMember() != null ? p.getMember().getId() : "null",
                    p.getId());
        }
        
        // 실제로 저장된 상품들 (ID가 자동으로 할당됨)
        log.info("💾 ========== DB INSERT 시작 ==========");
        log.info("💾 저장 시도할 상품 개수: {}개", nonDuplicates.size());
        log.info("💾 SQL INSERT 쿼리가 아래에 출력됩니다 (show-sql: true 설정됨)");
        
        List<Product> savedProducts;
        try {
            savedProducts = productRepository.saveAll(nonDuplicates);
            
            log.info("✅ ========== DB INSERT 완료 ==========");
            log.info("✅ 저장된 상품 개수: {}개", savedProducts.size());
            
            if (savedProducts.isEmpty()) {
                log.error("❌❌❌ 저장된 상품이 0개입니다! INSERT가 실행되지 않았을 수 있습니다! ❌❌❌");
            } else {
                log.info("✅✅✅ INSERT 성공! {}개 상품이 DB에 저장되었습니다! ✅✅✅", savedProducts.size());
            }
        } catch (Exception e) {
            log.error("❌❌❌ INSERT 실패! 에러 발생: {}", e.getMessage(), e);
            throw e;
        }
        
        // 저장 후 ID 할당 확인 및 상세 로그
        int savedWithIdCount = 0;
        log.info("🔍 저장 후 상품 ID 확인:");
        for (Product product : savedProducts) {
            if (product.getId() == null) {
                log.error("❌ 저장 후에도 ID가 null입니다: {}", product.getName());
            } else {
                savedWithIdCount++;
                // 저장된 상품 정보 로그 출력 (처음 5개만)
                if (savedWithIdCount <= 5) {
                    log.info("  ✅ ID={}, 이름={}, 가격={}, 브랜드={}", 
                            product.getId(), product.getName(), product.getPrice(), 
                            product.getBrand() != null ? product.getBrand().getBrandName() : "null");
                }
            }
        }
        
        // 실제 저장된 개수 반환
        int actuallySavedCount = savedProducts.size();
        
        log.info("📊 최종 저장 결과: 요청 {}개, 중복 제거 후 {}개, 실제 저장 {}개 (ID 할당됨: {}개)", 
                products.size(), nonDuplicates.size(), actuallySavedCount, savedWithIdCount);
        
        if (actuallySavedCount == 0) {
            log.error("❌ 저장된 상품이 0개입니다! INSERT 실패 가능성 확인 필요.");
        }
        
        return actuallySavedCount;
    }
}
