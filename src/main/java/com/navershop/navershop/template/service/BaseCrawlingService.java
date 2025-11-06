package com.navershop.navershop.template.service;

import com.google.common.util.concurrent.RateLimiter;
import com.navershop.navershop.custom.adapter.provider.*;
import com.navershop.navershop.custom.entity.*;
import com.navershop.navershop.custom.dto.CreateProductDto;
import com.navershop.navershop.custom.entity.enums.ProductImageType;
import com.navershop.navershop.custom.enums.BrandEnum;
import com.navershop.navershop.template.adapter.provider.category.CategoryProvider;
import com.navershop.navershop.template.adapter.mapper.ProductMapper;
import com.navershop.navershop.template.adapter.option.OptionGenerator;
import com.navershop.navershop.template.adapter.provider.product.ProductProvider;
import com.navershop.navershop.template.adapter.provider.user.UserProvider;
import com.navershop.navershop.core.api.NaverShoppingApiClient;
import com.navershop.navershop.core.dto.NaverShoppingResponse;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 범용 크롤링 서비스 추상 클래스 (Core - 수정 금지)
 */
@Slf4j
public abstract class BaseCrawlingService<PRODUCT, CATEGORY, USER> {

    protected final NaverShoppingApiClient apiClient;
    protected final ProductMapper<PRODUCT, CATEGORY, USER> productMapper;
    protected final ProductProvider<PRODUCT> productProvider;
    protected final CategoryProvider<CATEGORY> categoryProvider;
    protected final UserProvider<USER> userProvider;
    protected final OptionGenerator<PRODUCT> optionGenerator;
    protected final ProductDetailProviderImpl productDetailProviderImpl;
    protected final OptionMappingProviderImpl optionMappingProviderImpl;
    protected final ImageProviderIml imageProviderIml;
    protected final OptionProviderImpl optionProviderImpl;
    protected final ProductProviderImpl productProviderImpl;
    protected final RateLimiter rateLimiter;
    protected final TransactionTemplate transactionTemplate;
    protected final Executor crawlingExecutor;
    
    protected BaseCrawlingService(
            NaverShoppingApiClient apiClient,
            ProductMapper<PRODUCT, CATEGORY, USER> productMapper,
            ProductProvider<PRODUCT> productProvider,
            CategoryProvider<CATEGORY> categoryProvider,
            UserProvider<USER> userProvider,
            OptionGenerator<PRODUCT> optionGenerator,
            ProductDetailProviderImpl productDetailProvider,
            OptionMappingProviderImpl optionMappingProvider,
            ImageProviderIml imageProvider,
            OptionProviderImpl optionProvider,
            ProductProviderImpl productProviderImpl,
            RateLimiter rateLimiter,
            PlatformTransactionManager transactionManager,
            Executor crawlingExecutor
    ) {
        this.apiClient = apiClient;
        this.productMapper = productMapper;
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.userProvider = userProvider;
        this.optionGenerator = optionGenerator;
        this.productDetailProviderImpl = productDetailProvider;
        this.optionMappingProviderImpl = optionMappingProvider;
        this.imageProviderIml = imageProvider;
        this.optionProviderImpl = optionProvider;
        this.productProviderImpl = productProviderImpl;
        this.rateLimiter = rateLimiter;
        this.crawlingExecutor = crawlingExecutor;
        
        // TransactionTemplate 설정
        // 락 타임아웃 방지를 위해 타임아웃을 60초로 증가
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setTimeout(60);
        // 락 충돌 감소를 위해 READ_COMMITTED 격리 수준 사용
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // 읽기 전용이 아니므로 false
        this.transactionTemplate.setReadOnly(false);
    }

    public CrawlingResult crawlAllCategoriesReactive(Long userId, int productsPerCategory) {
        log.info("===== 🚀 Reactive 크롤링 시작 =====");
        long startTime = System.currentTimeMillis();

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid userId");
        }

