package com.navershop.navershop.custom.adapter.mapper;

import com.navershop.navershop.core.dto.NaverShoppingResponse;
import com.navershop.navershop.custom.entity.Brand;
import com.navershop.navershop.custom.entity.Category;
import com.navershop.navershop.custom.entity.Product;
import com.navershop.navershop.custom.entity.User;
import com.navershop.navershop.custom.entity.repository.BrandRepository;
import com.navershop.navershop.template.adapter.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 제품 <-> 네이버 제품 매핑
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements ProductMapper<Product, Category, User> {
    private final BrandRepository brandRepository;
    
    @Override
    public Product map(NaverShoppingResponse.NaverShoppingItem item, Category category, User seller) {

        Long randomBrandId = ThreadLocalRandom.current().nextLong(1, 31);

        Brand brand = brandRepository.findById(randomBrandId).orElse(null);
        Long price = 10000L;
        if(item.getHprice() != null){
            price = Long.parseLong(item.getHprice());
        } else {
            price = Long.parseLong(item.getLprice());
        }

        String info = "멋진 신발입니다. 착화감이 매우 좋습니다. 한번 신으면 다른거 못 신습니다.";

        return Product.createDefaultProduct(item.getTitle(), info, price, brand, category, seller);
    }
}