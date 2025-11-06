package com.navershop.navershop.custom.entity;

import com.navershop.navershop.custom.entity.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 카테고리 엔티티
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_category")
@Entity
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private Long id;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "ref_product_category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category refCategory;

    @Convert(converter = ProductCategoryConverter.class)
    private ProductCategory categoryType;

    private String categoryName;

    private Integer level;

}