        // userId 검증 (엔티티는 각 스레드에서 다시 로드하여 영속성 컨텍스트 문제 방지)
        USER adminUser = userProvider.findById(userId);
        if (adminUser == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        if (productsPerCategory <= 0) {
            throw new IllegalArgumentException("productsPerCategory must be > 0");
        }

        List<CATEGORY> targetCategories = findLeafCategories();
        if (targetCategories == null || targetCategories.isEmpty()) {
            throw new IllegalStateException("No categories configured");
        }
        log.info("검색 대상 카테고리 수: {}", targetCategories.size());

        AtomicInteger totalProducts = new AtomicInteger(0);
        AtomicInteger successCategories = new AtomicInteger(0);
        AtomicInteger failedCategories = new AtomicInteger(0);
        Map<Long, CategoryResult> categoryResults = new ConcurrentHashMap<>();

        // 동시 실행 수 제한 (DB 락 충돌 방지)
        // 데이터베이스 쓰기 작업이 많으므로 동시 실행 수를 5개로 제한
        int maxConcurrentCategories = 5;
        Semaphore semaphore = new Semaphore(maxConcurrentCategories);
        log.info("동시 실행 카테고리 수 제한: {}개", maxConcurrentCategories);

        // CompletableFuture로 병렬 처리 (동시 실행 수 제한)
        // Spring 관리 Executor 사용하여 트랜잭션 컨텍스트 전파 보장
        final Long finalUserId = userId;  // final 변수로 캡처 (엔티티 대신 ID 전달)
        List<CompletableFuture<Void>> futures = targetCategories.stream()
                .map(category -> CompletableFuture.runAsync(() -> {
                    Long categoryId = categoryProvider.getCategoryId(category);
                    String categoryName = categoryProvider.getCategoryName(category);

                    try {
                        // 세마포어로 동시 실행 수 제한
                        semaphore.acquire();
                        
                        try {
                            log.info("카테고리 '{}' 크롤링 시작... [Thread: {}]",
                                    categoryName, Thread.currentThread().getName());

                            // 각 스레드에서 User를 다시 로드하여 영속성 컨텍스트 문제 방지
                            USER threadLocalUser = userProvider.findById(finalUserId);
                            if (threadLocalUser == null) {
                                throw new IllegalStateException("User not found in thread: " + finalUserId);
                            }

                            // Reactive 방식으로 크롤링
                            int savedCount = crawlAndSaveByCategoryReactive(
                                    category, threadLocalUser, productsPerCategory);

                            if (savedCount > 0) {
                                categoryResults.put(categoryId, CategoryResult.success(
                                        categoryId, categoryName, savedCount));
                                totalProducts.addAndGet(savedCount);
                                successCategories.incrementAndGet();
                                log.info("카테고리 '{}' 완료: {}개 저장", categoryName, savedCount);
                            } else {
                                categoryResults.put(categoryId, CategoryResult.noResults(
                                        categoryId, categoryName));
                                log.warn("카테고리 '{}'에서 검색 결과 없음", categoryName);
                            }
                        } finally {
                            // 항상 세마포어 해제
                            semaphore.release();
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("카테고리 '{}' 세마포어 대기 중단", categoryName);
                        categoryResults.put(categoryId, CategoryResult.failed(
                                categoryId, categoryName, "Interrupted: " + e.getMessage()));
                        failedCategories.incrementAndGet();
                    } catch (Exception e) {
                        log.error("카테고리 '{}' 크롤링 실패: {}", categoryName, e.getMessage(), e);
                        categoryResults.put(categoryId, CategoryResult.failed(
                                categoryId, categoryName, e.getMessage()));
                        failedCategories.incrementAndGet();
                    }
                }, crawlingExecutor))  // Spring 관리 Executor 사용
                .toList();

        // 모든 작업 완료 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;

        log.info("===== ✅ Reactive 크롤링 완료 =====");
        log.info("총 카테고리: {}, 성공: {}, 실패: {}, 총 상품: {}, 소요시간: {}초",
                targetCategories.size(), successCategories.get(), failedCategories.get(),
                totalProducts.get(), duration);

        return CrawlingResult.builder()
                .totalCategories(targetCategories.size())
                .successCategories(successCategories.get())
                .failedCategories(failedCategories.get())
                .totalProducts(totalProducts.get())
                .durationSeconds(duration)
                .categoryResults(new ArrayList<>(categoryResults.values()))
                .build();
    }

    /**
     * 카테고리별 크롤링 (Reactive 방식)
     */

    protected int crawlAndSaveByCategoryReactive(CATEGORY category, USER seller, int count) {
        String categoryName = categoryProvider.getCategoryName(category);
        String keyword = buildFullCategoryPath(category);

        log.info("검색 키워드: '{}' (카테고리: '{}')", keyword, categoryName);

        int display = Math.min(count, 100);

        List<CreateProductDto> pr = new ArrayList<>();
        List<String> brand = BrandEnum.getAllBrandNames();
        for(int i = 0; i < brand.size(); i++) {
            rateLimiter.acquire(); // 1초에 1명만 이 라인을 통과합니다.

            // 🚀 Reactive 방식으로 API 호출
            NaverShoppingResponse response = apiClient.searchMultiplePagesReactive(
                    brand.get(i) + " " +  keyword , count, display, "sim");

            if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
                log.warn("'{}{}'에 대한 검색 결과 없음. 다음 브랜드로 넘어갑니다.", brand.get(i), keyword);
                continue; // return 0; (X) -> continue; (O)
            }



            List<CreateProductDto> list = response.getItems().stream()
                    .limit(count)
                    .map(item -> {  // 병렬 스트림 제거 (트랜잭션 문제 해결)

                        PRODUCT product = productMapper.map(item, category, seller);

                        CreateProductDto createProductDto = new CreateProductDto((Product) product, item.getImage());
                        //  옵션 생성
                        if (optionGenerator != null && optionGenerator.needsOptions(categoryName)) {
                            optionGenerator.generateAndAddOptions(product, categoryName);
                        }


                        return createProductDto;
                    })
                    .toList();


            pr.addAll(list);
        }


        // 🚀 Reactive 방식으로 API 호출
//        NaverShoppingResponse response = apiClient.searchMultiplePagesReactive(
//                keyword, count, display, "sim");

//        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
//            return 0;
//        }

        // 병렬 스트림으로 Product 변환
//        List<PRODUCT> products = response.getItems().stream()
//                .limit(count)
//                .parallel()
//                .map(item -> {
//                    PRODUCT product = productMapper.map(item, category, seller);
//
//                   //  옵션 생성
//                    if (optionGenerator != null && optionGenerator.needsOptions(categoryName)) {
//                        optionGenerator.generateAndAddOptions(product, categoryName);
//                    }
//
//
//                    return product;
//                })
//                .toList();

//        log.info("{}개 상품 변환 완료", products.size());

        // 배치 저장
        return saveProductsBatch(pr);
    }

