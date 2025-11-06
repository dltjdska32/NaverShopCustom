package com.navershop.navershop.custom.entity;

import com.navershop.navershop.custom.entity.enums.ProductImageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * ProductImage 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_image")
@Builder
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductImageType imageType;

    private String imgUrl;

    @ColumnDefault("0")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;


    public static ProductImage createDefaultProductImage(ProductImageType imageType,
                                                  String imgUrl,
                                                  Product product) {
        return ProductImage.builder()
                .imageType(imageType)
                .imgUrl(imgUrl)
                .product(product)
                .build();
    }

    public void deleteImage() {
        this.isDeleted = true;
    }

    public void updateProductImage(String imagUrl) {
        this.imgUrl = imagUrl;
    }
}