package org.canghai.danyaoxitong.listeners;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.canghai.danyaoxitong.api.ValueChangeHandler;
import org.canghai.danyaoxitong.items.pills.Pills;

import java.time.Duration;
import java.util.Objects;

public class PillUsageListener implements Listener {

    @EventHandler
    public void onPillConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        // 检查玩家使用的是否是自定义的丹药
        ItemStack pillItem = event.getItem();
        ItemMeta meta = pillItem.getItemMeta();
        String name = meta.getDisplayName();
        Pills[] pills = Pills.values();

        for (Pills pill : pills) {
            if (name.equals(pill.getDisplayName())) {
                // 根据丹药类型给予效果
                player.sendMessage("你服用了" + name +"丹药，获得了特殊效果！");
                ValueChangeHandler handler = new ValueChangeHandler(event.getPlayer());
                switch (name) {
                    case "回神丹":
                        // 恢复20%生命值
                        // TODO
                    case "敏攻丹":
                        // 60s内增加10%攻击力
                        // TODO

                    case "虚甲丹":
                        // 60s内伤害减免10%
                        // TODO

                    case "化风丹":
                        // 60s内移动速度增加50%
                        // TODO
                    case "夜明丹":
                        // 60s夜视
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 60 * 20, 0));

                    case "抗素丹":
                        // 60s抗火、水下呼吸
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60 * 20 , 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60 * 20, 0));

                    case "净心丹":
                        // 清除所有buff
                        player.clearActivePotionEffects();

                    case "隐修丹":
                        // 隐藏当前修为
                        // TODO

                    case "重命丹":
                        // 回到上一次死亡地点
                        // TODO

                    case "藏体丹":
                        // 隐身60s
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60 * 20, 0));

                    case "安魂丹":
                        // 永久增加10生命值
                        // TODO
                        handler.addHealth(10);

                    case "气血丹":
                        // 永久增加1攻击力
                        // TODO
                        handler.addAttack(1);

                    case "壮阳丹":
                        // 永久增加1防御值
                        // TODO
                        handler.addDefense(1);

                    case "引灵丹":
                        // 武器所需灵力值减少1%
                        // TODO

                    case "还魂丹":
                        // 永久增加10生命值
                        // TODO
                        handler.addHealth(10);

                    case "骤气丹":
                        // 永久增加1攻击力
                        // TODO
                        handler.addAttack(1);

                    case "重阳丹":
                        // 永久增加1防御值
                        // TODO
                        handler.addDefense(1);

                    case "聚灵丹":
                        // 每分钟获得10灵力值
                        // TODO
                }
            }
        }
    }
}
