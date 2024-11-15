package org.canghai.danyaoxitong;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.canghai.danyaoxitong.items.herbs.HerbType;
import org.canghai.danyaoxitong.items.pills.PillType;

import java.util.*;


public class AlchemyCraft {
    private final Random random = new Random(); //  用来随机生成丹药

    public AlchemyCraft() {

    }

    /*
        检查给定的药草品质是否相同   改为遍历AlchemyListener传进来的嵌套List，获取每个内层List的数据（Lore），
        再根据Lore对应的level来制定要合成的丹药
    */

    // 根据配方生成对应的丹药物品
    public ItemStack createPill(int pillLevel) {
        if (pillLevel != 0) {
            List<ItemStack> possiblePills = new ArrayList<>();
            //  遍历丹药数据并将合成结果对应品质的丹药放到静态集合里

            for (Map.Entry<String, PillType> entry : PillType.getLoadedPills().entrySet()) {
                PillType pill = entry.getValue();
                ItemStack pillItem = new ItemStack(pill.getMaterial());//  丹药材质待定
                ItemMeta meta = pillItem.getItemMeta();

                meta.setDisplayName(pill.getDisplayName());
                pillItem.setItemMeta(meta);

                pillItem.setLore(Arrays.asList(pill.getDescription()));
                if (pill.getQuality() == pillLevel) {
                    possiblePills.add(pillItem);
                }
            }

            if (!possiblePills.isEmpty()) {
                ItemStack selectedPill = possiblePills.get(random.nextInt(possiblePills.size()));
                return selectedPill;
            }
        }
        return null;
    }
}

