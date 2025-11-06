package com.navershop.navershop.core.api;

import com.navershop.navershop.custom.service.BaseCrawlingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 크롤링 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
public class ProductCrawlingController {

    private final BaseCrawlingServiceImpl crawlingService;

    /**
     * 전체 카테고리 크롤링
     *
     * @param userId DB에 등록된 판매자 ID
     * @param productsPerCategory 카테고리 별로 가져올 데이터의 개수 (최소 1개 ~ 최대 1000개)
     * POST /api/crawling/start
     */
    @PostMapping("/products")
    public com.navershop.navershop.template.service.BaseCrawlingService.CrawlingResult startCrawling(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "5") int productsPerCategory) {

        log.info("크롤링 시작: adminUserId={}, productsPerCategory={}",
                userId, productsPerCategory);

        return crawlingService.crawlAllCategoriesReactive(userId, productsPerCategory);
    }
}
