package com.navershop.navershop.custom.entity.repository;

import com.navershop.navershop.custom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 JPA 레포
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