    /**
     * 배치 저장 (개선된 버전 - 실제 배치 INSERT 사용)
     */
    protected int saveProductsBatch(List<CreateProductDto> createProductDtos) {
        if (createProductDtos.isEmpty()) {
            return 0;
        }

        log.info("💾 배치 저장 중... ({}개)", createProductDtos.size());

        int savedCount = 0;
        int batchSize = 100; // 배치 크기 증가 (배치 INSERT 사용으로 더 많이 가능)
        int skippedCount = 0;

        for (int i = 0; i < createProductDtos.size(); i += batchSize) {
            int end = Math.min(i + batchSize, createProductDtos.size());
            List<CreateProductDto> batch = createProductDtos.subList(i, end);

            try {
                // 배치 단위로 트랜잭션 처리
                Integer batchSaved = transactionTemplate.execute(status -> {
                    try {
                        // 1. Product 준비 및 저장
                        List<Product> products = new ArrayList<>();
                        for (CreateProductDto productDto : batch) {
                            Product pr = productDto.getProduct();
                            pr.changeDuplicatedName();
                            products.add(pr);
                        }
                        
                        // 1. Product 배치 저장 (ID가 자동으로 채워짐)
                        log.info("🚀 배치 저장 시작: {}개 상품", products.size());
                        int actuallySavedProductCount = productProviderImpl.saveAll(products);
                        
                        if (actuallySavedProductCount == 0) {
                            log.error("❌ 배치 저장 실패: 상품 0개 저장됨 (요청: {}개) - INSERT 실패 또는 모든 상품 중복", products.size());
                            return 0;
                        }
                        
                        if (actuallySavedProductCount < products.size()) {
                            log.warn("⚠️ 배치 저장 부분 실패: 요청 {}개 중 {}개만 저장됨 ({}개는 중복으로 제외됨)", 
                                    products.size(), actuallySavedProductCount, 
                                    products.size() - actuallySavedProductCount);
                        }
                        
                        // 실제 저장된 상품만 사용 (ID가 할당된 것들)
                        // 주의: saveAll() 후에도 products 리스트의 ID는 null이므로
                        // 저장된 상품을 다시 조회하거나 다른 방법 사용 필요
                        List<Product> savedProducts = products.stream()
                                .filter(p -> p.getId() != null)
                                .toList();
                        
                        log.info("🔍 저장 후 products 리스트 ID 확인: 총 {}개 중 ID가 null이 아닌 것 {}개", 
                                products.size(), savedProducts.size());
                        
                        if (savedProducts.isEmpty()) {
                            log.error("❌ 저장된 상품이 없습니다 (ID가 null) - saveAll() 후 ID가 products 리스트에 반영되지 않음");
                            log.error("💡 해결 방법: saveAll()이 반환한 상품 리스트를 사용하거나, 저장 후 다시 조회 필요");
                            return 0;
                        }
                        
                        log.info("✅ 배치 저장 성공: {}개 상품 저장됨 (ID 할당됨: {}개)", 
                                actuallySavedProductCount, savedProducts.size());
                        
                        // 2. ProductImage 배치 생성 및 저장
                        List<ProductImage> images = new ArrayList<>();
                        for (int j = 0; j < batch.size() && j < savedProducts.size(); j++) {
                            String mainImg = batch.get(j).getMainImg();
                            ProductImage img = ProductImage.createDefaultProductImage(
                                    ProductImageType.MAIN, mainImg, savedProducts.get(j));
                            images.add(img);
                        }
                        if (!images.isEmpty()) {
                            imageProviderIml.saveAll(images);
                        }
                        
                        // 3. ProductDetail 배치 생성 및 저장
                        List<ProductDetail> details = new ArrayList<>();
                        for (Product savedProduct : savedProducts) {
                            for (int k = 0; k < 4; k++) {
                                ProductDetail pd = ProductDetail.createDefaultProductDetail(savedProduct, 100000);
                                details.add(pd);
                            }
                        }
                        productDetailProviderImpl.saveAll(details);
                        
                        // 4. ProductOptionMapping 배치 생성 및 저장
                        List<ProductOptionMapping> mappings = new ArrayList<>();
                        int detailIndex = 0;
                        
                        for (int productIdx = 0; productIdx < savedProducts.size(); productIdx++) {
                            Product savedProduct = savedProducts.get(productIdx);
                            Long sizeOpNum = 0L;
                            Long colorOpNum = 0L;
                            
                            for (int k = 0; k < 4; k++) {
                                if (detailIndex >= details.size()) break;
                                
                                ProductDetail savedProductDetail = details.get(detailIndex++);
                                
                                // Option ID 생성
                                Long randomColorOpNum = ThreadLocalRandom.current().nextLong(1, 13);
                                if (colorOpNum.equals(randomColorOpNum)) {
                                    while (true) {
                                        Long candidate = ThreadLocalRandom.current().nextLong(1, 13);
                                        if (!colorOpNum.equals(candidate)) {
                                            colorOpNum = candidate;
                                            break;
                                        }
                                    }
                                } else {
                                    colorOpNum = randomColorOpNum;
                                }
                                
                                Long randomSizeOpNum = ThreadLocalRandom.current().nextLong(13, 42);
                                if (sizeOpNum.equals(randomSizeOpNum)) {
                                    while (true) {
                                        Long candidate = ThreadLocalRandom.current().nextLong(13, 42);
                                        if (!sizeOpNum.equals(candidate)) {
                                            sizeOpNum = candidate;
                                            break;
                                        }
                                    }
                                } else {
                                    sizeOpNum = randomSizeOpNum;
                                }
                                
                                // Option 조회
                                Option colorOp = optionProviderImpl.findById(colorOpNum);
                                Option sizeOp = optionProviderImpl.findById(sizeOpNum);
                                
                                // Option이 null이면 스킵
                                if (colorOp != null && sizeOp != null) {
                                    ProductOptionMapping colorOpm = ProductOptionMapping.createDefaultProductOptionMapping(
                                            colorOp, savedProductDetail);
                                    ProductOptionMapping sizeOpm = ProductOptionMapping.createDefaultProductOptionMapping(
                                            sizeOp, savedProductDetail);
                                    mappings.add(colorOpm);
                                    mappings.add(sizeOpm);
                                }
                            }
                        }
                        
                        if (!mappings.isEmpty()) {
                            optionMappingProviderImpl.saveAll(mappings);
                        }
                        
                        // 실제 저장된 상품 개수 반환
                        return actuallySavedProductCount;
                    } catch (Exception e) {
                        log.error("배치 저장 중 에러: {}", e.getMessage(), e);
                        status.setRollbackOnly();
                        throw e;
                    }
                });
                
                if (batchSaved != null && batchSaved > 0) {
                    savedCount += batchSaved;
                } else {
                    skippedCount += batch.size();
                }
                
            } catch (org.springframework.transaction.CannotCreateTransactionException e) {
                log.error("⚠️ 배치 트랜잭션 생성 실패: {}", e.getMessage());
                skippedCount += batch.size();
            } catch (Exception e) {
                log.error("배치 저장 실패: {}-{}", i, end, e);
                skippedCount += batch.size();
            }
            
            if ((i + batchSize) % 500 == 0 || (i + batchSize) >= createProductDtos.size()) {
                log.info("저장 진행 상황: {}/{} (저장됨: {}개, 스킵됨: {}개)", 
                        Math.min(i + batchSize, createProductDtos.size()), 
                        createProductDtos.size(), savedCount, skippedCount);
            }
        }

        log.info("💾 배치 저장 완료: 총 {}개 중 저장됨 {}개, 스킵됨 {}개", 
                createProductDtos.size(), savedCount, skippedCount);

        return savedCount;
    }

