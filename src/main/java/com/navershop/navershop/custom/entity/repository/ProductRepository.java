package com.navershop.navershop.custom.entity.repository;

import com.navershop.navershop.custom.entity.Product;
import com.navershop.navershop.custom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 상품 JPA 레포
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndMember(String name, User member);
    boolean existsByNameAndMember(String name, User member);
}
