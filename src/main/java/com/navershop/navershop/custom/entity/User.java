package com.navershop.navershop.custom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * User 엔티티 (판매자)
 */
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    MemberRole role;

    // 일반멤버 생성
    public static User createMember(String username, String encodedPassword, String address) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .address(address)
                .role(MemberRole.USER)
                .build();
    }

    // 맴버 업데이트
    public void updateMember(String username, String address, MemberRole role) {
        if (StringUtils.hasText(username)) {
            this.username = username;
        }
        if (StringUtils.hasText(address)) {
            this.address = address;
        }
        if (role != null) {
            this.role = role;
        }
    }
}