    /**
     * 개별 상품 저장 (트랜잭션 단위)
     */
    protected int saveSingleProduct(CreateProductDto productDto) {
        try {
            Integer result = transactionTemplate.execute(status -> {
                try {
                    Product pr = productDto.getProduct();

                    pr.changeDuplicatedName();
                    Product savedProduct = productProviderImpl.save(pr);

                    String mainImg = productDto.getMainImg();
                    ProductImage img = ProductImage.createDefaultProductImage(ProductImageType.MAIN, mainImg, savedProduct);
                    imageProviderIml.save(img);

                    Long sizeOpNum = 0L;
                    Long colorOpNum = 0L;
                    int detailCount = 0;
                    int mappingCount = 0;

                    /// 4개의 디테일 생성
                    for(int k = 0; k < 4; k++){
                        ProductDetail pd = ProductDetail.createDefaultProductDetail(savedProduct, 100000);
                        ProductDetail savedProductDetail = productDetailProviderImpl.save(pd);
                        detailCount++;

                        Long randomColorOpNum = ThreadLocalRandom.current().nextLong(1, 13);
                        if (colorOpNum.equals(randomColorOpNum)) {
                            // pick until different, then assign and break
                            while (true) {
                                Long candidate = ThreadLocalRandom.current().nextLong(1, 13);
                                if (!colorOpNum.equals(candidate)) {
                                    colorOpNum = candidate;
                                    break;
                                }
                            }
                        } else {
                            colorOpNum = randomColorOpNum;
                        }

                        Long randomSizeOpNum = ThreadLocalRandom.current().nextLong(13, 42);
                        if (sizeOpNum.equals(randomSizeOpNum)) {
                            while (true) {
                                Long candidate = ThreadLocalRandom.current().nextLong(13, 42);
                                if (!sizeOpNum.equals(candidate)) {
                                    sizeOpNum = candidate;
                                    break;
                                }
                            }
                        } else {
                            sizeOpNum = randomSizeOpNum;
                        }

                        Option colorOp = optionProviderImpl.findById(colorOpNum);
                        Option sizeOp = optionProviderImpl.findById(sizeOpNum);

                        // Option이 null이면 해당 디테일은 저장하지 않고 스킵
                        if (colorOp == null || sizeOp == null) {
                            log.warn("Option을 찾을 수 없음: colorOpId={}, sizeOpId={}, ProductDetail 저장은 완료됨", 
                                    colorOpNum, sizeOpNum);
                            continue; // 이 디테일의 OptionMapping만 스킵, 다음 디테일로 진행
                        }

                        ProductOptionMapping colorOpm = ProductOptionMapping.createDefaultProductOptionMapping(colorOp, savedProductDetail);
                        ProductOptionMapping sizeOpm = ProductOptionMapping.createDefaultProductOptionMapping(sizeOp, savedProductDetail);

                        optionMappingProviderImpl.save(colorOpm);
                        optionMappingProviderImpl.save(sizeOpm);
                        mappingCount += 2;
                    }

                    // 트랜잭션 커밋 성공 확인
                    log.debug("상품 저장 완료: ProductId={}, Detail={}개, OptionMapping={}개", 
                            savedProduct.getId(), detailCount, mappingCount);
                    return 1;
                } catch (Exception e) {
                    log.error("트랜잭션 내부 에러 (롤백됨): {}", e.getMessage(), e);
                    status.setRollbackOnly();
                    throw e;
                }
            });
            
            // result가 null이면 트랜잭션 실패
            if (result == null || result == 0) {
                log.warn("상품 저장 실패: 트랜잭션 결과가 null 또는 0");
                return 0;
            }
            return result;
        } catch (org.springframework.transaction.CannotCreateTransactionException e) {
            log.error("⚠️ 트랜잭션 생성 실패 (EntityManager 접근 불가): {}", e.getMessage());
            return 0;
        } catch (Exception e) {
            log.error("상품 저장 트랜잭션 실패: error={}, message={}", 
                    e.getClass().getSimpleName(), e.getMessage());
            // 트랜잭션이 롤백되었으므로 0 반환
            return 0;
        }
    }

