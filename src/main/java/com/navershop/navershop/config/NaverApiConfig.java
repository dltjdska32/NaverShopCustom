package com.navershop.navershop.config;

import com.google.common.util.concurrent.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 외부 API 연동 설정
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
@Configuration
public class NaverApiConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public io.github.resilience4j.ratelimiter.RateLimiter naverRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(3)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofSeconds(5))
                .build();
        return io.github.resilience4j.ratelimiter.RateLimiter.of("naver-global", config);
    }

    /**
     * Google Guava RateLimiter Bean
     * 1초에 1개의 요청만 허용
     */
    @Bean
    public RateLimiter guavaRateLimiter() {
        return RateLimiter.create(1.0); // 1초에 1개
    }
}