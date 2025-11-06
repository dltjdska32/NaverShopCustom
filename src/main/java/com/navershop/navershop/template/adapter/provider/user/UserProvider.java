package com.navershop.navershop.template.adapter.provider.user;

/**
 * 사용자 제공 인터페이스
 *
 * 각 팀이 구현해야 하는 인터페이스
 *
 * @param <USER> 프로젝트의 User 엔티티 타입
 */
public interface UserProvider<USER> {

    /**
     * 사용자 조회
     *
     * @param userId 사용자 ID
     * @return User 엔티티
     */
    USER findById(Long userId);
}
