package org.canghai.danyaoxitong;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.canghai.danyaoxitong.items.herbs.HerbType;
import org.canghai.danyaoxitong.items.pills.PillType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;


public class AlchemyCraft {
    private int level = 0; //  品质等级 1-3
    private String lastLore = "";
    private final Random random = new Random(); //  用来随机生成丹药

    public AlchemyCraft() {

    }

    /*
        检查给定的药草品质是否相同   改为遍历AlchemyListener传进来的嵌套List，获取每个内层List的数据（Lore），
        再根据Lore对应的level来制定要合成的丹药
    */

    public int checkRecipe(List<List<String>> ingredients, Player player) {
        int size = ingredients.size();
        for (List<String> innerList : ingredients) {

            String firstLore = innerList.get(0);
            //  调试
            player.sendMessage(firstLore);
            if (lastLore != "" && firstLore != lastLore) {
                player.sendMessage("请输入相同品质的物品物品");
                return 0;
            }
            lastLore = firstLore;
            if(size == 5) {
                switch (firstLore) {
                    case "上品药草":
                        level = 1;
                        break;
                    case "仙品药草":
                        level = 2;
                        break;
                    case "稀世药草":
                        level = 3;
                        break;
                }
            }
        }
        return level;
    }

    // 根据配方生成对应的丹药物品
    public ItemStack createPill(int pillLevel) {
        if (pillLevel != 0) {
            ItemStack pillItem = new ItemStack(Material.POTION);//  丹药材质待定
            ItemMeta meta = pillItem.getItemMeta();

            List<ItemStack> possiblePills = new ArrayList<>();
            //  遍历丹药数据并将合成结果对应品质的丹药放到静态集合里
            for (Map.Entry<String, PillType> entry : PillType.getLoadedPills().entrySet()) {
                PillType pill = entry.getValue();

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