    /**
     * 리프 노드 카테고리 조회
     */
    protected List<CATEGORY> findLeafCategories() {
        ///  전체 카테고리 찾아옴.
        List<CATEGORY> allCategories = categoryProvider.findAllCategories();
        Set<Long> parentIds = new HashSet<>();

        for (CATEGORY category : allCategories) {
            Long parentId = categoryProvider.getParentCategoryId(category);
            if (parentId != null) {
                parentIds.add(parentId);
            }
        }

        List<CATEGORY> leafCategories = allCategories.stream()
                .filter(category -> !parentIds.contains(
                        categoryProvider.getCategoryId(category)))
                .toList();

        log.info("전체 카테고리: {}개, 리프 카테고리: {}개",
                allCategories.size(), leafCategories.size());
        return leafCategories;
    }

    /**
     * 전체 카테고리 경로 생성
     */
    protected String buildFullCategoryPath(CATEGORY category) {
        List<String> pathNames = new ArrayList<>();
        CATEGORY current = category;

        while (current != null) {
            String name = categoryProvider.getCategoryName(current);
            pathNames.add(name);

            Long parentId = categoryProvider.getParentCategoryId(current);
            if (parentId != null) {
                current = categoryProvider.findById(parentId);
            } else {
                break;
            }
        }

        Collections.reverse(pathNames);
        String fullPath = String.join(" ", pathNames);
        return sanitizeKeyword(fullPath);
    }

