package com.navershop.navershop.custom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductDetail 엔티티 (상품 상세 - 옵션별 재고 관리)
 */
@Builder
@Entity
@Table(name = "product_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    private Integer quantity;

    @ColumnDefault("0")
    private boolean isDeleted;

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductOptionMapping>  optionMappings = new ArrayList<>();

    public static ProductDetail createDefaultProductDetail(Product product, Integer quantity) {

        if(quantity == null) {
            throw new IllegalArgumentException("quantity cannot be null");
        }
        if(product == null) {
            throw new IllegalArgumentException("product cannot be null");
        }
        if(quantity < 1) {
            throw new IllegalArgumentException("quantity cannot be less than 1");
        }

        return ProductDetail.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    public void deductStock(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("재고 차감 오류: 재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }

    public void restoreStock(int quantity) {
        if (this.quantity == null) {
            this.quantity = 0;
        }

        this.quantity += quantity;
    }
}