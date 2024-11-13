package org.canghai.danyaoxitong.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.entity.Player;

public class PillUsageListener implements Listener {

    @EventHandler
    public void onPillConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        // 检查玩家使用的是否是自定义的丹药
        // 根据丹药类型给予效果
        player.sendMessage("你服用了丹药，获得了特殊效果！");
        // 示例：可以添加具体的效果代码，如增加生命值等
    }
}
