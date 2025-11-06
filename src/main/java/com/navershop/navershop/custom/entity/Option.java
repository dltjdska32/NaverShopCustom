package com.navershop.navershop.custom.entity;

import com.navershop.navershop.custom.entity.enums.OptionCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Option 엔티티 (옵션 - 색상, 사이즈 등)
 */
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_option")
public class Option extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @Convert(converter = OptionConverter.class)
    private OptionCategory optionType;

    private String optionName;

    public static Option createDefaultOption(OptionCategory optionType, String optionName) {

        return Option.builder()
                .optionType(optionType)
                .optionName(optionName)
                .build();
    }


}