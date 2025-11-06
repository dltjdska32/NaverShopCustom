package com.navershop.navershop.custom.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 브랜드 목록 Enum
 */
public enum BrandEnum {
    // RandomBrand에 있는 브랜드 목록을 여기에 추가하거나
    // getAllBrandNames() 메서드에서 RandomBrand를 참조하도록 할 수 있습니다.
    
    BRAND_1("나무결"),
    BRAND_2("Woodora"),
    BRAND_3("더우드룸 (The Wood Room)"),
    BRAND_4("Oakline"),
    BRAND_5("포레스트홈 (Forest Home)");
    
    private final String name;
    
    BrandEnum(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 모든 브랜드 이름 목록 반환
     * RandomBrand의 브랜드 목록을 사용하거나
     * 여기서 직접 리스트로 반환할 수 있습니다.
     */
    public static List<String> getAllBrandNames() {
        // TODO: RandomBrand 컴포넌트를 주입받아 사용하거나
        // 여기서 직접 리스트로 반환
        return Arrays.asList(
            "나무결", "Woodora", "더우드룸 (The Wood Room)", "Oakline", "포레스트홈 (Forest Home)",
            "Timberia", "라운우드 (Roundwood)", "Mossé (모쎄)", "그루브우드 (Groove Wood)", "숲결 (Supgyeol)",
            "Hjem", "Luno", "미누홈 (Minuhome)", "Alba House", "노드레 (Nordre)",
            "Maison Kora", "Evra", "Lineé", "루미에홈 (Lumière Home)", "Skané",
            "Cozyne", "하우미 (Howme)", "홈바운드 (Homebound)", "Dailynest", "플레니홈 (Plenihome)",
            "루미하우스 (Lumihouse)", "Nuvéa", "오하우스 (Oh! House)", "LittleCorner", "스윗룸 (Sweetroom)",
            "Modenza", "라인하우스 (Linehouse)", "Forma", "아틀리에홈 (Atelier Home)", "Bravia",
            "ModoHome", "아르벨 (Arvel)", "Corevo", "Roomer", "스테이모드 (StayMode)",
            "Handen", "Woodmark", "하늘목공소 (Sky Workshop)", "BentoWood", "Treeform",
            "카프라 (Kapra)", "Madelee", "수공방 (Sugongbang)", "ArdenWood", "리빙크래프트 (LivingCraft)",
            "오크앤하우스 (Oak & House)", "Verano", "LaViel", "Heritage Home", "Maison Blu",
            "오브제하우스 (Objet House)", "Florin", "Chêne (셴)", "Armond", "엘레노아 (Elenoa)",
            "Roomie", "홈톡 (Hometalk)", "토토홈 (TotoHome)", "Nestie", "라룸 (Laroom)",
            "Popline", "Homyday", "스튜디오룸 (Studio Room)", "DecoBuddy", "리브홈 (LiveHome)",
            "Plantry", "에코룸 (EcoRoom)", "Grainly", "Leafnote", "하우스포레 (House Foret)",
            "Greenea", "Woodplain", "루트하우스 (Root House)", "Arboris", "수피홈 (SupiHome)",
            "하우젠 (Hauzen)", "가온홈 (GaonHome)", "온결 (OnGyeol)", "담소가구 (Damsoga)", "연우리빙 (Yeonwoo Living)",
            "모담 (Modam)", "소담하우스 (Sodam House)", "다온홈 (DaonHome)", "채온 (Chaon)", "나린가구 (Narin Furniture)",
            "Flatory", "루미룸 (LumiRoom)", "Moona", "FormaNest", "RoomLab",
            "메종루트 (Maison Route)", "Layered Home", "Ardora", "라인앤홈 (Line & Home)", "Velano"
        );
    }
}
