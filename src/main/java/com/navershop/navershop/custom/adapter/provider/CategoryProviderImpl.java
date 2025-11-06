package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.Category;
import com.navershop.navershop.custom.entity.repository.CategoryRepository;
import com.navershop.navershop.template.adapter.provider.category.CategoryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryProviderImpl implements CategoryProvider<Category> {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public Long getCategoryId(Category category) {
        return category.getId();
    }

    @Override
    public String getCategoryName(Category category) {
        return category.getCategoryName();
    }

    @Override
    public Long getParentCategoryId(Category category) {

        Category parent = category.getRefCategory();

        if (parent == null) {
            return null; // 최상위 카테고리이므로 부모 ID는 null입니다.
        }

        return parent.getId();
    }

    @Override
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