    protected String sanitizeKeyword(String keyword) {
        if (keyword == null) return "";

        return keyword
                .replace("+", " ")
                .replace("·", " ")
                .replace("、", " ")
                .replace("，", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    @Data
    @Builder
    public static class CrawlingResult {
        private Integer totalCategories;
        private Integer successCategories;
        private Integer failedCategories;
        private Integer totalProducts;
        private Long durationSeconds;
        private List<CategoryResult> categoryResults;
    }

    @Data
    @Builder
    public static class CategoryResult {
        private Long categoryId;
        private String categoryName;
        private Integer productCount;
        private String status;
        private String error;

        public static CategoryResult success(Long id, String name, Integer count) {
            return CategoryResult.builder()
                    .categoryId(id)
                    .categoryName(name)
                    .productCount(count)
                    .status("SUCCESS")
                    .build();
        }

        public static CategoryResult noResults(Long id, String name) {
            return CategoryResult.builder()
                    .categoryId(id)
                    .categoryName(name)
                    .productCount(0)
                    .status("NO_RESULTS")
                    .build();
        }

        public static CategoryResult failed(Long id, String name, String error) {
            return CategoryResult.builder()
                    .categoryId(id)
                    .categoryName(name)
                    .productCount(0)
                    .status("FAILED")
                    .error(error)
                    .build();
        }
    }
}