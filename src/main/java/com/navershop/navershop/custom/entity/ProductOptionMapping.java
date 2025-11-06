package com.navershop.navershop.custom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * ProductOptionMapping 엔티티 (옵션과 상품 상세 연결)
 */
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_option_mapping")
public class ProductOptionMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_mapping_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Option option;

    @ManyToOne
    @JoinColumn(name = "product_detail_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductDetail productDetail;

    @ColumnDefault("0")
    private boolean isDeleted;

    public static ProductOptionMapping createDefaultProductOptionMapping(Option option, ProductDetail productDetail) {

        return ProductOptionMapping.builder()
                .option(option)
                .productDetail(productDetail)
                .build();
    }

}