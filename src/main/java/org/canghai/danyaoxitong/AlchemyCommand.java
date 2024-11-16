package org.canghai.danyaoxitong;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AlchemyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 检查参数数量
        if (args.length < 3) {
            sender.sendMessage("§c用法: /alchemy <give> <random> <id/level>");
            return true;
        }
        String mode = args[1];
        String level = args[2];
        ItemStack item = (new AlchemyCraft()).createPill(Integer.parseInt(level));
        ((Player) sender).getInventory().addItem(item);
        return true;
    }
}
