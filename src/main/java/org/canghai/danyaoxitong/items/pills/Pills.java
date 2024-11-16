package org.canghai.danyaoxitong.items.pills;

public enum Pills {
    SUPERIOR_1("回神丹", 1),
    SUPERIOR_2("敏攻丹", 1),
    SUPERIOR_3("虚甲丹", 1),
    SUPERIOR_4("化风丹", 1),
    SUPERIOR_5("夜明丹", 1),
    SUPERIOR_6("抗素丹", 1),
    SUPERIOR_7("净心丹", 1),
    SUPERIOR_8("隐修丹", 1),
    SUPERIOR_9("重命丹", 1),
    SUPERIOR_10("藏体丹", 1),
    CELESTIAL_1("安魂丹", 2),
    CELESTIAL_2("气血丹", 2),
    CELESTIAL_3("壮阳丹", 2),
    CELESTIAL_4("引灵丹", 2),
    RARE_1("还魂丹", 3),
    RARE_2("骤气丹", 3),
    RARE_3("重阳丹", 3),
    RARE_4("聚灵丹", 3);

    private final String displayName;
    private final int quality;

    Pills(String displayName, int quality) {
        this.displayName = displayName;
        this.quality = quality;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getQuality() {
        return quality;
    }

}
