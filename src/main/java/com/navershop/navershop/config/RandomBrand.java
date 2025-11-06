package com.navershop.navershop.config;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 브랜드 이름 랜덤으로 100개 만들었습니다 :)
 *
 * @author junnukim1007gmail.com
 * @date 25. 10. 29.
 */
@Component
public class RandomBrand {

    private static final List<String> BRAND_LIST = Arrays.asList(
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

    private final Random random = new Random();

    /**
     * 전체 브랜드 리스트 조회
     */
    public List<String> getAllBrands() {
        return BRAND_LIST;
    }

    /**
     * 랜덤 브랜드 선택
     */
    public String getRandomBrand() {
        return BRAND_LIST.get(random.nextInt(BRAND_LIST.size()));
    }

    /**
     * 브랜드 유효성 검사
     */
    public boolean isValidBrand(String brand) {
        return brand != null && !brand.trim().isEmpty();
    }

    /**
     * 브랜드 처리 - 없으면 랜덤 선택
     */
    public String getOrDefault(String brand) {
        return isValidBrand(brand) ? brand : getRandomBrand();
    }
}
