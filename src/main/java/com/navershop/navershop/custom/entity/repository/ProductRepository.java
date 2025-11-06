package com.navershop.navershop.custom.entity.repository;

import com.navershop.navershop.custom.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 JPA 레포
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
