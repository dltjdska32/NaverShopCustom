package com.navershop.navershop.custom.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum BrandEnum {

    NIKE("SHOE_001", "나이키", "이민호"),
    ADIDAS("SHOE_002", "아디다스", "김지수"),
    NEW_BALANCE("SHOE_003", "뉴발란스", "박서준"),
    CONVERSE("SHOE_004", "컨버스", "최유진"),
    VANS("SHOE_005", "반스", "강태오"),
    PUMA("SHOE_006", "푸마", "윤서아"),
    REEBOK("SHOE_007", "리복", "정해인"),
    ASICS("SHOE_008", "아식스", "김태리"),
    HOKA("SHOE_009", "호카", "송강"),
    ON_RUNNING("SHOE_010", "온러닝", "한소희"),
    SALOMON("SHOE_011", "살로몬", "이도현"),
    CROCS("SHOE_012", "크록스", "박은빈"),
    BIRKENSTOCK("SHOE_013", "버켄스탁", "차은우"),
    DR_MARTENS("SHOE_014", "닥터마틴", "고윤정"),
    UGG("SHOE_015", "어그", "황민현"),
    TIMBERLAND("SHOE_016", "팀버랜드", "김유정"),
    SKECHERS("SHOE_017", "스케쳐스", "이재욱"),
    LACOSTE("SHOE_018", "라코스테", "김다미"),
    CLARKS("SHOE_019", "클락스", "안효섭"),
    CAMPER("SHOE_020", "캠퍼", "신혜선"),
    GOLDEN_GOOSE("SHOE_021", "골든구스", "이준호"),
    BALENCIAGA("SHOE_022", "발렌시아가", "임윤아"),
    MAISON_MARGIELA("SHOE_023", "메종 마르지엘라", "뷔"),
    GUCCI("SHOE_024", "구찌", "제니"),
    PRADA("SHOE_025", "프라다", "송중기"),
    ALDO("SHOE_026", "알도", "아이유"),
    GEOX("SHOE_027", "제옥스", "박보검"),
    REPETTO("SHOE_028", "레페토", "수지"),
    MERRELL("SHOE_029", "메렐", "유연석"),
    FILA("SHOE_030", "휠라", "김고은");

    // Enum 필드 (DB 컬럼과 매칭)
    private final String brandCode;
    private final String brandName;
    private final String presidentName;


    BrandEnum(String brandCode, String brandName, String presidentName) {
        this.brandCode = brandCode;
        this.brandName = brandName;
        this.presidentName = presidentName;
    }



    /**
     * 모든 브랜드의 '한글 이름' 리스트(List<String>)를 반환합니다.
     * @return List<String> (모든 브랜드의 이름)
     */
    public static List<String> getAllBrandNames() {
        // Java 8 스트림을 사용하여 'brandName' 필드만 추출
        return Arrays.stream(BrandEnum.values())
                .map(BrandEnum::getBrandName) // .map(brand -> brand.getBrandName()) 과 동일
                .collect(Collectors.toList());
    }
}

