package org.canghai.danyaoxitong.listeners;

import org.canghai.danyaoxitong.items.herbs.HerbType;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class HerbHarvestListener implements Listener {
    //  定义随机数以表示概率
    private final Random random = new Random();
    private static final Set<Material> CROPS = Set.of(
            Material.WHEAT,               // 小麦
            Material.CARROTS,             // 胡萝卜
            Material.POTATOES,            // 马铃薯
            Material.BEETROOTS,           // 甜菜根
            Material.MELON_STEM,          // 西瓜茎
            Material.PUMPKIN_STEM,        // 南瓜茎
            Material.SUGAR_CANE,          // 甘蔗
            Material.BAMBOO,              // 竹子
            Material.NETHER_WART,         // 下界疣
            Material.COCOA                // 可可豆
    );
    //  方块破坏事件监听
    @EventHandler
    public void onCropHarvest(BlockBreakEvent event) {
        Block block = event.getBlock();
        //  判断是否为可种植作物
        if (!CROPS.contains(event.getBlock().getType())) {
            return;
        }

        // 检查作物是否成熟
        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            if (ageable.getAge() < ageable.getMaximumAge()) {
                // 如果作物未成熟，直接返回，不掉落自定义药草
                return;
            }
        }

        // 保留原版掉落物
        event.setDropItems(true);

        //  定义数组来存放满足概率条件的药草（一类）
        List<ItemStack> possibleHerbs = new ArrayList<>();
        //  生成概率
        double chance = random.nextDouble();

        String message = String.valueOf(chance);
        // 获取触发事件的玩家
        Player player = event.getPlayer();

        // 发送消息给玩家(调试)
        player.sendMessage(message);
        //  遍历加载的药草数据
        for (Map.Entry<String, HerbType> entry : HerbType.getLoadedHerbs().entrySet()) {
            HerbType herb = entry.getValue();
            //  判断药草类型
            if (chance < herb.getDropChance()) {

                //  根据药草类型创建物品实例
                ItemStack herbItem = new ItemStack(herb.getMaterial());
                ItemMeta meta = herbItem.getItemMeta();

                if (meta != null) {
                    meta.setDisplayName(herb.getDisplayName());
                    herbItem.setItemMeta(meta);
                }

                //  将满足条件的物品放入集合中
                possibleHerbs.add(herbItem);
            }
        }

        //  掉落药草
        if (!possibleHerbs.isEmpty()) {
            ItemStack selectedHerb = possibleHerbs.get(random.nextInt(possibleHerbs.size()));
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), selectedHerb);
        }
    }
}