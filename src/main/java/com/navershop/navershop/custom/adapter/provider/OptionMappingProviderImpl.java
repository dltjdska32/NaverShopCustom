package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.ProductOptionMapping;
import com.navershop.navershop.custom.entity.repository.ProductOptionMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OptionMappingProviderImpl {
    private final ProductOptionMappingRepository productOptionMappingRepository;

    public void save(ProductOptionMapping productOptionMapping) {
        productOptionMappingRepository.save(productOptionMapping);
    }

    public void saveAll(List<ProductOptionMapping> productOptionMappings) {
        productOptionMappingRepository.saveAll(productOptionMappings);
    }
}