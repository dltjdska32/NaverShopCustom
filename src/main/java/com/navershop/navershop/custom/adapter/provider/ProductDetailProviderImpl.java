package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.ProductDetail;
import com.navershop.navershop.custom.entity.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDetailProviderImpl {
    private final ProductDetailRepository productDetailRepository;

    public void saveAll(List<ProductDetail> productDetail){
        productDetailRepository.saveAll(productDetail);
    }

    public ProductDetail save(ProductDetail productDetail){
        return productDetailRepository.save(productDetail);
    }
}