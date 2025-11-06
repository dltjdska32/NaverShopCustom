package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.User;
import com.navershop.navershop.custom.entity.repository.UserRepository;
import com.navershop.navershop.template.adapter.provider.user.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 사용자 관련 구현해야 하는 코드
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
@Component
@RequiredArgsConstructor
public class UserProviderImpl implements UserProvider<User> {

    private final UserRepository userRepository;

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));
    }
}