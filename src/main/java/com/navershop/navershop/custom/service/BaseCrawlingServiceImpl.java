package com.navershop.navershop.custom.service;

import com.google.common.util.concurrent.RateLimiter;
import com.navershop.navershop.core.api.NaverShoppingApiClient;
import com.navershop.navershop.custom.adapter.provider.*;
import com.navershop.navershop.custom.entity.Category;
import com.navershop.navershop.custom.entity.Product;
import com.navershop.navershop.custom.entity.User;
import com.navershop.navershop.template.adapter.mapper.ProductMapper;
import com.navershop.navershop.template.adapter.option.OptionGenerator;
import com.navershop.navershop.template.adapter.provider.category.CategoryProvider;
import com.navershop.navershop.template.adapter.provider.product.ProductProvider;
import com.navershop.navershop.template.adapter.provider.user.UserProvider;
import com.navershop.navershop.template.service.BaseCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Executor;

/**
 * BaseCrawlingService 구현체
 */
@Service
public class BaseCrawlingServiceImpl extends BaseCrawlingService<Product, Category, User> {

    public BaseCrawlingServiceImpl(
            NaverShoppingApiClient apiClient,
            ProductMapper<Product, Category, User> productMapper,
            ProductProvider<Product> productProvider,
            CategoryProvider<Category> categoryProvider,
            UserProvider<User> userProvider,
            @Autowired(required = false) OptionGenerator<Product> optionGenerator,
            ProductDetailProviderImpl productDetailProvider,
            OptionMappingProviderImpl optionMappingProvider,
            ImageProviderIml imageProvider,
            OptionProviderImpl optionProvider,
            ProductProviderImpl productProviderImpl,
            @Qualifier("guavaRateLimiter") RateLimiter rateLimiter,
            PlatformTransactionManager transactionManager,
            @Qualifier("crawlingExecutor") Executor crawlingExecutor) {
        super(apiClient, productMapper, productProvider, categoryProvider, userProvider, 
                optionGenerator, productDetailProvider, optionMappingProvider, 
                imageProvider, optionProvider, productProviderImpl, rateLimiter, 
                transactionManager, crawlingExecutor);
    }
}