package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.ProductImage;
import com.navershop.navershop.custom.entity.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageProviderIml {
    private final ProductImageRepository productImageRepository;

    public  void save(ProductImage productImage){
        productImageRepository.save(productImage);
    }

    public void saveAll(List<ProductImage> productImages) {
        productImageRepository.saveAll(productImages);
    }
}