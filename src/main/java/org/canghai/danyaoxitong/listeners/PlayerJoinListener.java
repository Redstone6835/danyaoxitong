package org.canghai.danyaoxitong.listeners;

import org.canghai.danyaoxitong.items.herbs.HerbType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

public class PlayerJoinListener implements Listener {
    //  将新物品添加到玩家背包
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 只对创造模式玩家进行操作
        if (player.getGameMode().toString().equals("CREATIVE")) {
            //  给予玩家炼丹炉（调试）
            ItemStack alchemyFurnace = new ItemStack(Material.FURNACE);
            ItemMeta meta = alchemyFurnace.getItemMeta();
            meta.setDisplayName("炼丹炉");  // 设置自定义名称
            alchemyFurnace.setItemMeta(meta);

            // 给予玩家炼丹炉方块
            player.getInventory().addItem(alchemyFurnace);

            for (Map.Entry<String, HerbType> entry : HerbType.getLoadedHerbs().entrySet()) {
                HerbType herb = entry.getValue();
                ItemStack herbItem = new ItemStack(herb.getMaterial());
                herbItem.getItemMeta().setDisplayName(herb.getDisplayName());
                player.getInventory().addItem(herbItem);
            }
            // 给玩家的创造模式库存添加丹药(要实现)
        }
    }
}
