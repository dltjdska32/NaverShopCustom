package com.navershop.navershop.custom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 상품 엔티티
 */

 @Builder
 @Entity
 @Table(name = "product")
 @Getter
 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 @AllArgsConstructor
 public class Product extends BaseEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "product_id")
     private Long id;
     private String info;
     private String name;
     private Long price;

     @ColumnDefault("0")
     @Builder.Default
     private Integer likeCount = 0;

     @ColumnDefault("0")
     @Builder.Default
     private Integer salesCount = 0;

     @ColumnDefault("0")
     @Builder.Default
     private Integer viewCount = 0;

     /// 단방향 매핑으로 설정.
     /// foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT) 외래키 제약 조건을 걸지않음
     @ManyToOne
     @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
     private Brand brand;

     @ManyToOne
     @JoinColumn(name = "product_category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
     private Category category;

     @ManyToOne
     @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
     private User member;

     @ColumnDefault("0")
     private boolean isDeleted;

     @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
     @Builder.Default
     private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductDetail> productDetails = new ArrayList<>();

    /// 중복 상품명 변경 시 사용할 형용사 리스트
    private static final List<String> ADJECTIVES = List.of(
            "프리미엄", "럭셔리", "스타일리시", "모던", "클래식", "트렌디", "세련된", "고급스러운",
            "편안한", "부드러운", "따뜻한", "시원한", "깔끔한", "세련된", "우아한", "화사한",
            "강력한", "효과적인", "실용적인", "혁신적인", "독특한", "특별한", "프리미엄", "럭셔리"
    );

    public static Product createDefaultProduct(String name,
                                                String info,
                                                Long price,
                                                Brand brand,
                                                Category category,
                                                User member
                                                ) {

         if(brand == null) {
             throw new IllegalArgumentException("brand cannot be null");
         }
         if(category == null) {
             throw new IllegalArgumentException("category cannot be null");
         }
         if(price == null){
             throw new IllegalArgumentException("price cannot be null");
         }
         if(price < 0) {
             throw new IllegalArgumentException("price cannot be negative");
         }
         if(info == null ||  info.isEmpty() || info.length() < 10) {
             throw new IllegalArgumentException("product information must be at least 10 characters long.");
         }
         if(info.length() > 65535) {
             throw new IllegalArgumentException("Product information is too long. A maximum of 65535 characters is allowed.");
         }
         if(name == null || name.isEmpty()) {
             throw new IllegalArgumentException("name cannot be null or empty");
         }
         if(member == null) {
             throw new IllegalArgumentException("member cannot be null");
         }

         return Product.builder()
                 .brand(brand)
                 .info(info)
                 .price(price)
                 .category(category)
                 .name(name)
                 .member(member)
                 .build();
     }


     public void updateProduct(String name,
                               String info,
                               Long price) {

         if(price < 0) {
             throw new IllegalArgumentException("가격은 0보다 작을수 없습니다.");
         }
         this.name = name;
         this.info = info;
         this.price = price;
     }

     /// 상품 논리적 제거
     public void deleteProduct() {
         this.isDeleted = true;
     }

     /// 좋아요 감소
     public void minusLike() {
         if(this.likeCount <= 0) {
             throw new IllegalArgumentException("like count cannot be negative");
         }
         this.likeCount -= 1;
     }

     /// 좋아요 증가
     public void plusLike() {
         this.likeCount += 1;
     }

     /// 판매량 감소
     public void minusSalesCount(int salesCount) {
         if(this.salesCount - salesCount < 0) {
             throw new IllegalArgumentException("salesCount cannot be negative");
         }

         this.salesCount -= salesCount;
     }

     /// 판매량 증가.
     public void plusSalesCount(int salesCount) {
         this.salesCount += salesCount;
     }

     /// 중복 상품명 변경 메서드
     /// 형식: 형용사 + 브랜드명 + 원래이름
     public void changeDuplicatedName() {

         // 형용사 리스트
         List<String> adjectives = List.of(
                 "프리미엄", "럭셔리", "스타일리시", "모던", "클래식", "트렌디", "세련된", "고급스러운",
                 "편안한", "부드러운", "따뜻한", "시원한", "깔끔한", "우아한", "화사한",
                 "강력한", "효과적인", "실용적인", "혁신적인", "독특한", "특별한"
         );
         
         Random random = new Random();
         String randomAdjective = adjectives.get(random.nextInt(adjectives.size()));
         Faker faker = new Faker();
         String brandName = faker.commerce().productName();
         
         this.name = randomAdjective + " " + brandName + " " + this.name;
     }

 }