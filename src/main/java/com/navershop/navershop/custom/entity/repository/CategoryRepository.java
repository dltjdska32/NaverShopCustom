package com.navershop.navershop.custom.entity.repository;

import com.navershop.navershop.custom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카테고리 JPA 레포
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
